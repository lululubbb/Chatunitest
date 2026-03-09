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
import java.lang.reflect.InvocationTargetException;
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
    void testWithHeaderWithNonNullResultSet() throws Exception {
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to call withHeader(ResultSet) to avoid compile ambiguity
        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", ResultSet.class);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(format, resultSetMock);

        assertNotNull(result);
        verify(resultSetMock, times(1)).getMetaData();
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithNullResultSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to call withHeader(ResultSet) with null
        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", ResultSet.class);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(format, new Object[] { null });

        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithResultSetThrowsSQLException() throws Exception {
        when(resultSetMock.getMetaData()).thenThrow(new SQLException("MetaData error"));
        CSVFormat format = CSVFormat.DEFAULT;

        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", ResultSet.class);

        SQLException thrown = assertThrows(SQLException.class, () -> {
            try {
                withHeaderMethod.invoke(format, resultSetMock);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof SQLException) {
                    throw (SQLException) cause;
                }
                throw e;
            }
        });

        assertEquals("MetaData error", thrown.getMessage());
        verify(resultSetMock, times(1)).getMetaData();
    }
}