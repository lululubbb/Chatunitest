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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter, QuoteMode quoteMode, Character quoteCharacter,
                                      Character commentMarker, Character escapeCharacter, String nullString,
                                      String[] header, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      boolean skipHeaderRecord, String recordSeparator) throws Exception {
        // Use reflection to invoke the private constructor
        Class<CSVFormat> clazz = CSVFormat.class;
        var constructor = clazz.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class, boolean.class, boolean.class,
                boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                null, header, skipHeaderRecord, false, false, false, false, false);
    }

    @Test
    @Timeout(8000)
    void testEquals_sameObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentClass() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals("some string"));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentDelimiter() throws Exception {
        CSVFormat f1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat f2 = createCSVFormat(';', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(f1.equals(f2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentQuoteMode() throws Exception {
        CSVFormat f1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat f2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(f1.equals(f2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacter_nulls() throws Exception {
        CSVFormat f1 = createCSVFormat(',', QuoteMode.ALL, null, '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat f2 = createCSVFormat(',', QuoteMode.ALL, null, '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertTrue(f1.equals(f2));

        CSVFormat f3 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(f1.equals(f3));

        CSVFormat f4 = createCSVFormat(',', QuoteMode.ALL, null, '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(f3.equals(f4));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarker_nulls() throws Exception {
        CSVFormat f1 = createCSVFormat(',', QuoteMode.ALL, '"', null, '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat f2 = createCSVFormat(',', QuoteMode.ALL, '"', null, '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertTrue(f1.equals(f2));

        CSVFormat f3 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(f1.equals(f3));

        CSVFormat f4 = createCSVFormat(',', QuoteMode.ALL, '"', null, '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(f3.equals(f4));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacter_nulls() throws Exception {
        CSVFormat f1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', null, "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat f2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', null, "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertTrue(f1.equals(f2));

        CSVFormat f3 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(f1.equals(f3));

        CSVFormat f4 = createCSVFormat(',', QuoteMode.ALL, '"', '#', null, "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(f3.equals(f4));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullString_nulls() throws Exception {
        CSVFormat f1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', null,
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat f2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', null,
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertTrue(f1.equals(f2));

        CSVFormat f3 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(f1.equals(f3));

        CSVFormat f4 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', null,
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(f3.equals(f4));
    }

    @Test
    @Timeout(8000)
    void testEquals_headerArrays() throws Exception {
        CSVFormat f1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat f2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertTrue(f1.equals(f2));

        CSVFormat f3 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "c"}, true, true, true, "\r\n");
        assertFalse(f1.equals(f3));

        CSVFormat f4 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                null, true, true, true, "\r\n");
        CSVFormat f5 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                null, true, true, true, "\r\n");
        assertTrue(f4.equals(f5));

        assertFalse(f1.equals(f4));
    }

    @Test
    @Timeout(8000)
    void testEquals_booleanFields() throws Exception {
        CSVFormat base = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");

        CSVFormat diffIgnoreSurroundingSpaces = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, false, true, true, "\r\n");
        assertFalse(base.equals(diffIgnoreSurroundingSpaces));

        CSVFormat diffIgnoreEmptyLines = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, false, true, "\r\n");
        assertFalse(base.equals(diffIgnoreEmptyLines));

        CSVFormat diffSkipHeaderRecord = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, false, "\r\n");
        assertFalse(base.equals(diffSkipHeaderRecord));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparator_nulls() throws Exception {
        CSVFormat f1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, null);
        CSVFormat f2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, null);
        assertTrue(f1.equals(f2));

        CSVFormat f3 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(f1.equals(f3));

        CSVFormat f4 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"a", "b"}, true, true, true, null);
        assertFalse(f3.equals(f4));
    }

    @Test
    @Timeout(8000)
    void testEquals_allFieldsEqual() throws Exception {
        CSVFormat f1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"header1", "header2"}, true, true, true, "\r\n");
        CSVFormat f2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"header1", "header2"}, true, true, true, "\r\n");
        assertTrue(f1.equals(f2));
    }
}