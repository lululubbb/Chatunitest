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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter, QuoteMode quoteMode, Character quoteCharacter,
                                      Character commentMarker, Character escapeCharacter, String nullString,
                                      String[] header, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      boolean skipHeaderRecord, String recordSeparator) throws Exception {
        // Use reflection to create instance with private constructor
        // Constructor signature:
        // CSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
        // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        // boolean ignoreEmptyLines, String recordSeparator, String nullString,
        // Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim,
        // boolean trailingDelimiter, boolean autoFlush)
        var ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        ctor.setAccessible(true);
        return ctor.newInstance(
                delimiter, quoteCharacter, quoteMode,
                commentMarker, escapeCharacter, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString,
                null, header, skipHeaderRecord,
                false, false, false,
                false, false);
    }

    @Test
    @Timeout(8000)
    void testEquals_SameReference() {
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
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_DifferentDelimiter() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ';', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_DifferentQuoteMode() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.ALL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_QuoteCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, null, '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_QuoteCharacterNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, null, '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_QuoteCharacterDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '\'', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_CommentMarkerNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', null, '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_CommentMarkerNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', null, '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_CommentMarkerDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '*', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_EscapeCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', null, "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_EscapeCharacterNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', null, "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_EscapeCharacterDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '*', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_NullStringNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', null,
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_NullStringNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', null,
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_NullStringDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL1",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL2",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_HeaderDifferentLength() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_HeaderDifferentContent() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "c"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_IgnoreSurroundingSpacesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, false, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_IgnoreEmptyLinesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, false, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_SkipHeaderRecordDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_RecordSeparatorNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, null);
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_RecordSeparatorNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, null);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_RecordSeparatorDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(
                ',', QuoteMode.MINIMAL, '"', '#', '\\', "NULL",
                new String[]{"a", "b"}, true, true, true, "\n");
        assertFalse(format1.equals(format2));
    }
}