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
import static org.mockito.ArgumentMatchers.isNull;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatWithHeaderTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NullResultSet() throws SQLException {
        CSVFormat result = csvFormat.withHeader((ResultSet) null);
        assertNotNull(result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_ResultSetWithMetaData() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);

        // Mock metaData to simulate header names
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenReturn("col2");
        when(metaData.getColumnLabel(3)).thenReturn("col3");

        // Spy on CSVFormat to intercept withHeader(ResultSetMetaData)
        CSVFormat spyFormat = spy(csvFormat);

        // We want to call real method withHeader(ResultSetMetaData)
        doCallRealMethod().when(spyFormat).withHeader(metaData);

        CSVFormat result = spyFormat.withHeader(resultSet);

        assertNotNull(result);
        assertArrayEquals(new String[] { "col1", "col2", "col3" }, result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_ResultSetThrowsSQLException() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("MetaData error"));

        SQLException thrown = assertThrows(SQLException.class, () -> csvFormat.withHeader(resultSet));
        assertEquals("MetaData error", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_CallsWithHeaderMetaDataWithNullWhenResultSetIsNull() throws SQLException {
        CSVFormat spyFormat = spy(csvFormat);
        doReturn(spyFormat).when(spyFormat).withHeader((ResultSetMetaData) isNull());

        CSVFormat result = spyFormat.withHeader((ResultSet) null);

        verify(spyFormat, times(1)).withHeader((ResultSetMetaData) isNull());
        assertSame(spyFormat, result);
    }
}