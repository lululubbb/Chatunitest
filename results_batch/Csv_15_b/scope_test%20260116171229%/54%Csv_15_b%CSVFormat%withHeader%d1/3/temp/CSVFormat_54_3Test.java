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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVFormat_54_3Test {

    private ResultSet resultSetMock;
    private ResultSetMetaData metaDataMock;

    @BeforeEach
    void setUp() {
        resultSetMock = mock(ResultSet.class);
        metaDataMock = mock(ResultSetMetaData.class);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withNullResultSet_returnsWithHeaderNull() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((ResultSet) null);
        CSVFormat expected = format.withHeader((ResultSetMetaData) null);
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withNonNullResultSet_callsGetMetaDataAndReturnsWithHeader() throws SQLException {
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(resultSetMock);
        CSVFormat expected = format.withHeader(metaDataMock);
        assertEquals(expected, result);
        verify(resultSetMock, times(1)).getMetaData();
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withResultSetThrowsSQLException_propagatesException() throws SQLException {
        when(resultSetMock.getMetaData()).thenThrow(new SQLException("test exception"));
        CSVFormat format = CSVFormat.DEFAULT;
        SQLException thrown = assertThrows(SQLException.class, () -> format.withHeader(resultSetMock));
        assertEquals("test exception", thrown.getMessage());
        verify(resultSetMock, times(1)).getMetaData();
    }
}