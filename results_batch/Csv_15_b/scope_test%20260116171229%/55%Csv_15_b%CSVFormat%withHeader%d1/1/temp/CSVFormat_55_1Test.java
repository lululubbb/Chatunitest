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
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVFormatWithHeaderTest {

    private ResultSetMetaData metaDataMock;

    @BeforeEach
    void setUp() {
        metaDataMock = mock(ResultSetMetaData.class);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NullMetaData() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((ResultSetMetaData) null);
        assertNotNull(result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyMetaData() throws SQLException {
        when(metaDataMock.getColumnCount()).thenReturn(0);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(metaDataMock);
        assertNotNull(result);
        assertArrayEquals(new String[0], result.getHeader());
        verify(metaDataMock, times(1)).getColumnCount();
        verifyNoMoreInteractions(metaDataMock);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MetaDataWithColumns() throws SQLException {
        when(metaDataMock.getColumnCount()).thenReturn(3);
        when(metaDataMock.getColumnLabel(1)).thenReturn("col1");
        when(metaDataMock.getColumnLabel(2)).thenReturn("col2");
        when(metaDataMock.getColumnLabel(3)).thenReturn("col3");

        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(metaDataMock);

        assertNotNull(result);
        String[] expectedHeaders = {"col1", "col2", "col3"};
        assertArrayEquals(expectedHeaders, result.getHeader());

        verify(metaDataMock, times(1)).getColumnCount();
        verify(metaDataMock, times(1)).getColumnLabel(1);
        verify(metaDataMock, times(1)).getColumnLabel(2);
        verify(metaDataMock, times(1)).getColumnLabel(3);
        verifyNoMoreInteractions(metaDataMock);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MetaDataWithNullColumnLabel() throws SQLException {
        when(metaDataMock.getColumnCount()).thenReturn(2);
        when(metaDataMock.getColumnLabel(1)).thenReturn(null);
        when(metaDataMock.getColumnLabel(2)).thenReturn("label2");

        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(metaDataMock);

        assertNotNull(result);
        String[] expectedHeaders = {null, "label2"};
        assertArrayEquals(expectedHeaders, result.getHeader());

        verify(metaDataMock, times(1)).getColumnCount();
        verify(metaDataMock, times(1)).getColumnLabel(1);
        verify(metaDataMock, times(1)).getColumnLabel(2);
        verifyNoMoreInteractions(metaDataMock);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_ThrowsSQLException() throws SQLException {
        when(metaDataMock.getColumnCount()).thenThrow(new SQLException("DB error"));

        CSVFormat format = CSVFormat.DEFAULT;
        SQLException exception = assertThrows(SQLException.class, () -> format.withHeader(metaDataMock));
        assertEquals("DB error", exception.getMessage());

        verify(metaDataMock, times(1)).getColumnCount();
        verifyNoMoreInteractions(metaDataMock);
    }
}