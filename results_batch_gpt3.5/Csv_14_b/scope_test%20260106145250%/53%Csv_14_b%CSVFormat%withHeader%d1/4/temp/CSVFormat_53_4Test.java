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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatWithHeaderTest {

    private CSVFormat baseFormat;

    @BeforeEach
    public void setup() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_NullMetaData() throws SQLException, NoSuchFieldException, IllegalAccessException {
        CSVFormat result = baseFormat.withHeader((ResultSetMetaData) null);
        assertNotNull(result);

        // Use reflection to get the private 'header' field
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(result);

        assertNull(header);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_EmptyMetaData() throws SQLException, NoSuchFieldException, IllegalAccessException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(0);

        CSVFormat result = baseFormat.withHeader(metaData);

        assertNotNull(result);
        assertArrayEquals(new String[0], result.getHeader());

        verify(metaData).getColumnCount();
        verifyNoMoreInteractions(metaData);

        // Additional reflection check to be sure
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(result);
        assertArrayEquals(new String[0], header);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_MultipleColumns() throws SQLException, NoSuchFieldException, IllegalAccessException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("id");
        when(metaData.getColumnLabel(2)).thenReturn("name");
        when(metaData.getColumnLabel(3)).thenReturn("email");

        CSVFormat result = baseFormat.withHeader(metaData);

        assertNotNull(result);
        assertArrayEquals(new String[] { "id", "name", "email" }, result.getHeader());

        verify(metaData).getColumnCount();
        verify(metaData).getColumnLabel(1);
        verify(metaData).getColumnLabel(2);
        verify(metaData).getColumnLabel(3);
        verifyNoMoreInteractions(metaData);

        // Additional reflection check
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(result);
        assertArrayEquals(new String[] { "id", "name", "email" }, header);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_MetaDataThrowsSQLException() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenThrow(new SQLException("Mock SQL Exception"));

        SQLException thrown = assertThrows(SQLException.class, () -> baseFormat.withHeader(metaData));
        assertEquals("Mock SQL Exception", thrown.getMessage());

        verify(metaData).getColumnCount();
        verifyNoMoreInteractions(metaData);
    }
}