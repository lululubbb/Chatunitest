package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

import java.lang.reflect.Field;
import java.util.Arrays;

class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter,
                                      QuoteMode quoteMode,
                                      Character quoteCharacter,
                                      Character commentMarker,
                                      Character escapeCharacter,
                                      String nullString,
                                      String[] header,
                                      boolean ignoreSurroundingSpaces,
                                      boolean ignoreEmptyLines,
                                      boolean skipHeaderRecord,
                                      String recordSeparator) throws Exception {
        // Use reflection to invoke private constructor
        Class<CSVFormat> clazz = CSVFormat.class;
        var ctor = clazz.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        ctor.setAccessible(true);
        // We pass null for headerComments and false for trailingDelimiter and trim
        return ctor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                null, header, skipHeaderRecord, false, false, false, false);
    }

    @Test
    @Timeout(8000)
    void testEquals_sameInstance() {
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
        assertFalse(format.equals("A String"));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentDelimiter() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(';', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentQuoteMode() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, null, '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacterNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, null, '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacterDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '\'', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarkerNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', null, '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarkerNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', null, '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarkerDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '!', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', null,
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacterNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', null,
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacterDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '!',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullStringNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullStringNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullStringDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL1", new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL2", new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_headerDifferentLength() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_headerDifferentContent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "c"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreSurroundingSpacesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreEmptyLinesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, false, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_skipHeaderRecordDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparatorNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, null);
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparatorNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, null);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparatorDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, false, true, false, "\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_allFieldsEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, false, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, false, true, "\r\n");
        assertTrue(format1.equals(format2));
        assertTrue(format2.equals(format1));
    }
}