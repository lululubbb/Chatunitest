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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVFormat_54_6Test {

    @Test
    @Timeout(8000)
    void testWithHeader_withNullResultSet_returnsWithHeaderNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Call withHeader(ResultSet) via reflection to avoid compile-time error if method is not visible
        Method withHeaderRs = CSVFormat.class.getDeclaredMethod("withHeader", ResultSet.class);
        withHeaderRs.setAccessible(true);
        CSVFormat result = (CSVFormat) withHeaderRs.invoke(format, (ResultSet) null);

        CSVFormat expected = format.withHeader((ResultSetMetaData) null);
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withResultSet_callsGetMetaDataAndReturnsWithHeader() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(metaData);

        CSVFormat format = CSVFormat.DEFAULT;

        // Call withHeader(ResultSet) via reflection
        Method withHeaderRs = CSVFormat.class.getDeclaredMethod("withHeader", ResultSet.class);
        withHeaderRs.setAccessible(true);
        CSVFormat result = (CSVFormat) withHeaderRs.invoke(format, resultSet);

        CSVFormat expected = format.withHeader(metaData);

        verify(resultSet, times(1)).getMetaData();
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withResultSet_throwsSQLException() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("MetaData exception"));

        CSVFormat format = CSVFormat.DEFAULT;

        Method withHeaderRs = CSVFormat.class.getDeclaredMethod("withHeader", ResultSet.class);
        withHeaderRs.setAccessible(true);

        SQLException thrown = assertThrows(SQLException.class, () -> {
            try {
                withHeaderRs.invoke(format, resultSet);
            } catch (java.lang.reflect.InvocationTargetException e) {
                // unwrap the SQLException thrown inside the invoked method
                Throwable cause = e.getCause();
                if (cause instanceof SQLException) {
                    throw (SQLException) cause;
                }
                throw e;
            }
        });
        assertEquals("MetaData exception", thrown.getMessage());
    }
}