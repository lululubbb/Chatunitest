package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVFormat_52_2Test {

    @Test
    @Timeout(8000)
    void testWithHeader_withNullResultSet() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((ResultSet) null);
        assertNotNull(result);
        // The header should be null because input is null
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withResultSet_callsMetaData() throws SQLException, NoSuchFieldException, IllegalAccessException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);

        // Mock metaData to return column count and column labels for header extraction
        when(metaData.getColumnCount()).thenReturn(2);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenReturn("col2");

        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(resultSet);

        verify(resultSet, times(1)).getMetaData();
        assertNotNull(result);

        // Use the public getter to verify the header instead of reflection
        String[] header = result.getHeader();
        // The header should not be null because metaData is not null and has columns
        assertNotNull(header);
        assertArrayEquals(new String[]{"col1", "col2"}, header);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withResultSet_nullMetaData() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getMetaData()).thenReturn(null);

        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(resultSet);

        verify(resultSet, times(1)).getMetaData();
        // When metaData is null, header should be null
        assertNotNull(result);
        assertNull(result.getHeader());
    }
}