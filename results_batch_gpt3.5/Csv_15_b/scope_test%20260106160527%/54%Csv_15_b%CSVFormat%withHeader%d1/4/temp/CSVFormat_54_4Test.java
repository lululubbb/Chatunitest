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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_54_4Test {

    private ResultSet resultSetMock;
    private ResultSetMetaData metaDataMock;

    @BeforeEach
    void setUp() {
        resultSetMock = mock(ResultSet.class);
        metaDataMock = mock(ResultSetMetaData.class);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withNonNullResultSet() throws Exception {
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Use reflection to invoke withHeader(ResultSet) because it's public but we want to test it via reflection
        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", ResultSet.class);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(baseFormat, resultSetMock);

        assertNotNull(result);
        verify(resultSetMock).getMetaData();
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withNullResultSet() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", ResultSet.class);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(baseFormat, (Object) null);

        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withMetaDataThrowsSQLException() throws Exception {
        when(resultSetMock.getMetaData()).thenThrow(new SQLException("MetaData error"));

        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", ResultSet.class);

        SQLException thrown = assertThrows(SQLException.class, () -> {
            try {
                withHeaderMethod.invoke(baseFormat, resultSetMock);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof SQLException) {
                    throw (SQLException) cause;
                } else {
                    throw e;
                }
            }
        });

        assertEquals("MetaData error", thrown.getMessage());
    }
}