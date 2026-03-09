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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
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
    void testWithHeader_NullResultSet() throws Exception {
        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", ResultSet.class);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(csvFormat, new Object[] { null });
        assertNotNull(result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_ResultSetWithMetaData() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenReturn("col2");
        when(metaData.getColumnLabel(3)).thenReturn("col3");

        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", ResultSet.class);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(csvFormat, resultSet);

        assertNotNull(result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertEquals(3, header.length);
        assertEquals("col1", header[0]);
        assertEquals("col2", header[1]);
        assertEquals("col3", header[2]);

        verify(resultSet, times(1)).getMetaData();
        verify(metaData, times(1)).getColumnCount();
        verify(metaData, times(1)).getColumnLabel(1);
        verify(metaData, times(1)).getColumnLabel(2);
        verify(metaData, times(1)).getColumnLabel(3);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_ResultSetGetMetaDataThrows() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("MetaData error"));

        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", ResultSet.class);

        SQLException thrown = assertThrows(SQLException.class, () -> {
            try {
                withHeaderMethod.invoke(csvFormat, resultSet);
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

        verify(resultSet, times(1)).getMetaData();
    }

    @Test
    @Timeout(8000)
    void testWithHeader_ResultSetMetaDataWithZeroColumns() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(0);

        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", ResultSet.class);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(csvFormat, resultSet);

        assertNotNull(result);
        assertNotNull(result.getHeader());
        assertEquals(0, result.getHeader().length);

        verify(metaData, times(1)).getColumnCount();
    }

}