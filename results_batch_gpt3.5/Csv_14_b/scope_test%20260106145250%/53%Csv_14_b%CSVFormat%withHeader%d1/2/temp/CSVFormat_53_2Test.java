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
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CSVFormat_53_2Test {

    @Test
    @Timeout(8000)
    public void testWithHeader_NullMetaData() throws SQLException {
        CSVFormat format = CSVFormat.DEFAULT;
        // Explicitly cast null to ResultSetMetaData to resolve ambiguity
        CSVFormat result = format.withHeader((ResultSetMetaData) null);
        assertNotNull(result);
        assertNotSame(format, result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_EmptyMetaData() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(0);

        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(metaData);

        assertNotNull(result);
        assertNotSame(format, result);
        assertNotNull(result.getHeader());
        assertEquals(0, result.getHeader().length);

        verify(metaData, times(1)).getColumnCount();
        verifyNoMoreInteractions(metaData);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_MultipleColumns() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenReturn("col2");
        when(metaData.getColumnLabel(3)).thenReturn("col3");

        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(metaData);

        assertNotNull(result);
        assertNotSame(format, result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertArrayEquals(new String[]{"col1", "col2", "col3"}, header);

        verify(metaData, times(1)).getColumnCount();
        verify(metaData, times(1)).getColumnLabel(1);
        verify(metaData, times(1)).getColumnLabel(2);
        verify(metaData, times(1)).getColumnLabel(3);
        verifyNoMoreInteractions(metaData);
    }
}