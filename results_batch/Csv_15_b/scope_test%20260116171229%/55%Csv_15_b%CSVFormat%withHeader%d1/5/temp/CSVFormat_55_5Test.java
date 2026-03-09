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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVFormatWithHeaderTest {

    @Mock
    private ResultSetMetaData metaData;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NullMetaData() throws SQLException {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader((ResultSetMetaData) null);
        assertNotNull(result);
        assertArrayEquals(null, result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyColumns() throws SQLException {
        when(metaData.getColumnCount()).thenReturn(0);

        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader(metaData);

        assertNotNull(result);
        assertArrayEquals(new String[0], result.getHeader());
        verify(metaData, times(1)).getColumnCount();
        verifyNoMoreInteractions(metaData);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MultipleColumns() throws SQLException {
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenReturn("col2");
        when(metaData.getColumnLabel(3)).thenReturn("col3");

        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader(metaData);

        assertNotNull(result);
        assertArrayEquals(new String[] {"col1", "col2", "col3"}, result.getHeader());
        verify(metaData, times(1)).getColumnCount();
        verify(metaData, times(1)).getColumnLabel(1);
        verify(metaData, times(1)).getColumnLabel(2);
        verify(metaData, times(1)).getColumnLabel(3);
        verifyNoMoreInteractions(metaData);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_ColumnLabelsNulls() throws SQLException {
        when(metaData.getColumnCount()).thenReturn(2);
        when(metaData.getColumnLabel(1)).thenReturn(null);
        when(metaData.getColumnLabel(2)).thenReturn("label2");

        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader(metaData);

        assertNotNull(result);
        assertArrayEquals(new String[] {null, "label2"}, result.getHeader());
        verify(metaData, times(1)).getColumnCount();
        verify(metaData, times(1)).getColumnLabel(1);
        verify(metaData, times(1)).getColumnLabel(2);
        verifyNoMoreInteractions(metaData);
    }
}