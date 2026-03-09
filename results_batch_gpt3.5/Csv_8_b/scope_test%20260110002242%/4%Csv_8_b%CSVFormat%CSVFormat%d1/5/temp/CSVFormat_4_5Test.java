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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.Quote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

public class CSVFormat_4_5Test {

    private Constructor<CSVFormat> constructor;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class,
                Character.class, boolean.class, boolean.class,
                String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_validParameters() throws Exception {
        char delimiter = ';';
        Character quoteChar = '"';
        Quote quotePolicy = Quote.MINIMAL;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        String[] header = new String[]{"col1", "col2"};
        boolean skipHeaderRecord = true;

        CSVFormat format = constructor.newInstance(delimiter, quoteChar, quotePolicy,
                commentStart, escape, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString,
                (Object) header, skipHeaderRecord);

        assertEquals(delimiter, format.getDelimiter());
        assertEquals(quoteChar, format.getQuoteChar());
        assertEquals(quotePolicy, format.getQuotePolicy());
        assertEquals(commentStart, format.getCommentStart());
        assertEquals(escape, format.getEscape());
        assertEquals(ignoreSurroundingSpaces, format.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, format.getIgnoreEmptyLines());
        assertEquals(recordSeparator, format.getRecordSeparator());
        assertEquals(nullString, format.getNullString());
        assertArrayEquals(header, format.getHeader());
        assertEquals(skipHeaderRecord, format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_delimiterIsLineBreak_throwsIllegalArgumentException() throws Exception {
        char[] lineBreaks = {'\r', '\n'};
        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                    () -> constructor.newInstance(lb, null, null,
                            null, null, false, false,
                            null, null, null, false));
            assertEquals("The delimiter cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testConstructor_nullHeaderCloned() throws Exception {
        String[] header = new String[]{"a", "b"};
        CSVFormat format = constructor.newInstance(',', null, null,
                null, null, false, false,
                null, null, (Object) header, false);
        String[] returnedHeader = format.getHeader();
        assertNotSame(header, returnedHeader);
        assertArrayEquals(header, returnedHeader);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_nullHeaderIsNull() throws Exception {
        CSVFormat format = constructor.newInstance(',', null, null,
                null, null, false, false,
                null, null, null, false);
        assertNull(format.getHeader());
    }
}