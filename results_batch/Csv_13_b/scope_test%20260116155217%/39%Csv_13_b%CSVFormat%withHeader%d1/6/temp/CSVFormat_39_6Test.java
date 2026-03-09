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

import org.junit.jupiter.api.Test;

class CSVFormatWithHeaderTest {

    @Test
    @Timeout(8000)
    void testWithHeader_NullMetaData() throws SQLException, NoSuchFieldException, IllegalAccessException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((ResultSetMetaData) null);
        assertNotNull(result);
        assertNull(result.getHeader());

        // Use reflection to verify original format header is still null (private final field)
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        assertNull(headerField.get(format));
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyMetaData() throws SQLException, NoSuchFieldException, IllegalAccessException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(0);

        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(metaData);

        assertNotNull(result);
        assertNotNull(result.getHeader());
        assertEquals(0, result.getHeader().length);

        // Original format header remains null
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        assertNull(headerField.get(format));
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MultipleColumns() throws SQLException, NoSuchFieldException, IllegalAccessException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("id");
        when(metaData.getColumnLabel(2)).thenReturn("name");
        when(metaData.getColumnLabel(3)).thenReturn("email");

        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(metaData);

        assertNotNull(result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertEquals(3, header.length);
        assertEquals("id", header[0]);
        assertEquals("name", header[1]);
        assertEquals("email", header[2]);

        // Original format header remains null
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        assertNull(headerField.get(format));
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MetaDataThrowsSQLException() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenThrow(new SQLException("Test exception"));

        CSVFormat format = CSVFormat.DEFAULT;

        SQLException thrown = assertThrows(SQLException.class, () -> format.withHeader(metaData));
        assertEquals("Test exception", thrown.getMessage());
    }
}