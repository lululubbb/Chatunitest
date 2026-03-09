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
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
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
    void testWithHeader_resultSetNull() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((ResultSet) null);
        assertNotNull(result);
        CSVFormat expected = format.withHeader((ResultSetMetaData) null);
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_resultSetNotNull() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);

        CSVFormat result = format.withHeader(resultSetMock);

        verify(resultSetMock, times(1)).getMetaData();
        CSVFormat expected = format.withHeader(metaDataMock);
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_resultSetThrowsSQLException() {
        CSVFormat format = CSVFormat.DEFAULT;
        try {
            when(resultSetMock.getMetaData()).thenThrow(new SQLException("MetaData error"));
            format.withHeader(resultSetMock);
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            assertEquals("MetaData error", e.getMessage());
        }
    }

}