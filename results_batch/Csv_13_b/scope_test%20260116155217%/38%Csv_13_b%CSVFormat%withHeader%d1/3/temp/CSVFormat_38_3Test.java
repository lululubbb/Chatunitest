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

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class CSVFormatWithHeaderTest {

    @Test
    @Timeout(8000)
    void testWithHeader_nullResultSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSet.class);
        withHeaderMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(format, new Object[] { null });

        assertNotNull(result);
        assertNotSame(format, result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_resultSetWithNullMetaData() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenReturn(null);

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSet.class);
        withHeaderMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(format, resultSet);

        assertNotNull(result);
        assertNotSame(format, result);
        assertNull(result.getHeader());
        verify(resultSet).getMetaData();
    }

    @Test
    @Timeout(8000)
    void testWithHeader_resultSetWithMetaData() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenReturn("col2");
        when(metaData.getColumnLabel(3)).thenReturn("col3");

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSet.class);
        withHeaderMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(format, resultSet);

        assertNotNull(result);
        assertNotSame(format, result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertArrayEquals(new String[]{"col1", "col2", "col3"}, header);

        verify(resultSet).getMetaData();
        verify(metaData).getColumnCount();
        verify(metaData, times(3)).getColumnLabel(anyInt());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_resultSetThrowsSQLException() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("meta data error"));

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSet.class);
        withHeaderMethod.setAccessible(true);

        SQLException thrown = assertThrows(SQLException.class, () -> {
            try {
                withHeaderMethod.invoke(format, resultSet);
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof SQLException) {
                    throw (SQLException) cause;
                }
                throw e;
            }
        });
        assertEquals("meta data error", thrown.getMessage());
        verify(resultSet).getMetaData();
    }
}