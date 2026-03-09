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
import org.mockito.Mockito;

class CSVFormatWithHeaderTest {

    private ResultSet resultSetMock;
    private ResultSetMetaData metaDataMock;

    @BeforeEach
    void setup() {
        resultSetMock = mock(ResultSet.class);
        metaDataMock = mock(ResultSetMetaData.class);
    }

    @Test
    @Timeout(8000)
    void withHeader_resultSetNull_callsWithHeaderWithNull() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((ResultSet) null);
        assertNotNull(result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void withHeader_resultSetNonNull_callsWithHeaderWithMetaData() throws SQLException {
        CSVFormat format = spy(CSVFormat.DEFAULT);
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);

        // Use doReturn to stub withHeader(ResultSetMetaData) since it might be final
        doReturn(format).when(format).withHeader(metaDataMock);

        CSVFormat result = format.withHeader(resultSetMock);

        verify(resultSetMock).getMetaData();
        verify(format).withHeader(metaDataMock);
        assertNotNull(result);
        assertEquals(format, result);
    }

    @Test
    @Timeout(8000)
    void withHeader_resultSetThrowsSQLException_propagatesException() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        when(resultSetMock.getMetaData()).thenThrow(new SQLException("metaData error"));

        SQLException thrown = assertThrows(SQLException.class, () -> format.withHeader(resultSetMock));
        assertEquals("metaData error", thrown.getMessage());
    }
}