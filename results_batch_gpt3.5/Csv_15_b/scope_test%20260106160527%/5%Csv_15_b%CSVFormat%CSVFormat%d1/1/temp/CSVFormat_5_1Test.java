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
import org.junit.jupiter.api.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CSVFormat_5_1Test {

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_andDefaults() throws Exception {
        // Using reflection to access private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        char delimiter = ';';
        Character quoteChar = '"';
        QuoteMode quoteMode = QuoteMode.ALL;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        Object[] headerComments = new Object[]{"comment1", "comment2"};
        String[] header = new String[]{"col1", "col2"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = true;
        boolean ignoreHeaderCase = true;
        boolean trim = true;
        boolean trailingDelimiter = true;
        boolean autoFlush = true;

        CSVFormat format = constructor.newInstance(
                delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter, autoFlush);

        // Verify fields via getters
        assertEquals(delimiter, format.getDelimiter());
        assertEquals(quoteChar, format.getQuoteCharacter());
        assertEquals(quoteMode, format.getQuoteMode());
        assertEquals(commentStart, format.getCommentMarker());
        assertEquals(escape, format.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, format.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, format.getIgnoreEmptyLines());
        assertEquals(recordSeparator, format.getRecordSeparator());
        assertEquals(nullString, format.getNullString());
        assertArrayEquals(header, format.getHeader());
        assertArrayEquals(new String[]{"comment1", "comment2"}, format.getHeaderComments());
        assertEquals(skipHeaderRecord, format.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, format.getAllowMissingColumnNames());
        assertEquals(ignoreHeaderCase, format.getIgnoreHeaderCase());
        assertEquals(trim, format.getTrim());
        assertEquals(trailingDelimiter, format.getTrailingDelimiter());
        assertEquals(autoFlush, format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_nullHeaderAndHeaderComments() throws Exception {
        // Test with null header and null headerComments
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
                ',', null, null, null, null,
                false, true, "\r\n", null,
                null, null, false, false,
                false, false, false, false);

        // header and headerComments should be null or empty array
        assertNull(format.getHeader());
        assertNotNull(format.getHeaderComments());
        assertEquals(0, format.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_invalidDelimiter_throwsException() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Delimiter is line break char, expect IllegalArgumentException from validate()
        char invalidDelimiter = '\n';

        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance(
                    invalidDelimiter, null, null, null, null,
                    false, true, "\r\n", null,
                    null, null, false, false,
                    false, false, false, false);
        });
        // The cause should be IllegalArgumentException
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_emptyRecordSeparator_throwsException() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance(
                    ',', null, null, null, null,
                    false, true, "", null,
                    null, null, false, false,
                    false, false, false, false);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_nullRecordSeparator_throwsException() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance(
                    ',', null, null, null, null,
                    false, true, null, null,
                    null, null, false, false,
                    false, false, false, false);
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}