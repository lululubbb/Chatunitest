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
                                      boolean skipHeaderRecord, boolean allowMissingColumnNames) throws Exception {
        // Use reflection to invoke the private constructor
        Class<CSVFormat> clazz = CSVFormat.class;
        var constructor = clazz.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header,
                skipHeaderRecord, allowMissingColumnNames);
    }

    @Test
    @Timeout(8000)
    void testEquals_SameInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    void testEquals_NullObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    void testEquals_DifferentClass() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals("some string"));
    }

    @Test
    @Timeout(8000)
    void testEquals_AllFieldsEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertTrue(format1.equals(format2));
        assertTrue(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_DifferentDelimiter() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(';', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_DifferentQuoteMode() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_QuoteCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_QuoteCharacterNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_QuoteCharacterDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '\'', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_CommentMarkerNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, null, '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_CommentMarkerNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, null, '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_CommentMarkerDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '*', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_EscapeCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', null,
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_EscapeCharacterNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', null,
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_EscapeCharacterDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '*',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_NullStringNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", null, new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_NullStringNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", null, new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_NullStringDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null1", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null2", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_HeaderDifferentLength() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_HeaderDifferentContent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","c"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_IgnoreSurroundingSpacesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                false, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_IgnoreEmptyLinesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, false, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_SkipHeaderRecordDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, true, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_RecordSeparatorNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, null, "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_RecordSeparatorNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, null, "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_RecordSeparatorDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\n", "null", new String[]{"a","b"}, false, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "null", new String[]{"a","b"}, false, false);
        assertFalse(format1.equals(format2));
    }
}