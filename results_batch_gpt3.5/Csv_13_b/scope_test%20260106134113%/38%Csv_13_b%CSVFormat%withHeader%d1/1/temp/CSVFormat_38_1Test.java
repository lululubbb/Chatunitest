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
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader((ResultSet) null);
        CSVFormat expected = baseFormat.withHeader((ResultSetMetaData) null);
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withResultSet_callsGetMetaDataAndReturnsWithHeader() throws SQLException {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);
        CSVFormat expected = baseFormat.withHeader(metaDataMock);
        CSVFormat result = baseFormat.withHeader(resultSetMock);
        verify(resultSetMock).getMetaData();
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withResultSetThrowsSQLException_propagatesException() throws SQLException {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        when(resultSetMock.getMetaData()).thenThrow(new SQLException("metaData failure"));
        SQLException thrown = assertThrows(SQLException.class, () -> baseFormat.withHeader(resultSetMock));
        assertEquals("metaData failure", thrown.getMessage());
    }
}