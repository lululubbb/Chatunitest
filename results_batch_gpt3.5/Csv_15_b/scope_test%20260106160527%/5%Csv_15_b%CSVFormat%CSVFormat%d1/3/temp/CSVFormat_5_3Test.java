package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_5_3Test {

    private Constructor<CSVFormat> csvFormatConstructor;

    @BeforeEach
    public void setup() throws Exception {
        csvFormatConstructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        csvFormatConstructor.setAccessible(true);
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode, Character commentStart,
            Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines, String recordSeparator,
            String nullString, Object[] headerComments, String[] header, boolean skipHeaderRecord,
            boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim, boolean trailingDelimiter,
            boolean autoFlush) throws Exception {
        return csvFormatConstructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter, autoFlush);
    }

    @Test
    @Timeout(8000)
    public void testConstructorAndGetters() throws Exception {
        Character quoteChar = '"';
        QuoteMode quoteMode = QuoteMode.ALL;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        Object[] headerComments = new Object[] { "comment1", "comment2" };
        String[] header = new String[] { "h1", "h2" };
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = true;
        boolean ignoreHeaderCase = true;
        boolean trim = true;
        boolean trailingDelimiter = true;
        boolean autoFlush = true;

        CSVFormat format = createCSVFormat(',', quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString, headerComments, header, skipHeaderRecord,
                allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter, autoFlush);

        assertEquals(',', format.getDelimiter());
        assertEquals(quoteChar, format.getQuoteCharacter());
        assertEquals(quoteMode, format.getQuoteMode());
        assertEquals(commentStart, format.getCommentMarker());
        assertEquals(escape, format.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, format.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, format.getIgnoreEmptyLines());
        assertEquals(recordSeparator, format.getRecordSeparator());
        assertEquals(nullString, format.getNullString());
        assertArrayEquals(new String[] { "comment1", "comment2" }, format.getHeaderComments());
        assertArrayEquals(header, format.getHeader());
        assertEquals(skipHeaderRecord, format.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, format.getAllowMissingColumnNames());
        assertEquals(ignoreHeaderCase, format.getIgnoreHeaderCase());
        assertEquals(trim, format.getTrim());
        assertEquals(trailingDelimiter, format.getTrailingDelimiter());
        assertEquals(autoFlush, format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testValidateCalled() throws Exception {
        // The constructor calls validate() - if invalid, it throws IllegalArgumentException.
        // We test that invalid delimiter throws exception.
        assertThrows(IllegalArgumentException.class, () -> {
            createCSVFormat('\n', '"', null, null, null, false, true, "\r\n", null, null, null, false, false, false,
                    false, false, false);
        });
    }

    @Test
    @Timeout(8000)
    public void testStaticConstants() {
        assertNotNull(CSVFormat.DEFAULT);
        assertEquals(',', CSVFormat.DEFAULT.getDelimiter());
        assertEquals("\r\n", CSVFormat.DEFAULT.getRecordSeparator());

        assertNotNull(CSVFormat.EXCEL);
        assertFalse(CSVFormat.EXCEL.getIgnoreEmptyLines());

        assertEquals('|', CSVFormat.INFORMIX_UNLOAD.getDelimiter());
        assertEquals('\\', CSVFormat.INFORMIX_UNLOAD.getEscapeCharacter());

        assertEquals('\t', CSVFormat.TDF.getDelimiter());
        assertTrue(CSVFormat.TDF.getIgnoreSurroundingSpaces());
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
        assertEquals(CSVFormat.DEFAULT, CSVFormat.valueOf("DEFAULT"));
        assertEquals(CSVFormat.EXCEL, CSVFormat.valueOf("EXCEL"));
        assertEquals(CSVFormat.INFORMIX_UNLOAD, CSVFormat.valueOf("INFORMIX_UNLOAD"));
        assertEquals(CSVFormat.INFORMIX_UNLOAD_CSV, CSVFormat.valueOf("INFORMIX_UNLOAD_CSV"));
        assertEquals(CSVFormat.MYSQL, CSVFormat.valueOf("MYSQL"));
        assertEquals(CSVFormat.POSTGRESQL_CSV, CSVFormat.valueOf("POSTGRESQL_CSV"));
        assertEquals(CSVFormat.POSTGRESQL_TEXT, CSVFormat.valueOf("POSTGRESQL_TEXT"));
        assertEquals(CSVFormat.RFC4180, CSVFormat.valueOf("RFC4180"));
        assertEquals(CSVFormat.TDF, CSVFormat.valueOf("TDF"));

        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("UNKNOWN"));
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }

    @Test
    @Timeout(8000)
    public void testEqualsAndHashCode() throws Exception {
        CSVFormat f1 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\', true, false, "\n", "NULL", null,
                new String[] { "h1" }, false, true, true, true, true, true);
        CSVFormat f2 = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\', true, false, "\n", "NULL", null,
                new String[] { "h1" }, false, true, true, true, true, true);
        CSVFormat f3 = createCSVFormat(';', '"', QuoteMode.ALL, '#', '\\', true, false, "\n", "NULL", null,
                new String[] { "h1" }, false, true, true, true, true, true);

        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
        assertNotEquals(f1, f3);
        assertNotEquals(f1.hashCode(), f3.hashCode());
        assertNotEquals(f1, null);
        assertNotEquals(f1, "string");
    }

    @Test
    @Timeout(8000)
    public void testToString() {
        String s = CSVFormat.DEFAULT.toString();
        assertNotNull(s);
        assertTrue(s.contains("delimiter="));
        assertTrue(s.contains("recordSeparator="));
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

    @Test
    @Timeout(8000)
    public void testWithMethods() throws Exception {
        CSVFormat base = CSVFormat.DEFAULT;

        CSVFormat f1 = base.withAllowMissingColumnNames(true);
        assertTrue(f1.getAllowMissingColumnNames());

        CSVFormat f2 = base.withCommentMarker('#');
        assertEquals((Character) '#', f2.getCommentMarker());

        CSVFormat f3 = base.withDelimiter(';');
        assertEquals(';', f3.getDelimiter());

        CSVFormat f4 = base.withEscape('\\');
        assertEquals((Character) '\\', f4.getEscapeCharacter());

        CSVFormat f5 = base.withIgnoreEmptyLines(false);
        assertFalse(f5.getIgnoreEmptyLines());

        CSVFormat f6 = base.withIgnoreHeaderCase(true);
        assertTrue(f6.getIgnoreHeaderCase());

        CSVFormat f7 = base.withIgnoreSurroundingSpaces(true);
        assertTrue(f7.getIgnoreSurroundingSpaces());

        CSVFormat f8 = base.withNullString("NULL");
        assertEquals("NULL", f8.getNullString());

        CSVFormat f9 = base.withQuote('"');
        assertEquals((Character) '"', f9.getQuoteCharacter());

        CSVFormat f10 = base.withQuoteMode(QuoteMode.ALL_NON_NULL);
        assertEquals(QuoteMode.ALL_NON_NULL, f10.getQuoteMode());

        CSVFormat f11 = base.withRecordSeparator('\n');
        assertEquals("\n", f11.getRecordSeparator());

        CSVFormat f12 = base.withSkipHeaderRecord(true);
        assertTrue(f12.getSkipHeaderRecord());

        CSVFormat f13 = base.withTrailingDelimiter(true);
        assertTrue(f13.getTrailingDelimiter());

        CSVFormat f14 = base.withTrim(true);
        assertTrue(f14.getTrim());

        CSVFormat f15 = base.withAutoFlush(true);
        assertTrue(f15.getAutoFlush());
    }
}