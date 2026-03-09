package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVFormatWithHeaderTest {

    @Test
    @Timeout(8000)
    void testWithHeaderWithNullMetaData() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((ResultSetMetaData) null);
        assertNotNull(result);
        assertNotSame(format, result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithEmptyMetaData() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(0);

        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(metaData);

        assertNotNull(result);
        assertNotSame(format, result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertEquals(0, header.length);

        verify(metaData).getColumnCount();
        verifyNoMoreInteractions(metaData);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithMultipleColumns() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("id");
        when(metaData.getColumnLabel(2)).thenReturn("name");
        when(metaData.getColumnLabel(3)).thenReturn("email");

        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(metaData);

        assertNotNull(result);
        assertNotSame(format, result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertArrayEquals(new String[]{"id", "name", "email"}, header);

        verify(metaData).getColumnCount();
        verify(metaData).getColumnLabel(1);
        verify(metaData).getColumnLabel(2);
        verify(metaData).getColumnLabel(3);
        verifyNoMoreInteractions(metaData);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderThrowsSQLException() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(2);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenThrow(new SQLException("SQL error"));

        CSVFormat format = CSVFormat.DEFAULT;

        SQLException thrown = assertThrows(SQLException.class, () -> format.withHeader(metaData));
        assertEquals("SQL error", thrown.getMessage());

        verify(metaData).getColumnCount();
        verify(metaData).getColumnLabel(1);
        verify(metaData).getColumnLabel(2);
        verifyNoMoreInteractions(metaData);
    }

    // Additional test to verify immutability and header field correctness using reflection
    @Test
    @Timeout(8000)
    void testHeaderFieldIsSetCorrectly() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(2);
        when(metaData.getColumnLabel(1)).thenReturn("a");
        when(metaData.getColumnLabel(2)).thenReturn("b");

        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(metaData);

        // Use reflection to access private final field 'header'
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] headerValue = (String[]) headerField.get(result);

        assertArrayEquals(new String[]{"a", "b"}, headerValue);
    }
}