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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

import java.lang.reflect.Field;
import java.util.Arrays;

class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter, boolean ignoreSurroundingSpaces,
                                      boolean ignoreEmptyLines, String recordSeparator, String nullString,
                                      String[] header, boolean skipHeaderRecord) throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Using reflection to set private final fields since constructor is private
        setField(format, "delimiter", delimiter);
        setField(format, "quoteCharacter", quoteCharacter);
        setField(format, "quoteMode", quoteMode);
        setField(format, "commentMarker", commentMarker);
        setField(format, "escapeCharacter", escapeCharacter);
        setField(format, "ignoreSurroundingSpaces", ignoreSurroundingSpaces);
        setField(format, "ignoreEmptyLines", ignoreEmptyLines);
        setField(format, "recordSeparator", recordSeparator);
        setField(format, "nullString", nullString);
        setField(format, "header", header);
        setField(format, "skipHeaderRecord", skipHeaderRecord);

        return format;
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
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
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        CSVFormat format2 = createCSVFormat(';', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentQuoteMode() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.ALL, null, null,
                false, true, "\r\n", null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacter_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', '"', QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacter_nonEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '"', QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\'', QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarker_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, '#', null,
                false, true, "\r\n", null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarker_nonEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, '#', null,
                false, true, "\r\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, '!', null,
                false, true, "\r\n", null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacter_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, '\\',
                false, true, "\r\n", null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacter_nonEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, '\\',
                false, true, "\r\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, '/',
                false, true, "\r\n", null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullString_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", "NULL", null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullString_nonEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", "NULL", null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", "N/A", null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_header_nonEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, new String[]{"A", "B"}, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, new String[]{"A", "C"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreSurroundingSpaces() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                true, true, "\r\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreEmptyLines() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, false, "\r\n", null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_skipHeaderRecord() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, true);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparator_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparator_nonEqual() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null,
                false, true, "\n", null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_equalObjects() throws Exception {
        String[] header = new String[]{"A", "B"};
        CSVFormat format1 = createCSVFormat(';', '"', QuoteMode.ALL, '#', '\\',
                true, false, "\n", "NULL", header, true);
        CSVFormat format2 = createCSVFormat(';', '"', QuoteMode.ALL, '#', '\\',
                true, false, "\n", "NULL", header.clone(), true);
        assertTrue(format1.equals(format2));
        assertTrue(format2.equals(format1));
    }
}