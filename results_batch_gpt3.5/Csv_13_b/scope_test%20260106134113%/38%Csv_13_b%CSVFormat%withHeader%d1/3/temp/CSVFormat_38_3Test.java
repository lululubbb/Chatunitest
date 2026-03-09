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
import org.mockito.Mockito;

class CSVFormatWithHeaderTest {

    private ResultSet resultSetMock;
    private ResultSetMetaData metaDataMock;

    @BeforeEach
    public void setup() {
        resultSetMock = mock(ResultSet.class);
        metaDataMock = mock(ResultSetMetaData.class);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_withNonNullResultSet_shouldCallWithHeaderMetaData() throws SQLException {
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);

        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat spyFormat = Mockito.spy(csvFormat);

        CSVFormat expectedFormat = CSVFormat.DEFAULT.withHeader("a", "b");
        doReturn(expectedFormat).when(spyFormat).withHeader(metaDataMock);

        CSVFormat result = spyFormat.withHeader(resultSetMock);

        verify(resultSetMock).getMetaData();
        verify(spyFormat).withHeader(metaDataMock);
        assertSame(expectedFormat, result);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_withNullResultSet_shouldCallWithHeaderNull() throws SQLException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat spyFormat = Mockito.spy(csvFormat);

        CSVFormat expectedFormat = CSVFormat.DEFAULT.withHeader((ResultSetMetaData) null);
        doReturn(expectedFormat).when(spyFormat).withHeader((ResultSetMetaData) isNull());

        CSVFormat result = spyFormat.withHeader((ResultSet) null);

        verify(spyFormat).withHeader((ResultSetMetaData) isNull());
        assertSame(expectedFormat, result);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_withResultSetThrowsSQLException_shouldThrow() throws SQLException {
        when(resultSetMock.getMetaData()).thenThrow(new SQLException("MetaData error"));

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        SQLException thrown = assertThrows(SQLException.class, () -> {
            csvFormat.withHeader(resultSetMock);
        });

        assertEquals("MetaData error", thrown.getMessage());
    }
}