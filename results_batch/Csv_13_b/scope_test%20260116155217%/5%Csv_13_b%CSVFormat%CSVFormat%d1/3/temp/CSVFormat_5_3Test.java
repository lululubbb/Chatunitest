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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_5_3Test {

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructorAndValidate() throws Exception {
        // Access private constructor via reflection
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Case 1: Normal valid constructor call
        CSVFormat format = constructor.newInstance(
                ',', '"', QuoteMode.ALL, '#', '\\',
                true, false, "\n", "NULL", new Object[]{"comment1"},
                new String[]{"header1", "header2"}, true, true, true);
        assertNotNull(format);
        assertEquals(',', format.getDelimiter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertEquals(QuoteMode.ALL, format.getQuoteMode());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertEquals("\n", format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
        assertArrayEquals(new String[]{"comment1"}, format.getHeaderComments());
        assertArrayEquals(new String[]{"header1", "header2"}, format.getHeader());
        assertTrue(format.getSkipHeaderRecord());
        assertTrue(format.getAllowMissingColumnNames());
        assertTrue(format.getIgnoreHeaderCase());

        // Case 2: header null, headerComments null, quoteMode null, commentStart null, escape null, nullString null
        CSVFormat format2 = constructor.newInstance(
                ';', null, null, null, null,
                false, true, "\r\n", null, null,
                null, false, false, false);
        assertNotNull(format2);
        assertEquals(';', format2.getDelimiter());
        assertNull(format2.getQuoteCharacter());
        assertNull(format2.getQuoteMode());
        assertNull(format2.getCommentMarker());
        assertNull(format2.getEscapeCharacter());
        assertFalse(format2.getIgnoreSurroundingSpaces());
        assertTrue(format2.getIgnoreEmptyLines());
        assertEquals("\r\n", format2.getRecordSeparator());
        assertNull(format2.getNullString());
        assertNull(format2.getHeader());
        assertNull(format2.getHeaderComments());
        assertFalse(format2.getSkipHeaderRecord());
        assertFalse(format2.getAllowMissingColumnNames());
        assertFalse(format2.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testToStringArrayPrivateMethod() throws Exception {
        // Prepare CSVFormat instance to invoke private method
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                ',', '"', QuoteMode.ALL, '#', '\\',
                true, false, "\n", "NULL", null,
                null, true, true, true);

        // Access private toStringArray method
        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        // Case 1: null input returns null
        Object result = toStringArray.invoke(format, (Object) null);
        assertNull(result);

        // Case 2: empty array returns empty String[]
        Object emptyResult = toStringArray.invoke(format, (Object) new Object[0]);
        assertNotNull(emptyResult);
        assertTrue(emptyResult instanceof String[]);
        assertEquals(0, ((String[]) emptyResult).length);

        // Case 3: array with mixed types returns String[] with toString values
        Object[] input = new Object[]{"one", 2, null, 4.5};
        String[] expected = new String[]{"one", "2", null, "4.5"};
        Object mixedResult = toStringArray.invoke(format, (Object) input);
        assertArrayEquals(expected, (String[]) mixedResult);
    }

    @Test
    @Timeout(8000)
    public void testValidatePrivateMethod() throws Exception {
        // Access private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Access private validate method
        Method validate = CSVFormat.class.getDeclaredMethod("validate");
        validate.setAccessible(true);

        // Case 1: valid delimiter and recordSeparator
        CSVFormat format = constructor.newInstance(
                ',', '"', QuoteMode.ALL, null, null,
                false, false, "\n", null, null,
                null, false, false, false);
        validate.invoke(format); // Should not throw

        // Case 2: invalid delimiter (line break char)
        CSVFormat invalidDelimiterFormat = constructor.newInstance(
                '\n', '"', QuoteMode.ALL, null, null,
                false, false, "\n", null, null,
                null, false, false, false);
        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> validate.invoke(invalidDelimiterFormat));
        assertTrue(ex.getCause() instanceof IllegalArgumentException);

        // Case 3: invalid recordSeparator containing line break characters in wrong place
        CSVFormat invalidRecordSeparatorFormat = constructor.newInstance(
                ',', '"', QuoteMode.ALL, null, null,
                false, false, "a\nb", null, null,
                null, false, false, false);
        InvocationTargetException ex2 = assertThrows(InvocationTargetException.class, () -> validate.invoke(invalidRecordSeparatorFormat));
        assertTrue(ex2.getCause() instanceof IllegalArgumentException);
    }
}