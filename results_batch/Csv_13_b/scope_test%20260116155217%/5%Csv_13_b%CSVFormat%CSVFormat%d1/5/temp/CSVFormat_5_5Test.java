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

class CSVFormat_5_5Test {

    @Test
    @Timeout(8000)
    void testCSVFormatConstructorAndValidate() throws Exception {
        // Use reflection to access private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Test with typical valid parameters
        CSVFormat format = constructor.newInstance(
                ',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, false, "\r\n", "NULL",
                new Object[]{"comment1", "comment2"}, new String[]{"header1", "header2"},
                true, true, false);
        assertNotNull(format);
        assertEquals(',', format.getDelimiter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertEquals(QuoteMode.MINIMAL, format.getQuoteMode());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertEquals("\r\n", format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
        assertArrayEquals(new String[]{"comment1", "comment2"}, format.getHeaderComments());
        assertArrayEquals(new String[]{"header1", "header2"}, format.getHeader());
        assertTrue(format.getSkipHeaderRecord());
        assertTrue(format.getAllowMissingColumnNames());
        assertFalse(format.getIgnoreHeaderCase());

        // Test with null header and headerComments
        CSVFormat format2 = constructor.newInstance(
                ';', null, null, null, null,
                false, true, "\n", null,
                null, null,
                false, false, true);
        assertNotNull(format2);
        assertEquals(';', format2.getDelimiter());
        assertNull(format2.getQuoteCharacter());
        assertNull(format2.getQuoteMode());
        assertNull(format2.getCommentMarker());
        assertNull(format2.getEscapeCharacter());
        assertFalse(format2.getIgnoreSurroundingSpaces());
        assertTrue(format2.getIgnoreEmptyLines());
        assertEquals("\n", format2.getRecordSeparator());
        assertNull(format2.getNullString());
        assertNull(format2.getHeaderComments());
        assertNull(format2.getHeader());
        assertFalse(format2.getSkipHeaderRecord());
        assertFalse(format2.getAllowMissingColumnNames());
        assertTrue(format2.getIgnoreHeaderCase());

        // Test that validate() is called and does not throw for valid input
        // We tested above by successful constructor calls

        // Test invalid delimiter (line break) triggers validate exception
        // Since validate is private, we test via constructor with invalid delimiter
        char invalidDelimiter = '\n'; // line break is invalid delimiter
        Exception ex = assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance(
                    invalidDelimiter, '"', QuoteMode.MINIMAL, null, null,
                    false, false, "\n", null,
                    null, null,
                    false, false, false);
        });
        // Cause should be IllegalArgumentException from validate
        assertTrue(ex.getCause() instanceof IllegalArgumentException);
        assertTrue(ex.getCause().getMessage().contains("The delimiter cannot be a line break"));
    }

    @Test
    @Timeout(8000)
    void testCSVFormatEqualsAndHashCode() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat format1 = constructor.newInstance(
                ',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, false, "\r\n", "NULL",
                new Object[]{"comment1"}, new String[]{"header1"},
                true, true, false);

        CSVFormat format2 = constructor.newInstance(
                ',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, false, "\r\n", "NULL",
                new Object[]{"comment1"}, new String[]{"header1"},
                true, true, false);

        CSVFormat format3 = constructor.newInstance(
                ';', '"', QuoteMode.MINIMAL, '#', '\\',
                true, false, "\r\n", "NULL",
                new Object[]{"comment1"}, new String[]{"header1"},
                true, true, false);

        assertEquals(format1, format2);
        assertEquals(format1.hashCode(), format2.hashCode());
        assertNotEquals(format1, format3);
        assertNotEquals(format1.hashCode(), format3.hashCode());
        assertNotEquals(format1, null);
        assertNotEquals(format1, "some string");
    }

    @Test
    @Timeout(8000)
    void testToString() {
        String s = CSVFormat.DEFAULT.toString();
        assertNotNull(s);
        assertTrue(s.contains("delimiter=,"));
    }

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSetAndIsEscapeCharacterSetAndIsNullStringSetAndIsQuoteCharacterSet() {
        assertFalse(CSVFormat.DEFAULT.isCommentMarkerSet());
        assertFalse(CSVFormat.DEFAULT.isEscapeCharacterSet());
        assertFalse(CSVFormat.DEFAULT.isNullStringSet());
        assertTrue(CSVFormat.DEFAULT.isQuoteCharacterSet());

        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#').withEscape('\\').withNullString("NULL").withQuote(null);
        assertTrue(format.isCommentMarkerSet());
        assertTrue(format.isEscapeCharacterSet());
        assertTrue(format.isNullStringSet());
        assertFalse(format.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testWithMethodsReturnNewInstance() {
        CSVFormat base = CSVFormat.DEFAULT;

        CSVFormat c1 = base.withCommentMarker('#');
        assertNotSame(base, c1);
        assertEquals('#', c1.getCommentMarker());

        CSVFormat c2 = base.withDelimiter(';');
        assertNotSame(base, c2);
        assertEquals(';', c2.getDelimiter());

        CSVFormat c3 = base.withEscape('\\');
        assertNotSame(base, c3);
        assertEquals(Character.valueOf('\\'), c3.getEscapeCharacter());

        CSVFormat c4 = base.withHeader("A", "B");
        assertNotSame(base, c4);
        assertArrayEquals(new String[]{"A", "B"}, c4.getHeader());

        CSVFormat c5 = base.withAllowMissingColumnNames(true);
        assertNotSame(base, c5);
        assertTrue(c5.getAllowMissingColumnNames());

        CSVFormat c6 = base.withIgnoreEmptyLines(false);
        assertNotSame(base, c6);
        assertFalse(c6.getIgnoreEmptyLines());

        CSVFormat c7 = base.withIgnoreSurroundingSpaces(true);
        assertNotSame(base, c7);
        assertTrue(c7.getIgnoreSurroundingSpaces());

        CSVFormat c8 = base.withIgnoreHeaderCase(true);
        assertNotSame(base, c8);
        assertTrue(c8.getIgnoreHeaderCase());

        CSVFormat c9 = base.withNullString("null");
        assertNotSame(base, c9);
        assertEquals("null", c9.getNullString());

        CSVFormat c10 = base.withQuote(null);
        assertNotSame(base, c10);
        assertNull(c10.getQuoteCharacter());

        CSVFormat c11 = base.withQuoteMode(QuoteMode.ALL);
        assertNotSame(base, c11);
        assertEquals(QuoteMode.ALL, c11.getQuoteMode());

        CSVFormat c12 = base.withRecordSeparator("\n");
        assertNotSame(base, c12);
        assertEquals("\n", c12.getRecordSeparator());

        CSVFormat c13 = base.withSkipHeaderRecord(true);
        assertNotSame(base, c13);
        assertTrue(c13.getSkipHeaderRecord());
    }
}