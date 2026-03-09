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
import java.lang.reflect.Method;

public class CSVFormat_4_2Test {

    @Test
    @Timeout(8000)
    public void testConstructorValidParameters() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        String[] header = new String[]{"col1", "col2"};
        CSVFormat format = constructor.newInstance(
                ',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, false, "\n", "NULL",
                header, true, true);

        assertEquals(',', format.getDelimiter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertEquals(QuoteMode.MINIMAL, format.getQuoteMode());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertEquals("\n", format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
        assertArrayEquals(header, format.getHeader());
        assertTrue(format.getSkipHeaderRecord());
        assertTrue(format.getAllowMissingColumnNames());
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
                false, true, "\r\n", null,
                (String[]) null, false, false);

        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testConstructorDuplicateHeaderThrows() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        String[] dupHeader = new String[]{"a", "b", "a"};

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            constructor.newInstance(
                    ',', '"', null, null, null,
                    false, true, "\r\n", null,
                    dupHeader, false, false);
        });
        assertTrue(thrown.getMessage().contains("duplicate entry"));
    }

    @Test
    @Timeout(8000)
    public void testConstructorDelimiterLineBreakThrows() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Using '\n' line break as delimiter should throw
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            constructor.newInstance(
                    '\n', '"', null, null, null,
                    false, true, "\r\n", null,
                    (String[]) null, false, false);
        });
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateCalled() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Use valid parameters, validate() should not throw
        String[] header = new String[]{"x"};
        CSVFormat format = constructor.newInstance(
                ',', '"', null, null, null,
                false, true, "\r\n", null,
                header, false, false);

        // No exception means validate passed
        assertNotNull(format);
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar() throws Exception {
        Method isLineBreak = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreak.setAccessible(true);

        assertTrue((Boolean) isLineBreak.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreak.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreak.invoke(null, ','));
        assertFalse((Boolean) isLineBreak.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter() throws Exception {
        Method isLineBreak = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreak.setAccessible(true);

        assertTrue((Boolean) isLineBreak.invoke(null, (Character) '\n'));
        assertTrue((Boolean) isLineBreak.invoke(null, (Character) '\r'));
        assertFalse((Boolean) isLineBreak.invoke(null, (Character) ','));
        assertFalse((Boolean) isLineBreak.invoke(null, (Character) 'a'));
        assertFalse((Boolean) isLineBreak.invoke(null, (Character) null));
    }
}