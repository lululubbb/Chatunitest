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

    // Helper method to create CSVFormat instance via reflection
    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord) throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;
        // CSVFormat constructor signature:
        // (char delimiter, Character quoteChar, QuoteMode quoteMode,
        // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        // boolean ignoreEmptyLines, String recordSeparator, String nullString,
        // Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase)
        // We'll pass null for headerComments, false for allowMissingColumnNames and ignoreHeaderCase
        return clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class)
                .newInstance(delimiter, quoteCharacter, quoteMode,
                        commentMarker, escapeCharacter, ignoreSurroundingSpaces,
                        ignoreEmptyLines, recordSeparator, nullString,
                        null, header, skipHeaderRecord,
                        false, false);
    }

    @Test
    @Timeout(8000)
    void testEqualsSameInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    void testEqualsNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    void testEqualsDifferentClass() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals("some string"));
    }

    @Test
    @Timeout(8000)
    void testEqualsDifferentDelimiter() throws Exception {
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format2 = createCSVFormat(';', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsDifferentQuoteMode() throws Exception {
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.ALL,
                null, null, false, true, "\r\n", null,
                null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsQuoteCharacterNullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', null, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format4 = createCSVFormat(',', null, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    void testEqualsQuoteCharacterDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', 'a', QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format2 = createCSVFormat(',', 'b', QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsCommentMarkerNullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                '#', null, false, true, "\r\n", null,
                null, false);
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format4 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    void testEqualsCommentMarkerDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                'a', null, false, true, "\r\n", null,
                null, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                'b', null, false, true, "\r\n", null,
                null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsEscapeCharacterNullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, '\\', false, true, "\r\n", null,
                null, false);
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format4 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    void testEqualsEscapeCharacterDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, 'a', false, true, "\r\n", null,
                null, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, 'b', false, true, "\r\n", null,
                null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsNullStringNullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", "null",
                null, false);
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format4 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    void testEqualsNullStringDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", "a",
                null, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", "b",
                null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsHeaderArraysDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                new String[]{"a", "c"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsHeaderArraysSame() throws Exception {
        String[] header = new String[]{"a", "b"};
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                header, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                header.clone(), false);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsIgnoreSurroundingSpacesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, true, true, "\r\n", null,
                null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsIgnoreEmptyLinesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, false, "\r\n", null,
                null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsSkipHeaderRecordDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, true);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsRecordSeparatorNullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, null, null,
                null, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, null, null,
                null, false);
        CSVFormat format4 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, null, null,
                null, false);
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    void testEqualsRecordSeparatorDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\r\n", null,
                null, false);
        CSVFormat format2 = createCSVFormat(',', DOUBLE_QUOTE_CHAR, QuoteMode.MINIMAL,
                null, null, false, true, "\n", null,
                null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsAllFieldsEqual() throws Exception {
        String[] header = new String[]{"a", "b"};
        CSVFormat format1 = createCSVFormat(';', 'q', QuoteMode.ALL,
                '#', '\\', true, false, "\n", "nullStr",
                header, true);
        CSVFormat format2 = createCSVFormat(';', 'q', QuoteMode.ALL,
                '#', '\\', true, false, "\n", "nullStr",
                header.clone(), true);
        assertTrue(format1.equals(format2));
    }
}