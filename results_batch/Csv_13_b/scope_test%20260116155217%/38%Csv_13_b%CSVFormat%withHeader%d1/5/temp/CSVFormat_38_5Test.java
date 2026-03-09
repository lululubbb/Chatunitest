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
    void testWithHeader_resultSetNull() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((ResultSet) null);
        // Should call withHeader with null metaData and return a new CSVFormat instance
        assertNotNull(result);
        // The returned CSVFormat should not be the same instance (immutable pattern)
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_resultSetNonNull() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);

        CSVFormat result = format.withHeader(resultSetMock);

        verify(resultSetMock, times(1)).getMetaData();
        assertNotNull(result);
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_resultSetGetMetaDataThrowsSQLException() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        when(resultSetMock.getMetaData()).thenThrow(new SQLException("MetaData failure"));

        SQLException thrown = assertThrows(SQLException.class, () -> format.withHeader(resultSetMock));
        assertEquals("MetaData failure", thrown.getMessage());
        verify(resultSetMock, times(1)).getMetaData();
    }
}