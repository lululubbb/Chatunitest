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
import java.lang.reflect.Field;
import java.util.Arrays;

class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord) throws Exception {
        // Use reflection to instantiate CSVFormat with private constructor
        var constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return (CSVFormat) constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker,
                escapeCharacter, ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                null, header, skipHeaderRecord, false, false);
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
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(';', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsDifferentQuoteMode() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.ALL, null, null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsQuoteCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEqualsQuoteCharacterEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsCommentMarkerNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, '#', null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEqualsCommentMarkerEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, '#', null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, '#', null,
                false, false, null, null, null, false);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsEscapeCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, '\\',
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEqualsEscapeCharacterEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, '\\',
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, '\\',
                false, false, null, null, null, false);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsNullStringNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, "NULL", null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEqualsNullStringEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, "NULL", null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, "NULL", null, false);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsHeaderDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, new String[]{"a", "b"}, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, new String[]{"a", "c"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsHeaderEqual() throws Exception {
        String[] header = new String[]{"a", "b"};
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, header, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, header.clone(), false);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsIgnoreSurroundingSpacesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                true, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsIgnoreEmptyLinesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsSkipHeaderRecordDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, true);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsRecordSeparatorNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, "\n", null, null, false);
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEqualsRecordSeparatorEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, "\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, "\n", null, null, false);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsAllFieldsEqual() throws Exception {
        String[] header = new String[]{"a", "b"};
        CSVFormat format1 = createCSVFormat(';', '\"', QuoteMode.ALL, '#', '\\',
                true, true, "\r\n", "NULL", header, true);
        CSVFormat format2 = createCSVFormat(';', '\"', QuoteMode.ALL, '#', '\\',
                true, true, "\r\n", "NULL", header.clone(), true);
        assertTrue(format1.equals(format2));
    }
}