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

class CSVFormat_32_6Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertTrue(original.getIgnoreEmptyLines());

        CSVFormat updated = original.withIgnoreEmptyLines(true);
        assertNotNull(updated);
        assertTrue(updated.getIgnoreEmptyLines());

        // Original instance remains unchanged
        assertTrue(original.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesFalse() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertTrue(original.getIgnoreEmptyLines());

        CSVFormat updated = original.withIgnoreEmptyLines(false);
        assertNotNull(updated);
        assertFalse(updated.getIgnoreEmptyLines());

        // Original instance remains unchanged
        assertTrue(original.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesCreatesNewInstance() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withIgnoreEmptyLines(!original.getIgnoreEmptyLines());

        assertNotSame(original, updated);
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesPreservesFields() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                Quote.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                String[].class,
                boolean.class);
        constructor.setAccessible(true);

        CSVFormat original = constructor.newInstance(
                ';',
                Character.valueOf('"'),
                null,
                Character.valueOf('#'),
                Character.valueOf('\\'),
                true,
                true,
                "\n",
                "NULL",
                new String[]{"a", "b"},
                true);

        CSVFormat updated = original.withIgnoreEmptyLines(false);

        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteChar(), updated.getQuoteChar());
        assertEquals(original.getQuotePolicy(), updated.getQuotePolicy());
        assertEquals(original.getCommentStart(), updated.getCommentStart());
        assertEquals(original.getEscape(), updated.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertFalse(updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
    }
}