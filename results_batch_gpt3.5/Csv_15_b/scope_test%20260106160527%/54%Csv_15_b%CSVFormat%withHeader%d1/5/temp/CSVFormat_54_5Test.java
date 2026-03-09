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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVFormat_54_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withNullResultSet() throws SQLException {
        CSVFormat result = csvFormat.withHeader((ResultSet) null);
        assertNotNull(result);
        assertArrayEquals(null, result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withResultSet() throws SQLException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);

        // Spy on csvFormat
        CSVFormat spyFormat = Mockito.spy(csvFormat);

        // Use reflection to create a CSVFormat instance with header {"col1", "col2"}
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat formatWithHeader = constructor.newInstance(
                csvFormat.getDelimiter(),
                csvFormat.getQuoteCharacter(),
                csvFormat.getQuoteMode(),
                csvFormat.getCommentMarker(),
                csvFormat.getEscapeCharacter(),
                csvFormat.getIgnoreSurroundingSpaces(),
                csvFormat.getIgnoreEmptyLines(),
                csvFormat.getRecordSeparator(),
                csvFormat.getNullString(),
                csvFormat.getHeaderComments(),
                new String[]{"col1", "col2"},
                csvFormat.getSkipHeaderRecord(),
                csvFormat.getAllowMissingColumnNames(),
                csvFormat.getIgnoreHeaderCase(),
                csvFormat.getTrim(),
                csvFormat.getTrailingDelimiter(),
                csvFormat.getAutoFlush()
        );

        doReturn(formatWithHeader).when(spyFormat).withHeader(metaData);

        CSVFormat result = spyFormat.withHeader(resultSet);

        assertNotNull(result);
        assertArrayEquals(new String[]{"col1", "col2"}, result.getHeader());

        verify(resultSet).getMetaData();
        verify(spyFormat).withHeader(metaData);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withResultSet_getMetaDataThrows() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("metaData error"));

        assertThrows(SQLException.class, () -> csvFormat.withHeader(resultSet));
    }
}