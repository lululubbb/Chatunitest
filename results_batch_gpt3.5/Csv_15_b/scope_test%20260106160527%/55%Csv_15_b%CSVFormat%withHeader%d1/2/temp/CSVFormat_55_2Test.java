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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVFormat_55_2Test {

    @Test
    @Timeout(8000)
    void testWithHeader_NullMetaData() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to call withHeader(ResultSetMetaData) to avoid compile issues if method is package-private
        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);

        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(format, (Object) null);

        assertNotNull(result);
        assertNotSame(format, result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyMetaData() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(0);

        CSVFormat format = CSVFormat.DEFAULT;

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);

        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(format, metaData);

        assertNotNull(result);
        assertNotSame(format, result);
        assertNotNull(result.getHeader());
        assertEquals(0, result.getHeader().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MultipleColumns() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenReturn("col2");
        when(metaData.getColumnLabel(3)).thenReturn("col3");

        CSVFormat format = CSVFormat.DEFAULT;

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);

        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(format, metaData);

        assertNotNull(result);
        assertNotSame(format, result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertArrayEquals(new String[]{"col1", "col2", "col3"}, header);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MetaDataThrowsSQLException() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenThrow(new SQLException("fail"));

        CSVFormat format = CSVFormat.DEFAULT;

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);

        SQLException thrown = assertThrows(SQLException.class, () -> {
            try {
                withHeaderMethod.invoke(format, metaData);
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof SQLException) {
                    throw (SQLException) cause;
                }
                throw e;
            }
        });
        assertEquals("fail", thrown.getMessage());
    }
}