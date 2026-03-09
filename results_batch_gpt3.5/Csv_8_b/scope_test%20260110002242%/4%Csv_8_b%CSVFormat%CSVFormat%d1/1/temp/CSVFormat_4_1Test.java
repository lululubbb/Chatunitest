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

public class CSVFormat_4_1Test {

    @Test
    @Timeout(8000)
    void testCSVFormatConstructor_validParameters() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        char delimiter = ';';
        Character quoteChar = '"';
        Quote quotePolicy = Quote.MINIMAL;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        String[] header = new String[] {"a", "b", "c"};
        boolean skipHeaderRecord = true;

        CSVFormat csvFormat = constructor.newInstance(delimiter, quoteChar, quotePolicy, commentStart,
                escape, ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);

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
    void testCSVFormatConstructor_headerNull() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat = constructor.newInstance(',', '"', Quote.ALL, null,
                null, false, true, "\r\n", null, (String[]) null, false);

        assertNull(csvFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    void testCSVFormatConstructor_invalidDelimiterLineBreak() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        char[] lineBreaks = {'\r', '\n'};
        for (char lb : lineBreaks) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
                constructor.newInstance(lb, '"', Quote.MINIMAL, null,
                        null, false, true, "\r\n", null, (String[]) null, false);
            });
            assertEquals("The delimiter cannot be a line break", ex.getMessage());
        }
    }
}