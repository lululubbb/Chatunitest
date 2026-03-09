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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class CSVFormat_5_3Test {

    @Test
    @Timeout(8000)
    void testCSVFormatConstructorAndGetters() throws Exception {
        // Prepare parameters for constructor
        char delimiter = ';';
        Character quoteChar = '"';
        QuoteMode quoteMode = QuoteMode.ALL;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        Object[] headerComments = new Object[]{"header1", "header2"};
        String[] header = new String[]{"col1", "col2"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = true;
        boolean ignoreHeaderCase = true;
        boolean trim = true;
        boolean trailingDelimiter = true;
        boolean autoFlush = true;

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat = constructor.newInstance(
                delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase, trim,
                trailingDelimiter, autoFlush);

        assertEquals(delimiter, csvFormat.getDelimiter());
        assertEquals(quoteChar, csvFormat.getQuoteCharacter());
        assertEquals(quoteMode, csvFormat.getQuoteMode());
        assertEquals(commentStart, csvFormat.getCommentMarker());
        assertEquals(escape, csvFormat.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, csvFormat.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, csvFormat.getIgnoreEmptyLines());
        assertEquals(recordSeparator, csvFormat.getRecordSeparator());
        assertEquals(nullString, csvFormat.getNullString());
        assertArrayEquals(new String[]{"header1", "header2"}, csvFormat.getHeaderComments());
        assertArrayEquals(header, csvFormat.getHeader());
        assertEquals(skipHeaderRecord, csvFormat.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, csvFormat.getAllowMissingColumnNames());
        assertEquals(ignoreHeaderCase, csvFormat.getIgnoreHeaderCase());
        assertEquals(trim, csvFormat.getTrim());
        assertEquals(trailingDelimiter, csvFormat.getTrailingDelimiter());
        assertEquals(autoFlush, csvFormat.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testCSVFormatConstructorWithNullHeaderAndNullHeaderComments() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat = constructor.newInstance(
                ',', null, null, null, null, false,
                true, "\r\n", null, null, null,
                false, false, false, false,
                false, false);

        assertEquals(',', csvFormat.getDelimiter());
        assertNull(csvFormat.getQuoteCharacter());
        assertNull(csvFormat.getQuoteMode());
        assertNull(csvFormat.getCommentMarker());
        assertNull(csvFormat.getEscapeCharacter());
        assertFalse(csvFormat.getIgnoreSurroundingSpaces());
        assertTrue(csvFormat.getIgnoreEmptyLines());
        assertEquals("\r\n", csvFormat.getRecordSeparator());
        assertNull(csvFormat.getNullString());
        assertNull(csvFormat.getHeaderComments());
        assertNull(csvFormat.getHeader());
        assertFalse(csvFormat.getSkipHeaderRecord());
        assertFalse(csvFormat.getAllowMissingColumnNames());
        assertFalse(csvFormat.getIgnoreHeaderCase());
        assertFalse(csvFormat.getTrim());
        assertFalse(csvFormat.getTrailingDelimiter());
        assertFalse(csvFormat.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testCSVFormatConstructorWithEmptyHeaderCommentsArray() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        Object[] emptyHeaderComments = new Object[0];
        CSVFormat csvFormat = constructor.newInstance(
                ',', '"', QuoteMode.MINIMAL, null, null, false,
                true, "\r\n", null, emptyHeaderComments, new String[]{"a", "b"},
                false, false, false, false,
                false, false);

        assertNotNull(csvFormat.getHeaderComments());
        assertEquals(0, csvFormat.getHeaderComments().length);
        assertArrayEquals(new String[]{"a", "b"}, csvFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    void testCSVFormatConstructorValidationFailure() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Delimiter is a line break character which should fail validation
        char invalidDelimiter = '\n';

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () ->
                constructor.newInstance(
                        invalidDelimiter, '"', QuoteMode.MINIMAL, null, null, false,
                        true, "\r\n", null, null, null,
                        false, false, false, false,
                        false, false)
        );
        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
    }
}