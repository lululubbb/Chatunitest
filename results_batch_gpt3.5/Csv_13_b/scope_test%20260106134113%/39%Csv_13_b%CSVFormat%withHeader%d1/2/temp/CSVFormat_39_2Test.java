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

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class CSVFormatWithHeaderTest {

    @Test
    @Timeout(8000)
    void testWithHeader_NullMetaData() throws SQLException {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeader((ResultSetMetaData) null);
        assertNotNull(result);
        assertNotSame(original, result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyMetaData() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(0);

        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeader(metaData);

        assertNotNull(result);
        assertNotSame(original, result);

        String[] header = result.getHeader();
        assertNotNull(header);
        assertEquals(0, header.length);

        verify(metaData).getColumnCount();
        verifyNoMoreInteractions(metaData);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MultipleColumns() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenReturn("col2");
        when(metaData.getColumnLabel(3)).thenReturn("col3");

        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeader(metaData);

        assertNotNull(result);
        assertNotSame(original, result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertArrayEquals(new String[] {"col1", "col2", "col3"}, header);

        verify(metaData).getColumnCount();
        verify(metaData).getColumnLabel(1);
        verify(metaData).getColumnLabel(2);
        verify(metaData).getColumnLabel(3);
        verifyNoMoreInteractions(metaData);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MetaDataThrowsSQLException() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenThrow(new SQLException("fail"));

        CSVFormat original = CSVFormat.DEFAULT;
        SQLException thrown = assertThrows(SQLException.class, () -> original.withHeader(metaData));
        assertEquals("fail", thrown.getMessage());

        verify(metaData).getColumnCount();
        verifyNoMoreInteractions(metaData);
    }
}