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
import java.lang.reflect.Method;

class CSVFormat_5_1Test {

    @Test
    @Timeout(8000)
    void testPrivateConstructorAndValidate() throws Exception {
        // Access the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Create instance with various parameters
        CSVFormat format = constructor.newInstance(
                ';', '"', QuoteMode.ALL, '#', '\\', true, false,
                "\n", "NULL", new Object[]{}, new String[]{"h1", "h2"},
                true, true, true);

        assertNotNull(format);

        // Use reflection to invoke private validate method
        Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
        validateMethod.invoke(format);

        // Assert fields via getters to confirm constructor set them correctly
        assertEquals(';', format.getDelimiter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertEquals(QuoteMode.ALL, format.getQuoteMode());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertEquals("\n", format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
        assertArrayEquals(new String[]{"h1", "h2"}, format.getHeader());
        assertArrayEquals(new String[]{}, format.getHeaderComments());
        assertTrue(format.getSkipHeaderRecord());
        assertTrue(format.getAllowMissingColumnNames());
        assertTrue(format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testPrivateConstructorWithNullHeader() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
                ',', null, null, null, null, false, true,
                "\r\n", null, null, null,
                false, false, false);

        assertNotNull(format);
        assertNull(format.getHeader());
        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testStaticConstants() {
        assertNotNull(CSVFormat.DEFAULT);
        assertNotNull(CSVFormat.RFC4180);
        assertNotNull(CSVFormat.EXCEL);
        assertNotNull(CSVFormat.TDF);
        assertNotNull(CSVFormat.MYSQL);

        // Check some expected properties of constants
        assertEquals(',', CSVFormat.DEFAULT.getDelimiter());
        assertEquals('\t', CSVFormat.TDF.getDelimiter());
        assertTrue(CSVFormat.EXCEL.getAllowMissingColumnNames());
        assertFalse(CSVFormat.DEFAULT.getIgnoreEmptyLines());
        assertFalse(CSVFormat.MYSQL.isQuoteCharacterSet());
    }
}