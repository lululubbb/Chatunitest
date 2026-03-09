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

    private CSVFormat baseFormat;

    @BeforeEach
    void setUp() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void withHeader_nullMetaData_returnsFormatWithNullHeader() throws SQLException {
        CSVFormat result = baseFormat.withHeader((ResultSetMetaData) null);
        assertNotNull(result);
        assertNull(result.getHeader());
        // Other fields remain the same
        assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void withHeader_metaDataWithNoColumns_returnsFormatWithEmptyHeader() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(0);

        // Use reflection to call withHeader(ResultSetMetaData) method
        CSVFormat result = invokeWithHeaderMetaData(baseFormat, metaData);

        assertNotNull(result);
        assertNotNull(result.getHeader());
        assertEquals(0, result.getHeader().length);
    }

    @Test
    @Timeout(8000)
    void withHeader_metaDataWithColumns_returnsFormatWithCorrectHeader() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenReturn("col2");
        when(metaData.getColumnLabel(3)).thenReturn("col3");

        // Use reflection to call withHeader(ResultSetMetaData) method
        CSVFormat result = invokeWithHeaderMetaData(baseFormat, metaData);

        assertNotNull(result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertArrayEquals(new String[] { "col1", "col2", "col3" }, header);
        // Check other fields unchanged
        assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void withHeader_metaDataThrowsSQLException_propagatesException() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenThrow(new SQLException("fail"));

        SQLException thrown = assertThrows(SQLException.class, () -> invokeWithHeaderMetaData(baseFormat, metaData));
        assertEquals("fail", thrown.getMessage());
    }

    private CSVFormat invokeWithHeaderMetaData(CSVFormat format, ResultSetMetaData metaData) throws Exception {
        Method method = CSVFormat.class.getMethod("withHeader", ResultSetMetaData.class);
        return invokeMethod(format, method, metaData);
    }

    private CSVFormat invokeMethod(CSVFormat format, Method method, ResultSetMetaData metaData) throws Exception {
        try {
            return (CSVFormat) method.invoke(format, metaData);
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof SQLException) {
                throw (SQLException) cause;
            }
            throw e;
        }
    }
}