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

import java.lang.reflect.Constructor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.Quote;
import org.junit.jupiter.api.Test;

public class CSVFormat_4_4Test {

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_validParameters_shouldCreateInstance() throws Exception {
        // Using reflection to access private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        char delimiter = ';';
        Character quoteChar = Character.valueOf('"');
        Quote quotePolicy = null; // assuming null is valid
        Character commentStart = Character.valueOf('#');
        Character escape = Character.valueOf('\\');
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        String[] header = new String[] { "A", "B", "C" };
        boolean skipHeaderRecord = true;

        CSVFormat csvFormat = constructor.newInstance(
                delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator,
                nullString, header, skipHeaderRecord);

        assertNotNull(csvFormat);
        assertEquals(delimiter, csvFormat.getDelimiter());
        assertEquals(quoteChar, csvFormat.getQuoteChar());
        assertEquals(quotePolicy, csvFormat.getQuotePolicy());
        assertEquals(commentStart, csvFormat.getCommentStart());
        assertEquals(escape, csvFormat.getEscape());
        assertEquals(ignoreSurroundingSpaces, csvFormat.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, csvFormat.getIgnoreEmptyLines());
        assertEquals(recordSeparator, csvFormat.getRecordSeparator());
        assertEquals(nullString, csvFormat.getNullString());
        assertArrayEquals(header, csvFormat.getHeader());
        assertEquals(skipHeaderRecord, csvFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_nullHeader_shouldSetHeaderToNull() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat = constructor.newInstance(
                ',', Character.valueOf('"'), null, null, null,
                false, true, "\r\n",
                null, null, false);

        assertNotNull(csvFormat);
        assertNull(csvFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_lineBreakDelimiter_shouldThrow() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        char[] lineBreaks = {'\r', '\n'};

        for (char lineBreak : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                constructor.newInstance(
                        lineBreak, Character.valueOf('"'), null, null, null,
                        false, true, "\r\n",
                        null, null, false);
            });
            assertEquals("The delimiter cannot be a line break", thrown.getMessage());
        }
    }
}