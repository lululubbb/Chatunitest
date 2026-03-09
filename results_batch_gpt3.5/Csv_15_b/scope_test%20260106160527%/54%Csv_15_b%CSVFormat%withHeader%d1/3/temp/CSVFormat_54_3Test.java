package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_54_3Test {

    private ResultSet resultSetMock;
    private ResultSetMetaData metaDataMock;

    @BeforeEach
    public void setUp() {
        resultSetMock = mock(ResultSet.class);
        metaDataMock = mock(ResultSetMetaData.class);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_WithNullResultSet_ReturnsCSVFormatWithNullHeader() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((ResultSet) null);

        // Using reflection to access private 'header' field
        String[] header = getHeaderField(result);

        assertNotNull(result);
        assertNull(header);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_WithResultSet_ReturnsCSVFormatWithHeaderFromMetaData() throws SQLException {
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);

        // Mock metaData to simulate header names
        when(metaDataMock.getColumnCount()).thenReturn(2);
        when(metaDataMock.getColumnLabel(1)).thenReturn("col1");
        when(metaDataMock.getColumnLabel(2)).thenReturn("col2");

        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(resultSetMock);

        assertNotNull(result);
        String[] headers = getHeaderField(result);
        assertNotNull(headers);
        assertEquals(2, headers.length);
        assertEquals("col1", headers[0]);
        assertEquals("col2", headers[1]);

        verify(resultSetMock, times(1)).getMetaData();
        verify(metaDataMock, times(1)).getColumnCount();
        verify(metaDataMock, times(1)).getColumnLabel(1);
        verify(metaDataMock, times(1)).getColumnLabel(2);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_WithResultSet_ThrowsSQLException() throws SQLException {
        when(resultSetMock.getMetaData()).thenThrow(new SQLException("metaData error"));

        CSVFormat format = CSVFormat.DEFAULT;
        assertThrows(SQLException.class, () -> format.withHeader(resultSetMock));
        verify(resultSetMock, times(1)).getMetaData();
    }

    private String[] getHeaderField(CSVFormat format) {
        try {
            Field headerField = CSVFormat.class.getDeclaredField("header");
            headerField.setAccessible(true);
            return (String[]) headerField.get(format);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}