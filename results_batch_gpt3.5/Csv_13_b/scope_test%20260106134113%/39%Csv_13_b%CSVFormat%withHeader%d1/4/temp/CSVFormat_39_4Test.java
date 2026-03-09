package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatWithHeaderTest {

    private CSVFormat baseFormat;

    @BeforeEach
    void setUp() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void withHeader_nullMetaData_returnsFormatWithNullHeader() throws SQLException {
        CSVFormat format = baseFormat.withHeader((ResultSetMetaData) null);
        assertNotNull(format);
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void withHeader_metaDataWithNoColumns_returnsFormatWithEmptyHeader() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(0);

        CSVFormat format = baseFormat.withHeader(metaData);

        assertNotNull(format);
        String[] header = format.getHeader();
        assertNotNull(header);
        assertEquals(0, header.length);
    }

    @Test
    @Timeout(8000)
    void withHeader_metaDataWithColumns_returnsFormatWithCorrectHeader() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("id");
        when(metaData.getColumnLabel(2)).thenReturn("name");
        when(metaData.getColumnLabel(3)).thenReturn("email");

        CSVFormat format = baseFormat.withHeader(metaData);

        assertNotNull(format);
        String[] header = format.getHeader();
        assertNotNull(header);
        assertEquals(3, header.length);
        assertArrayEquals(new String[]{"id", "name", "email"}, header);
    }

    @Test
    @Timeout(8000)
    void withHeader_metaDataThrowsSQLException_propagatesException() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenThrow(new SQLException("SQL error"));

        SQLException thrown = assertThrows(SQLException.class, () -> baseFormat.withHeader(metaData));
        assertEquals("SQL error", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void getHeader_returnsCorrectHeaderUsingReflection() throws Exception {
        // Create CSVFormat with header using reflection to set private field
        CSVFormat format = CSVFormat.DEFAULT.withHeader("a", "b", "c");

        // Use reflection to get private header field
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(format);

        assertNotNull(header);
        assertArrayEquals(new String[]{"a", "b", "c"}, header);

        // Also verify getHeader returns the same array
        assertArrayEquals(header, format.getHeader());
    }
}