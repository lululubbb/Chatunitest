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

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class CSVFormat_54_2Test {

    @Test
    @Timeout(8000)
    void testWithHeader_NullResultSet() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((ResultSet) null);
        assertNotNull(result);
        // When ResultSet is null, withHeader(ResultSetMetaData) is called with null
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_ValidResultSet() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);

        // Mock metaData to simulate columns
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenReturn("col2");
        when(metaData.getColumnLabel(3)).thenReturn("col3");

        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(resultSet);

        assertNotNull(result);

        // Use reflection to access the private header field because getHeader() may not return expected value
        String[] header = getHeaderField(result);
        assertArrayEquals(new String[] { "col1", "col2", "col3" }, header);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_ResultSetThrowsSQLException() {
        ResultSet resultSet = mock(ResultSet.class);
        try {
            when(resultSet.getMetaData()).thenThrow(new SQLException("MetaData error"));
        } catch (SQLException e) {
            fail("Mock setup failed");
        }

        CSVFormat format = CSVFormat.DEFAULT;

        SQLException thrown = assertThrows(SQLException.class, () -> {
            format.withHeader(resultSet);
        });
        assertEquals("MetaData error", thrown.getMessage());
    }

    private String[] getHeaderField(CSVFormat format) {
        try {
            Field headerField = CSVFormat.class.getDeclaredField("header");
            headerField.setAccessible(true);
            return (String[]) headerField.get(format);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed to access header field");
            return null; // unreachable
        }
    }
}