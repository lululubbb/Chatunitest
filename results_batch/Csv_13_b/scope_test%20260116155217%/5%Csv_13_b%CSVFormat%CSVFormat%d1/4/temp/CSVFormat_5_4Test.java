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

public class CSVFormat_5_4Test {

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructorAndValidate() throws Exception {
        // Using reflection to access private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Valid inputs
        CSVFormat format = constructor.newInstance(
                ',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, true, "\r\n", "NULL",
                new Object[]{"comment1", "comment2"}, new String[]{"header1", "header2"},
                true, false, true);
        assertNotNull(format);
        assertEquals(',', format.getDelimiter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertEquals(QuoteMode.MINIMAL, format.getQuoteMode());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertTrue(format.getIgnoreEmptyLines());
        assertEquals("\r\n", format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
        assertArrayEquals(new String[]{"header1", "header2"}, format.getHeader());
        assertArrayEquals(new String[]{"comment1", "comment2"}, format.getHeaderComments());
        assertTrue(format.getSkipHeaderRecord());
        assertFalse(format.getAllowMissingColumnNames());
        assertTrue(format.getIgnoreHeaderCase());

        // Test with null header and headerComments
        CSVFormat format2 = constructor.newInstance(
                ';', null, null, null, null,
                false, false, "\n", null,
                null, null,
                false, true, false);
        assertNotNull(format2);
        assertEquals(';', format2.getDelimiter());
        assertNull(format2.getQuoteCharacter());
        assertNull(format2.getQuoteMode());
        assertNull(format2.getCommentMarker());
        assertNull(format2.getEscapeCharacter());
        assertFalse(format2.getIgnoreSurroundingSpaces());
        assertFalse(format2.getIgnoreEmptyLines());
        assertEquals("\n", format2.getRecordSeparator());
        assertNull(format2.getNullString());
        assertNull(format2.getHeader());
        assertNull(format2.getHeaderComments());
        assertFalse(format2.getSkipHeaderRecord());
        assertTrue(format2.getAllowMissingColumnNames());
        assertFalse(format2.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatValidateThrowsException() throws Exception {
        // Access private constructor and validate method
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Create instance with invalid delimiter (line break)
        CSVFormat format = constructor.newInstance(
                '\n', '"', QuoteMode.MINIMAL, null, null,
                false, false, "\n", null,
                null, null,
                false, false, false);

        Method validate = CSVFormat.class.getDeclaredMethod("validate");
        validate.setAccessible(true);

        InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> validate.invoke(format));
        assertTrue(e.getCause() instanceof IllegalArgumentException);
    }
}