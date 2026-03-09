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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_54_4Test {

    private ResultSet resultSetMock;
    private ResultSetMetaData metaDataMock;

    @BeforeEach
    void setup() {
        resultSetMock = mock(ResultSet.class);
        metaDataMock = mock(ResultSetMetaData.class);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_ResultSetNotNull() throws SQLException, NoSuchFieldException, IllegalAccessException {
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);
        when(metaDataMock.getColumnCount()).thenReturn(2);
        when(metaDataMock.getColumnLabel(1)).thenReturn("col1");
        when(metaDataMock.getColumnLabel(2)).thenReturn("col2");

        CSVFormat originalFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = originalFormat.withHeader(resultSetMock);

        assertNotNull(newFormat);
        assertNotSame(originalFormat, newFormat);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(newFormat);
        assertNotNull(header);
        assertArrayEquals(new String[]{"col1", "col2"}, header);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_ResultSetNull() throws NoSuchFieldException, IllegalAccessException, SQLException {
        CSVFormat originalFormat = CSVFormat.DEFAULT;

        CSVFormat newFormat = originalFormat.withHeader((ResultSet) null);

        assertNotNull(newFormat);
        assertNotSame(originalFormat, newFormat);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(newFormat);
        assertNull(header);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_ResultSetGetMetaDataThrows() throws SQLException {
        when(resultSetMock.getMetaData()).thenThrow(new SQLException("MetaData error"));

        CSVFormat originalFormat = CSVFormat.DEFAULT;

        SQLException thrown = assertThrows(SQLException.class, () -> {
            originalFormat.withHeader(resultSetMock);
        });

        assertEquals("MetaData error", thrown.getMessage());
    }
}