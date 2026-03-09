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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.Arrays;

class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord) throws Exception {
        // Use reflection to invoke private constructor
        Class<CSVFormat> clazz = CSVFormat.class;
        var constructor = clazz.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                null, header, skipHeaderRecord, false, false);
    }

    @Test
    @Timeout(8000)
    void testEquals_sameInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    void testEquals_null() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentClass() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals("string"));
    }

    @Test
    @Timeout(8000)
    void testEquals_allFieldsEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentDelimiter() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(';', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentQuoteMode() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacter_nullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacter_nonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacter_notEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', 'a', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', 'b', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarker_nullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, null, '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarker_nonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, null, '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarker_notEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, 'a', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, 'b', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacter_nullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', null,
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacter_nonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', null,
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacter_notEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', 'a',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', 'b',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullString_nullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", null, new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullString_nonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", null, new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullString_notEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "a", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "b", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_header_notEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "c"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreSurroundingSpaces_notEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                false, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreEmptyLines_notEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, false, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_skipHeaderRecord_notEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, true);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparator_nullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, null, "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparator_nonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, null, "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparator_notEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "a", "null", new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "b", "null", new String[]{"a", "b"}, false);
        assertFalse(format1.equals(format2));
    }
}