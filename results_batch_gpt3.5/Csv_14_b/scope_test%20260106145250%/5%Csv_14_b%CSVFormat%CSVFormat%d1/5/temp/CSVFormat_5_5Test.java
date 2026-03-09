package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
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

public class CSVFormat_5_5Test {

    @Test
    @Timeout(8000)
    public void testPrivateConstructorAndValidate() throws Exception {
        // Access private constructor with all parameters
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class
        );
        constructor.setAccessible(true);

        // Create instance with various parameters
        CSVFormat format = constructor.newInstance(
                ';', '"', QuoteMode.ALL, '#', '\\',
                true, false, "\n", "NULL",
                new Object[]{"comment1", "comment2"},
                new String[]{"h1", "h2"},
                true, true, true, true, true
        );

        // Validate that fields are set correctly via getters
        assertEquals(';', format.getDelimiter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertEquals(QuoteMode.ALL, format.getQuoteMode());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertEquals("\n", format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
        // The headerComments field is Object[] in constructor but returns String[] via getter
        assertArrayEquals(new String[]{"comment1", "comment2"}, format.getHeaderComments());
        assertArrayEquals(new String[]{"h1", "h2"}, format.getHeader());
        assertTrue(format.getSkipHeaderRecord());
        assertTrue(format.getAllowMissingColumnNames());
        assertTrue(format.getIgnoreHeaderCase());
        assertTrue(format.getTrim());
        assertTrue(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testStaticConstants() {
        assertNotNull(CSVFormat.DEFAULT);
        assertNotNull(CSVFormat.EXCEL);
        assertNotNull(CSVFormat.INFORMIX_UNLOAD);
        assertNotNull(CSVFormat.INFORMIX_UNLOAD_CSV);
        assertNotNull(CSVFormat.MYSQL);
        assertNotNull(CSVFormat.RFC4180);
        assertNotNull(CSVFormat.TDF);
    }

    @Test
    @Timeout(8000)
    public void testNewFormat() {
        CSVFormat format = CSVFormat.newFormat(';');
        assertEquals(';', format.getDelimiter());
        assertNull(format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testValueOf() {
        CSVFormat format = CSVFormat.valueOf("DEFAULT");
        assertEquals(CSVFormat.DEFAULT, format);
        CSVFormat format2 = CSVFormat.valueOf("EXCEL");
        assertEquals(CSVFormat.EXCEL, format2);
    }

    @Test
    @Timeout(8000)
    public void testEqualsAndHashCode() {
        CSVFormat f1 = CSVFormat.DEFAULT;
        CSVFormat f2 = CSVFormat.DEFAULT.withDelimiter(';').withIgnoreEmptyLines(false);
        CSVFormat f3 = CSVFormat.DEFAULT.withDelimiter(',');

        assertEquals(f1, f1);
        assertNotEquals(f1, f2);
        assertEquals(f1.hashCode(), f3.hashCode());
        assertNotEquals(f1.hashCode(), f2.hashCode());
        assertNotEquals(f1, null);
        assertNotEquals(f1, new Object());
    }

    @Test
    @Timeout(8000)
    public void testFormat() {
        CSVFormat format = CSVFormat.DEFAULT;
        String result = format.format("a", null, "c");
        assertTrue(result.contains("a"));
        assertTrue(result.contains("c"));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakStaticMethods() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));

        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));
    }
}