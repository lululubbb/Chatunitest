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

class CSVFormat_4_6Test {

    private enum Quote {
        MINIMAL, ALL, NONE, NON_NUMERIC
    }

    @Test
    @Timeout(8000)
    void testPrivateConstructor_validParameters() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, CSVFormat.Quote.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        char delimiter = ';';
        Character quoteChar = '"';
        CSVFormat.Quote quotePolicy = CSVFormat.Quote.MINIMAL;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        String[] header = new String[]{"col1", "col2"};
        boolean skipHeaderRecord = true;

        CSVFormat csvFormat = constructor.newInstance(delimiter, quoteChar, quotePolicy,
                commentStart, escape, ignoreSurroundingSpaces, ignoreEmptyLines,
                recordSeparator, nullString, header, skipHeaderRecord);

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
    void testPrivateConstructor_nullHeader() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, CSVFormat.Quote.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat = constructor.newInstance(',', '"', CSVFormat.Quote.MINIMAL,
                null, null, false, true, "\r\n", null, null, false);

        assertNotNull(csvFormat);
        assertNull(csvFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    void testPrivateConstructor_invalidDelimiterLineBreak() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, CSVFormat.Quote.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        char[] lineBreaks = {'\n', '\r'};

        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                constructor.newInstance(lb, '"', CSVFormat.Quote.MINIMAL,
                        null, null, false, true, "\r\n", null, null, false);
            });
            assertEquals("The delimiter cannot be a line break", thrown.getMessage());
        }
    }
}