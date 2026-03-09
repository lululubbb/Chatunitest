package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

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
    void withHeader_resultSetIsNull_shouldCallWithHeaderWithNull() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat formatSpy = spy(format);

        // Use doReturn to avoid calling real method with null argument which may cause NPE
        doReturn(format).when(formatSpy).withHeader((ResultSetMetaData) isNull());

        CSVFormat result = formatSpy.withHeader((ResultSet) null);

        verify(formatSpy).withHeader((ResultSetMetaData) isNull());
        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void withHeader_resultSetNotNull_shouldCallWithHeaderWithMetaData() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat formatSpy = spy(format);

        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);
        // Stub the withHeader(metaData) method to return 'format' to avoid real call
        doReturn(format).when(formatSpy).withHeader(metaDataMock);

        CSVFormat result = formatSpy.withHeader(resultSetMock);

        verify(resultSetMock).getMetaData();
        verify(formatSpy).withHeader(metaDataMock);
        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void withHeader_resultSetGetMetaDataThrowsSQLException_shouldThrowSQLException() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        when(resultSetMock.getMetaData()).thenThrow(new SQLException("MetaData error"));

        assertThrows(SQLException.class, () -> format.withHeader(resultSetMock));
    }
}