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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_38_1Test {

    private ResultSet resultSetMock;
    private ResultSetMetaData metaDataMock;

    @BeforeEach
    public void setUp() {
        resultSetMock = mock(ResultSet.class);
        metaDataMock = mock(ResultSetMetaData.class);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_withNonNullResultSet() throws SQLException {
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);
        // Mock metaDataMock.getColumnCount and getColumnName to avoid exceptions
        when(metaDataMock.getColumnCount()).thenReturn(1);
        when(metaDataMock.getColumnName(1)).thenReturn("header1");

        CSVFormat originalFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = originalFormat.withHeader(resultSetMock);

        verify(resultSetMock, times(1)).getMetaData();
        assertNotNull(newFormat);
        assertNotSame(originalFormat, newFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_withNullResultSet() throws SQLException {
        CSVFormat originalFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = originalFormat.withHeader((ResultSet) null);

        assertNotNull(newFormat);
        assertNotSame(originalFormat, newFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_withResultSet_getMetaDataThrows() throws SQLException {
        when(resultSetMock.getMetaData()).thenThrow(new SQLException("metaData error"));

        CSVFormat originalFormat = CSVFormat.DEFAULT;
        SQLException thrown = assertThrows(SQLException.class, () -> originalFormat.withHeader(resultSetMock));
        assertEquals("metaData error", thrown.getMessage());

        verify(resultSetMock, times(1)).getMetaData();
    }
}