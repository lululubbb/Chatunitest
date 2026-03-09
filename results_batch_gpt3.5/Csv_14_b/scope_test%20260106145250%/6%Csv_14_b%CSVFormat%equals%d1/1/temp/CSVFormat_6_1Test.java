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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.Arrays;

class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter, QuoteMode quoteMode, Character quoteCharacter,
                                      Character commentMarker, Character escapeCharacter, String nullString,
                                      String[] header, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      boolean skipHeaderRecord, String recordSeparator) throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to set private final fields
        setField(format, "delimiter", delimiter);
        setField(format, "quoteMode", quoteMode);
        setField(format, "quoteCharacter", quoteCharacter);
        setField(format, "commentMarker", commentMarker);
        setField(format, "escapeCharacter", escapeCharacter);
        setField(format, "nullString", nullString);
        setField(format, "header", header);
        setField(format, "ignoreSurroundingSpaces", ignoreSurroundingSpaces);
        setField(format, "ignoreEmptyLines", ignoreEmptyLines);
        setField(format, "skipHeaderRecord", skipHeaderRecord);
        setField(format, "recordSeparator", recordSeparator);
        return format;
    }

    private void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
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
    void testEquals_allFieldsEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        assertTrue(format1.equals(format2));
        assertTrue(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_delimiterDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(';', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteModeDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacterNullity() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, null, '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', QuoteMode.MINIMAL, null, '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format4 = createCSVFormat(',', QuoteMode.MINIMAL, null, '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarkerNullity() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', null, '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', QuoteMode.MINIMAL, '"', null, '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format4 = createCSVFormat(',', QuoteMode.MINIMAL, '"', null, '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacterNullity() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', null,
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', null,
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format4 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', null,
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullStringNullity() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format4 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, new String[]{"a", "b"}, true, true, false, "\r\n");
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    void testEquals_headerDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "c"}, true, true, false, "\r\n");
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", null, true, true, false, "\r\n");
        CSVFormat format4 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a"}, true, true, false, "\r\n");
        assertFalse(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreSurroundingSpacesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreEmptyLinesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, false, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_skipHeaderRecordDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparatorNullity() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, null);
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, "\r\n");
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, null);
        CSVFormat format4 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                "NULL", new String[]{"a", "b"}, true, true, false, null);
        assertTrue(format3.equals(format4));
    }
}