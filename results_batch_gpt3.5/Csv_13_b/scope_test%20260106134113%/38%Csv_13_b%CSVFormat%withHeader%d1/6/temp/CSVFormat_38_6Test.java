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

public class CSVFormat_38_6Test {

    private ResultSet resultSetMock;
    private ResultSetMetaData metaDataMock;

    @BeforeEach
    public void setUp() {
        resultSetMock = mock(ResultSet.class);
        metaDataMock = mock(ResultSetMetaData.class);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_NullResultSet() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((ResultSet) null);
        // when ResultSet is null, withHeader(ResultSetMetaData) is called with null
        CSVFormat expected = format.withHeader((ResultSetMetaData) null);
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_ValidResultSet() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);

        CSVFormat result = format.withHeader(resultSetMock);

        CSVFormat expected = format.withHeader(metaDataMock);
        assertEquals(expected, result);

        verify(resultSetMock, times(1)).getMetaData();
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_ResultSetThrowsSQLException() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        when(resultSetMock.getMetaData()).thenThrow(new SQLException("Test Exception"));

        assertThrows(SQLException.class, () -> format.withHeader(resultSetMock));

        verify(resultSetMock, times(1)).getMetaData();
    }
}