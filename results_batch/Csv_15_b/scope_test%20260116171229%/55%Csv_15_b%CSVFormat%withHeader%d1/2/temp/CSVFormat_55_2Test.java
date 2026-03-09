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
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVFormatWithHeaderTest {

    private ResultSetMetaData metaDataMock;

    @BeforeEach
    void setUp() {
        metaDataMock = mock(ResultSetMetaData.class);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NullMetaData() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(baseFormat, new Object[]{null});
        assertNotNull(result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyMetaData() throws Exception {
        when(metaDataMock.getColumnCount()).thenReturn(0);
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(baseFormat, metaDataMock);
        assertNotNull(result);
        assertArrayEquals(new String[0], result.getHeader());
        verify(metaDataMock, times(1)).getColumnCount();
        verifyNoMoreInteractions(metaDataMock);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MultipleColumns() throws Exception {
        when(metaDataMock.getColumnCount()).thenReturn(3);
        when(metaDataMock.getColumnLabel(1)).thenReturn("id");
        when(metaDataMock.getColumnLabel(2)).thenReturn("name");
        when(metaDataMock.getColumnLabel(3)).thenReturn("email");

        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(baseFormat, metaDataMock);

        assertNotNull(result);
        String[] expected = {"id", "name", "email"};
        assertArrayEquals(expected, result.getHeader());

        verify(metaDataMock, times(1)).getColumnCount();
        verify(metaDataMock, times(1)).getColumnLabel(1);
        verify(metaDataMock, times(1)).getColumnLabel(2);
        verify(metaDataMock, times(1)).getColumnLabel(3);
        verifyNoMoreInteractions(metaDataMock);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_ThrowsSQLException() throws Exception {
        when(metaDataMock.getColumnCount()).thenThrow(new SQLException("Test exception"));

        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);
        SQLException thrown = assertThrows(SQLException.class, () -> {
            try {
                withHeaderMethod.invoke(baseFormat, metaDataMock);
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof SQLException) {
                    throw (SQLException) cause;
                }
                throw e;
            }
        });
        assertEquals("Test exception", thrown.getMessage());

        verify(metaDataMock, times(1)).getColumnCount();
        verifyNoMoreInteractions(metaDataMock);
    }
}