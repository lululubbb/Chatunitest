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

public class CSVFormat_5_1Test {

    @Test
    @Timeout(8000)
    void testPrivateConstructorAndDefaults() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Create instance with various parameters
        CSVFormat format = constructor.newInstance(
                ';', '"', QuoteMode.ALL, '#', '\\',
                true, false, "\n", "NULL", new Object[]{"comment1", "comment2"},
                new String[]{"h1", "h2"}, true, true, true, true, true);

        assertNotNull(format);
        assertEquals(';', format.getDelimiter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertEquals(QuoteMode.ALL, format.getQuoteMode());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertEquals("\n", format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
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
    void testStaticConstants() {
        assertNotNull(CSVFormat.DEFAULT);
        assertEquals(',', CSVFormat.DEFAULT.getDelimiter());
        assertEquals(Character.valueOf('"'), CSVFormat.DEFAULT.getQuoteCharacter());
        assertEquals("\r\n", CSVFormat.DEFAULT.getRecordSeparator());

        assertNotNull(CSVFormat.EXCEL);
        assertFalse(CSVFormat.EXCEL.getIgnoreEmptyLines());
        assertTrue(CSVFormat.EXCEL.getAllowMissingColumnNames());

        assertNotNull(CSVFormat.INFORMIX_UNLOAD);
        assertEquals('|', CSVFormat.INFORMIX_UNLOAD.getDelimiter());
        assertEquals(Character.valueOf('\\'), CSVFormat.INFORMIX_UNLOAD.getEscapeCharacter());
        assertEquals("\n", CSVFormat.INFORMIX_UNLOAD.getRecordSeparator());

        assertNotNull(CSVFormat.MYSQL);
        assertEquals('\t', CSVFormat.MYSQL.getDelimiter());
        assertEquals(Character.valueOf('\\'), CSVFormat.MYSQL.getEscapeCharacter());
        assertFalse(CSVFormat.MYSQL.getIgnoreEmptyLines());
        assertNull(CSVFormat.MYSQL.getQuoteCharacter());
        assertEquals("\n", CSVFormat.MYSQL.getRecordSeparator());
        assertEquals("\\N", CSVFormat.MYSQL.getNullString());
    }

    @Test
    @Timeout(8000)
    void testNewFormatAndValueOf() {
        CSVFormat format = CSVFormat.newFormat(';');
        assertEquals(';', format.getDelimiter());

        CSVFormat rfc4180 = CSVFormat.valueOf("RFC4180");
        assertNotNull(rfc4180);
        assertEquals(',', rfc4180.getDelimiter());
        assertFalse(rfc4180.getIgnoreEmptyLines());

        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("NON_EXISTENT_FORMAT"));
    }

    @Test
    @Timeout(8000)
    void testEqualsAndHashCode() {
        CSVFormat f1 = CSVFormat.DEFAULT;
        CSVFormat f2 = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        CSVFormat f3 = CSVFormat.DEFAULT;

        assertNotEquals(f1, f2);
        assertEquals(f1, f3);
        assertEquals(f1.hashCode(), f3.hashCode());
    }

    @Test
    @Timeout(8000)
    void testWithMethods() {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat changed = format.withDelimiter(';')
                .withQuote('?')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreEmptyLines(false)
                .withIgnoreHeaderCase(true)
                .withIgnoreSurroundingSpaces(true)
                .withNullString("null")
                .withRecordSeparator("\n")
                .withSkipHeaderRecord(true)
                .withTrailingDelimiter(true)
                .withTrim(true)
                .withAllowMissingColumnNames(true);

        assertEquals(';', changed.getDelimiter());
        assertEquals(Character.valueOf('?'), changed.getQuoteCharacter());
        assertEquals(QuoteMode.MINIMAL, changed.getQuoteMode());
        assertEquals(Character.valueOf('#'), changed.getCommentMarker());
        assertEquals(Character.valueOf('\\'), changed.getEscapeCharacter());
        assertFalse(changed.getIgnoreEmptyLines());
        assertTrue(changed.getIgnoreHeaderCase());
        assertTrue(changed.getIgnoreSurroundingSpaces());
        assertEquals("null", changed.getNullString());
        assertEquals("\n", changed.getRecordSeparator());
        assertTrue(changed.getSkipHeaderRecord());
        assertTrue(changed.getTrailingDelimiter());
        assertTrue(changed.getTrim());
        assertTrue(changed.getAllowMissingColumnNames());
    }
}