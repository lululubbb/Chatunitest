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

class CSVFormat_39_1Test {

    private CSVFormat baseFormat;

    @BeforeEach
    void setUp() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithHeader_nullMetaData_returnsFormatWithNullHeader() throws Exception {
        // Use reflection to invoke withHeader(ResultSetMetaData) because of possible compile ambiguity
        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", ResultSetMetaData.class);
        CSVFormat formatWithHeader = (CSVFormat) withHeaderMethod.invoke(baseFormat, (Object) null);

        assertNotNull(formatWithHeader);
        assertNull(formatWithHeader.getHeader());
        // Confirm original format header is unchanged
        assertNull(baseFormat.getHeader());
        assertNotSame(baseFormat, formatWithHeader);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_metaDataWithNoColumns_returnsFormatWithEmptyHeader() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(0);

        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", ResultSetMetaData.class);
        CSVFormat formatWithHeader = (CSVFormat) withHeaderMethod.invoke(baseFormat, (Object) metaData);

        assertNotNull(formatWithHeader);
        assertNotNull(formatWithHeader.getHeader());
        assertEquals(0, formatWithHeader.getHeader().length);
        assertNotSame(baseFormat, formatWithHeader);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_metaDataWithColumns_returnsFormatWithHeaderLabels() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("id");
        when(metaData.getColumnLabel(2)).thenReturn("name");
        when(metaData.getColumnLabel(3)).thenReturn("email");

        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", ResultSetMetaData.class);
        CSVFormat formatWithHeader = (CSVFormat) withHeaderMethod.invoke(baseFormat, (Object) metaData);

        assertNotNull(formatWithHeader);
        String[] header = formatWithHeader.getHeader();
        assertNotNull(header);
        assertEquals(3, header.length);
        assertArrayEquals(new String[] { "id", "name", "email" }, header);
        assertNotSame(baseFormat, formatWithHeader);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_metaDataThrowsSQLException_propagatesException() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenThrow(new SQLException("error"));

        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", ResultSetMetaData.class);

        SQLException thrown = assertThrows(SQLException.class, () -> {
            try {
                withHeaderMethod.invoke(baseFormat, (Object) metaData);
            } catch (Exception e) {
                // Unwrap the InvocationTargetException to throw the cause (SQLException)
                Throwable cause = e.getCause();
                if (cause instanceof SQLException) {
                    throw (SQLException) cause;
                }
                throw e;
            }
        });
        assertEquals("error", thrown.getMessage());
    }
}