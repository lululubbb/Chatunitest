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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatWithHeaderTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NullMetaData() throws Exception {
        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);

        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(csvFormat, new Object[] { null });
        assertNotNull(result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyMetaData() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(0);

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);

        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(csvFormat, metaData);
        assertNotNull(result);
        assertArrayEquals(new String[0], result.getHeader());

        verify(metaData).getColumnCount();
        verifyNoMoreInteractions(metaData);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MultipleColumns() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenReturn("col2");
        when(metaData.getColumnLabel(3)).thenReturn("col3");

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);

        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(csvFormat, metaData);
        assertNotNull(result);
        assertArrayEquals(new String[] { "col1", "col2", "col3" }, result.getHeader());

        verify(metaData).getColumnCount();
        verify(metaData).getColumnLabel(1);
        verify(metaData).getColumnLabel(2);
        verify(metaData).getColumnLabel(3);
        verifyNoMoreInteractions(metaData);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MetaDataThrowsSQLException() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenThrow(new SQLException("Test exception"));

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);

        SQLException thrown = assertThrows(SQLException.class, () -> {
            try {
                withHeaderMethod.invoke(csvFormat, metaData);
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof SQLException) {
                    throw (SQLException) cause;
                }
                throw e;
            }
        });
        assertEquals("Test exception", thrown.getMessage());

        verify(metaData).getColumnCount();
        verifyNoMoreInteractions(metaData);
    }
}