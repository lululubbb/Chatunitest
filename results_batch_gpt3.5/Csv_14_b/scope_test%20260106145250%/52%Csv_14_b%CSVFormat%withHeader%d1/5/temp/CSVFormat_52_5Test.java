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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_52_5Test {

    private ResultSet resultSetMock;
    private ResultSetMetaData metaDataMock;

    @BeforeEach
    public void setUp() {
        resultSetMock = mock(ResultSet.class);
        metaDataMock = mock(ResultSetMetaData.class);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_WithNullResultSet_ReturnsWithHeaderNull() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat result = format.withHeader((ResultSet) null);

        // Using reflection to invoke withHeader(ResultSetMetaData) for comparison
        CSVFormat expected = invokeWithHeaderMetaData(format, null);

        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_WithResultSet_ReturnsWithHeaderMetaData() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;

        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);

        CSVFormat result = format.withHeader(resultSetMock);

        CSVFormat expected = invokeWithHeaderMetaData(format, metaDataMock);

        assertEquals(expected, result);

        verify(resultSetMock).getMetaData();
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_WithResultSet_ThrowsSQLException() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;

        when(resultSetMock.getMetaData()).thenThrow(new SQLException("metaData exception"));

        SQLException thrown = assertThrows(SQLException.class, () -> {
            format.withHeader(resultSetMock);
        });

        assertEquals("metaData exception", thrown.getMessage());

        verify(resultSetMock).getMetaData();
    }

    private CSVFormat invokeWithHeaderMetaData(CSVFormat format, ResultSetMetaData metaData) {
        try {
            Method method = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
            method.setAccessible(true);
            return (CSVFormat) method.invoke(format, metaData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}