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

public class CSVFormat_33_4Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        // Confirm original ignoreSurroundingSpaces is false
        assertFalse(original.getIgnoreSurroundingSpaces());

        // Invoke withIgnoreSurroundingSpaces(true)
        CSVFormat modifiedTrue = original.withIgnoreSurroundingSpaces(true);
        assertNotNull(modifiedTrue);
        assertTrue(modifiedTrue.getIgnoreSurroundingSpaces());
        // Other fields remain the same
        assertEquals(original.getDelimiter(), modifiedTrue.getDelimiter());
        assertEquals(original.getQuoteChar(), modifiedTrue.getQuoteChar());
        assertEquals(original.getQuotePolicy(), modifiedTrue.getQuotePolicy());
        assertEquals(original.getCommentStart(), modifiedTrue.getCommentStart());
        assertEquals(original.getEscape(), modifiedTrue.getEscape());
        assertEquals(original.getIgnoreEmptyLines(), modifiedTrue.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modifiedTrue.getRecordSeparator());
        assertArrayEquals(original.getHeader(), modifiedTrue.getHeader());
        assertEquals(original.getNullString(), modifiedTrue.getNullString());
        assertEquals(original.getSkipHeaderRecord(), modifiedTrue.getSkipHeaderRecord());

        // Invoke withIgnoreSurroundingSpaces(false) on modifiedTrue, should produce original
        CSVFormat modifiedFalse = modifiedTrue.withIgnoreSurroundingSpaces(false);
        assertNotNull(modifiedFalse);
        assertFalse(modifiedFalse.getIgnoreSurroundingSpaces());
        assertEquals(original, modifiedFalse);

        // Use reflection to invoke private constructor and verify withIgnoreSurroundingSpaces field
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class,
                Character.class, Character.class, boolean.class, boolean.class,
                String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat reflected = constructor.newInstance(
                ',',
                Character.valueOf('"'),
                null,
                null,
                null,
                true,
                true,
                "\r\n",
                null,
                (String[]) null,
                false);
        assertTrue(reflected.getIgnoreSurroundingSpaces());
    }
}