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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_40_5Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withSkipHeaderRecord(true);

        assertNotSame(original, modified);
        assertEquals(true, modified.getSkipHeaderRecord());

        // Other attributes remain unchanged
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteChar(), modified.getQuoteChar());
        assertEquals(original.getQuotePolicy(), modified.getQuotePolicy());
        assertEquals(original.getCommentStart(), modified.getCommentStart());
        assertEquals(original.getEscape(), modified.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getNullString(), modified.getNullString());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat modified = original.withSkipHeaderRecord(false);

        assertNotSame(original, modified);
        assertEquals(false, modified.getSkipHeaderRecord());

        // Other attributes remain unchanged
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteChar(), modified.getQuoteChar());
        assertEquals(original.getQuotePolicy(), modified.getQuotePolicy());
        assertEquals(original.getCommentStart(), modified.getCommentStart());
        assertEquals(original.getEscape(), modified.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getNullString(), modified.getNullString());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordOnCustomFormat() throws Exception {
        // Use reflection to invoke private constructor
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        ctor.setAccessible(true);
        CSVFormat custom = ctor.newInstance(
                ';', '"', null, '#', '\\',
                true, false, "\n", "NULL", new String[]{"a", "b"}, false);
        CSVFormat modified = custom.withSkipHeaderRecord(true);

        assertNotSame(custom, modified);
        assertTrue(modified.getSkipHeaderRecord());

        assertEquals(custom.getDelimiter(), modified.getDelimiter());
        assertEquals(custom.getQuoteChar(), modified.getQuoteChar());
        assertEquals(custom.getQuotePolicy(), modified.getQuotePolicy());
        assertEquals(custom.getCommentStart(), modified.getCommentStart());
        assertEquals(custom.getEscape(), modified.getEscape());
        assertEquals(custom.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(custom.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(custom.getRecordSeparator(), modified.getRecordSeparator());
        assertArrayEquals(custom.getHeader(), modified.getHeader());
        assertEquals(custom.getNullString(), modified.getNullString());
    }
}