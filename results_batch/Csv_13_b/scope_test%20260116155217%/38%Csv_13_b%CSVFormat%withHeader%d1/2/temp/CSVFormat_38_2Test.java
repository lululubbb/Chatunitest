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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatWithHeaderTest {

    private CSVFormat csvFormatDefault;

    @BeforeEach
    void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withNullResultSet_returnsWithHeaderNull() throws SQLException {
        CSVFormat result = csvFormatDefault.withHeader((ResultSet) null);
        assertNotNull(result);
        String[] header = getHeaderArray(result);
        assertNull(header);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withResultSet_callsWithHeaderWithMetaData() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(metaData);

        CSVFormat spyFormatWithMock = spy(csvFormatDefault);
        CSVFormat expectedFormat = csvFormatDefault.withHeader("a", "b");
        doReturn(expectedFormat).when(spyFormatWithMock).withHeader(metaData);

        CSVFormat result = spyFormatWithMock.withHeader(resultSet);

        verify(spyFormatWithMock).withHeader(metaData);
        assertSame(expectedFormat, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withResultSetThrowsSQLException_propagatesException() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("MetaData error"));

        SQLException thrown = assertThrows(SQLException.class, () -> csvFormatDefault.withHeader(resultSet));
        assertEquals("MetaData error", thrown.getMessage());
    }

    private String[] getHeaderArray(CSVFormat format) {
        try {
            java.lang.reflect.Field headerField = CSVFormat.class.getDeclaredField("header");
            headerField.setAccessible(true);
            return (String[]) headerField.get(format);
        } catch (ReflectiveOperationException e) {
            fail("Reflection error: " + e.getMessage());
            return null;
        }
    }
}