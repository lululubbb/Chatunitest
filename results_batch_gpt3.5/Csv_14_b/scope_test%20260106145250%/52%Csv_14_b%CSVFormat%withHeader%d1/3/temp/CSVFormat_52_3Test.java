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
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CSVFormat_52_3Test {

    @Test
    @Timeout(8000)
    public void testWithHeader_ResultSetNull() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((ResultSet) null);
        assertNotNull(result);
        // When resultSet is null, withHeader(ResultSetMetaData) is called with null
        // So header should be null in resulting CSVFormat
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_ResultSetNotNull() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);

        // Mock metaData to simulate header names
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenReturn("col2");
        when(metaData.getColumnLabel(3)).thenReturn("col3");

        CSVFormat format = CSVFormat.DEFAULT;

        // Spy on format to intercept withHeader(ResultSetMetaData)
        CSVFormat spyFormat = Mockito.spy(format);

        // Use doAnswer to call real method for withHeader(ResultSetMetaData)
        doAnswer((InvocationOnMock invocation) -> {
            ResultSetMetaData md = invocation.getArgument(0);
            if (md == null) {
                // Return a CSVFormat with null header
                return format.withHeader((String[]) null);
            }
            // Build header array from metaData
            int count = md.getColumnCount();
            String[] headers = new String[count];
            for (int i = 1; i <= count; i++) {
                headers[i - 1] = md.getColumnLabel(i);
            }
            return format.withHeader(headers);
        }).when(spyFormat).withHeader(any(ResultSetMetaData.class));

        // Call real method for withHeader(ResultSet)
        CSVFormat result = spyFormat.withHeader(resultSet);

        // Verify that withHeader(ResultSetMetaData) is called with metaData
        verify(spyFormat).withHeader(metaData);

        // The returned CSVFormat should have header with "col1","col2","col3"
        assertNotNull(result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertArrayEquals(new String[]{"col1", "col2", "col3"}, header);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_ResultSetGetMetaDataThrows() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("MetaData error"));

        CSVFormat format = CSVFormat.DEFAULT;

        SQLException thrown = assertThrows(SQLException.class, () -> format.withHeader(resultSet));
        assertEquals("MetaData error", thrown.getMessage());
    }
}