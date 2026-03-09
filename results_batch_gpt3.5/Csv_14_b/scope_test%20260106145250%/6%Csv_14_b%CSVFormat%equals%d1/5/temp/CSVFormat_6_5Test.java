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

    private CSVFormat createCSVFormat(char delimiter, QuoteMode quoteMode, Character quoteCharacter,
                                      Character commentMarker, Character escapeCharacter, String nullString,
                                      String[] header, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      boolean skipHeaderRecord, String recordSeparator) throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

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
    void testEquals_SameObject() {
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
    void testEquals_DifferentDelimiter() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(';', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_DifferentQuoteMode() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_QuoteCharacterNullMismatch() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, null, '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_QuoteCharacterNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '\'', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_CommentMarkerNullMismatch() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', null, '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_CommentMarkerNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '*', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_EscapeCharacterNullMismatch() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', null, "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_EscapeCharacterNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '*', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_NullStringNullMismatch() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', null,
                new String[]{"h1"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_NullStringNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null1",
                new String[]{"h1"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null2",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_HeaderNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1", "h2"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1", "h3"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_IgnoreSurroundingSpacesNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, true, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_IgnoreEmptyLinesNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, false, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_SkipHeaderRecordNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_RecordSeparatorNullMismatch() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, null);
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_RecordSeparatorNotEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1"}, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_AllFieldsEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1", "h2"}, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"h1", "h2"}, false, true, false, "\r\n");
        assertTrue(format1.equals(format2));
    }
}