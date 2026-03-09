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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatWithHeaderTest {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_NullMetaData() throws SQLException {
        CSVFormat result = csvFormat.withHeader((ResultSetMetaData) null);
        assertNotNull(result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_MetaDataWithColumns() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("first");
        when(metaData.getColumnLabel(2)).thenReturn("second");
        when(metaData.getColumnLabel(3)).thenReturn("third");

        CSVFormat result = csvFormat.withHeader(metaData);

        assertNotNull(result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertEquals(3, header.length);
        assertEquals("first", header[0]);
        assertEquals("second", header[1]);
        assertEquals("third", header[2]);

        verify(metaData, times(1)).getColumnCount();
        verify(metaData, times(1)).getColumnLabel(1);
        verify(metaData, times(1)).getColumnLabel(2);
        verify(metaData, times(1)).getColumnLabel(3);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_MetaDataZeroColumns() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(0);

        CSVFormat result = csvFormat.withHeader(metaData);

        assertNotNull(result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertEquals(0, header.length);

        verify(metaData, times(1)).getColumnCount();
        verify(metaData, never()).getColumnLabel(anyInt());
    }

}