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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class CSVFormat_5_6Test {

    @Test
    @Timeout(8000)
    void testCSVFormatConstructorAndValidate() throws Exception {
        // Use reflection to access private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Normal constructor call
        CSVFormat csvFormat = constructor.newInstance(
                ',', '"', QuoteMode.MINIMAL,
                '#', '\\', true,
                false, "\n", "NULL",
                new Object[]{"comment1", "comment2"}, new String[]{"header1", "header2"},
                true, true, true);

        assertNotNull(csvFormat);
        assertEquals(',', csvFormat.getDelimiter());
        assertEquals(Character.valueOf('"'), csvFormat.getQuoteCharacter());
        assertEquals(QuoteMode.MINIMAL, csvFormat.getQuoteMode());
        assertEquals(Character.valueOf('#'), csvFormat.getCommentMarker());
        assertEquals(Character.valueOf('\\'), csvFormat.getEscapeCharacter());
        assertTrue(csvFormat.getIgnoreSurroundingSpaces());
        assertFalse(csvFormat.getIgnoreEmptyLines());
        assertEquals("\n", csvFormat.getRecordSeparator());
        assertEquals("NULL", csvFormat.getNullString());
        assertArrayEquals(new String[]{"comment1", "comment2"}, csvFormat.getHeaderComments());
        assertArrayEquals(new String[]{"header1", "header2"}, csvFormat.getHeader());
        assertTrue(csvFormat.getSkipHeaderRecord());
        assertTrue(csvFormat.getAllowMissingColumnNames());
        assertTrue(csvFormat.getIgnoreHeaderCase());

        // Test with null header and headerComments
        CSVFormat csvFormat2 = constructor.newInstance(
                ';', null, null,
                null, null, false,
                true, "\r\n", null,
                null, null,
                false, false, false);

        assertNotNull(csvFormat2);
        assertEquals(';', csvFormat2.getDelimiter());
        assertNull(csvFormat2.getQuoteCharacter());
        assertNull(csvFormat2.getQuoteMode());
        assertNull(csvFormat2.getCommentMarker());
        assertNull(csvFormat2.getEscapeCharacter());
        assertFalse(csvFormat2.getIgnoreSurroundingSpaces());
        assertTrue(csvFormat2.getIgnoreEmptyLines());
        assertEquals("\r\n", csvFormat2.getRecordSeparator());
        assertNull(csvFormat2.getNullString());
        assertNull(csvFormat2.getHeaderComments());
        assertNull(csvFormat2.getHeader());
        assertFalse(csvFormat2.getSkipHeaderRecord());
        assertFalse(csvFormat2.getAllowMissingColumnNames());
        assertFalse(csvFormat2.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testValidateMethod() throws Exception {
        // Access private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat;

        // Valid delimiter and recordSeparator
        csvFormat = constructor.newInstance(
                ',', '"', QuoteMode.MINIMAL,
                null, null, false,
                true, "\n", null,
                null, null,
                false, false, false);

        Method validate = CSVFormat.class.getDeclaredMethod("validate");
        validate.setAccessible(true);
        // Should not throw exception
        validate.invoke(csvFormat);

        // Delimiter is line break character - should throw IllegalArgumentException
        CSVFormat badDelimiterFormat = constructor.newInstance(
                '\n', '"', QuoteMode.MINIMAL,
                null, null, false,
                true, "\n", null,
                null, null,
                false, false, false);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> validate.invoke(badDelimiterFormat));
        assertTrue(ex.getCause() instanceof IllegalArgumentException);

        // recordSeparator is null - should throw IllegalArgumentException
        CSVFormat nullRecordSeparator = constructor.newInstance(
                ',', '"', QuoteMode.MINIMAL,
                null, null, false,
                true, null, null,
                null, null,
                false, false, false);

        InvocationTargetException ex2 = assertThrows(InvocationTargetException.class, () -> validate.invoke(nullRecordSeparator));
        assertTrue(ex2.getCause() instanceof IllegalArgumentException);
    }

    @Test
    @Timeout(8000)
    void testToStringArray() throws Exception {
        // Access private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat = constructor.newInstance(
                ',', '"', QuoteMode.MINIMAL,
                null, null, false,
                true, "\n", null,
                null, null,
                false, false, false);

        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        // null input returns null
        assertNull(toStringArray.invoke(csvFormat, (Object) null));

        // empty array returns empty string array
        String[] result = (String[]) toStringArray.invoke(csvFormat, (Object) new Object[] {});
        assertNotNull(result);
        assertEquals(0, result.length);

        // array with nulls and objects
        Object[] input = new Object[] { "a", null, 123, new StringBuilder("sb") };
        String[] output = (String[]) toStringArray.invoke(csvFormat, (Object) input);
        assertArrayEquals(new String[] { "a", null, "123", "sb" }, output);
    }
}