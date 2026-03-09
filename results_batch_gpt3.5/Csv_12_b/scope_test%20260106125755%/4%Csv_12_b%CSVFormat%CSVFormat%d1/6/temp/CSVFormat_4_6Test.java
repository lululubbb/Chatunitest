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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CSVFormat_4_6Test {

    @Test
    @Timeout(8000)
    public void testConstructorValidParameters() throws Exception {
        // Using reflection to access private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Valid header with no duplicates
        String[] header = new String[]{"col1", "col2"};
        CSVFormat format = constructor.newInstance(
                ',', '"', null, '#', '\\',
                true, true, "\r\n", "NULL", header,
                true, false);

        assertNotNull(format);
        assertEquals(',', format.getDelimiter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertEquals('#', format.getCommentMarker());
        assertEquals('\\', format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertTrue(format.getIgnoreEmptyLines());
        assertEquals("\r\n", format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
        assertArrayEquals(header, format.getHeader());
        assertTrue(format.getSkipHeaderRecord());
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testConstructorDelimiterLineBreakThrows() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Delimiters that are line breaks: '\r', '\n'
        char[] lineBreaks = {'\r', '\n'};
        for (char delimiter : lineBreaks) {
            InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
                constructor.newInstance(
                        delimiter, '"', null, null, null,
                        false, false, "\r\n", null, (String[]) null,
                        false, false);
            });
            Throwable cause = ex.getCause();
            assertTrue(cause instanceof IllegalArgumentException);
            assertEquals("The delimiter cannot be a line break", cause.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testConstructorDuplicateHeadersThrows() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        String[] duplicateHeader = new String[]{"a", "b", "a"};
        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance(
                    ',', '"', null, null, null,
                    false, false, "\r\n", null, duplicateHeader,
                    false, false);
        });
        // The cause should be IllegalArgumentException with message about duplicate
        Throwable cause = ex.getCause();
        assertTrue(cause instanceof IllegalArgumentException);
        assertTrue(cause.getMessage().contains("The header contains a duplicate entry"));
    }

    @Test
    @Timeout(8000)
    public void testConstructorNullHeader() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
                ',', '"', null, null, null,
                false, false, "\r\n", null, (String[]) null,
                false, false);
        assertNull(format.getHeader());
    }
}