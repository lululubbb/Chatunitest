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
        // Use reflection to create CSVFormat instance with given parameters
        // The constructor is private, so we access it reflectively
        var constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        return (CSVFormat) constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, null, header, skipHeaderRecord,
                false, false, false, false, false);
    }

    @Test
    @Timeout(8000)
    void testEqualsSameObject() {
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
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(';', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsDifferentQuoteMode() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsQuoteCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, null, '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsQuoteCharacterDifferentNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '\'', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsCommentMarkerNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', null, '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsCommentMarkerDifferentNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '!', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsEscapeCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', null, "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsEscapeCharacterDifferentNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '/', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsNullStringNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', null, new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsNullStringDifferentNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null1", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null2", new String[]{"a", "b"},
                true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsHeaderDifferentLengths() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a"},
                true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsHeaderDifferentContents() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "c"},
                true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsIgnoreSurroundingSpacesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                false, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsIgnoreEmptyLinesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, false, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsSkipHeaderRecordDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsRecordSeparatorNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, null);
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsRecordSeparatorDifferentNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", new String[]{"a", "b"},
                true, true, true, "\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEqualsAllFieldsEqual() throws Exception {
        String[] header = new String[]{"a", "b"};
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", header,
                true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null", header,
                true, true, true, "\r\n");
        assertTrue(format1.equals(format2));
    }
}