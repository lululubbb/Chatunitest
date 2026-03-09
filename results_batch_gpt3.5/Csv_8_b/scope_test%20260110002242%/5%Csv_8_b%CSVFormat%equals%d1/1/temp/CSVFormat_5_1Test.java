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
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Arrays;

class CSVFormatEqualsTest {

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
        assertFalse(format.equals("some string"));
    }

    @Test
    @Timeout(8000)
    void testEquals_allFieldsEqual() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT;
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentDelimiter() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("delimiter", (char) (format1.getDelimiter() + 1));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentQuotePolicy() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("quotePolicy", format1.getQuotePolicy() == null ? Quote.ALL : null);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentQuoteChar() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("quoteChar", format1.getQuoteChar() == null ? 'a' : null);
        assertFalse(format1.equals(format2));

        format2 = createCSVFormatWithField("quoteChar", format1.getQuoteChar() == null ? null : (char)(format1.getQuoteChar() + 1));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentCommentStart() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("commentStart", format1.getCommentStart() == null ? 'a' : null);
        assertFalse(format1.equals(format2));

        format2 = createCSVFormatWithField("commentStart", format1.getCommentStart() == null ? null : (char)(format1.getCommentStart() + 1));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentEscape() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("escape", format1.getEscape() == null ? 'a' : null);
        assertFalse(format1.equals(format2));

        format2 = createCSVFormatWithField("escape", format1.getEscape() == null ? null : (char)(format1.getEscape() + 1));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentNullString() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("nullString", format1.getNullString() == null ? "null" : null);
        assertFalse(format1.equals(format2));

        format2 = createCSVFormatWithField("nullString", format1.getNullString() == null ? null : format1.getNullString() + "diff");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentHeader() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        String[] differentHeader = new String[] {"a", "b"};
        CSVFormat format2 = createCSVFormatWithField("header", differentHeader);
        assertFalse(format1.equals(format2));

        // Also test equal headers
        format2 = createCSVFormatWithField("header", format1.getHeader());
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentIgnoreSurroundingSpaces() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("ignoreSurroundingSpaces", !format1.getIgnoreSurroundingSpaces());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentIgnoreEmptyLines() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("ignoreEmptyLines", !format1.getIgnoreEmptyLines());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentSkipHeaderRecord() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("skipHeaderRecord", !format1.getSkipHeaderRecord());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentRecordSeparator() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("recordSeparator", format1.getRecordSeparator() == null ? "sep" : null);
        assertFalse(format1.equals(format2));

        format2 = createCSVFormatWithField("recordSeparator", format1.getRecordSeparator() == null ? null : format1.getRecordSeparator() + "diff");
        assertFalse(format1.equals(format2));
    }

    // Helper method to create a CSVFormat with one field changed via reflection
    private CSVFormat createCSVFormatWithField(String fieldName, Object value) throws Exception {
        // Get all fields needed for constructor
        CSVFormat base = CSVFormat.DEFAULT;
        Class<?> cls = CSVFormat.class;
        java.lang.reflect.Constructor<CSVFormat> constructor = (java.lang.reflect.Constructor<CSVFormat>) cls.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        Object delimiter = base.getDelimiter();
        Object quoteChar = base.getQuoteChar();
        Object quotePolicy = base.getQuotePolicy();
        Object commentStart = base.getCommentStart();
        Object escape = base.getEscape();
        Object ignoreSurroundingSpaces = base.getIgnoreSurroundingSpaces();
        Object ignoreEmptyLines = base.getIgnoreEmptyLines();
        Object recordSeparator = base.getRecordSeparator();
        Object nullString = base.getNullString();
        Object header = base.getHeader();
        Object skipHeaderRecord = base.getSkipHeaderRecord();

        switch (fieldName) {
            case "delimiter": delimiter = value; break;
            case "quoteChar": quoteChar = value; break;
            case "quotePolicy": quotePolicy = value; break;
            case "commentStart": commentStart = value; break;
            case "escape": escape = value; break;
            case "ignoreSurroundingSpaces": ignoreSurroundingSpaces = value; break;
            case "ignoreEmptyLines": ignoreEmptyLines = value; break;
            case "recordSeparator": recordSeparator = value; break;
            case "nullString": nullString = value; break;
            case "header": header = value; break;
            case "skipHeaderRecord": skipHeaderRecord = value; break;
            default: throw new IllegalArgumentException("Unknown field: " + fieldName);
        }

        return constructor.newInstance(
                delimiter,
                quoteChar,
                quotePolicy,
                commentStart,
                escape,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                header,
                skipHeaderRecord);
    }
}