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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_5_4Test {

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructorAndGetters() throws Exception {
        // Using reflection to get the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        char delimiter = ';';
        Character quoteChar = '"';
        QuoteMode quoteMode = QuoteMode.ALL;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        Object[] headerComments = new Object[]{"comment1", "comment2"};
        String[] header = new String[]{"h1", "h2"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = true;
        boolean ignoreHeaderCase = true;
        boolean trim = true;
        boolean trailingDelimiter = true;

        CSVFormat csvFormat = constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter);

        assertEquals(delimiter, csvFormat.getDelimiter());
        assertEquals(quoteChar, csvFormat.getQuoteCharacter());
        assertEquals(quoteMode, csvFormat.getQuoteMode());
        assertEquals(commentStart, csvFormat.getCommentMarker());
        assertEquals(escape, csvFormat.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, csvFormat.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, csvFormat.getIgnoreEmptyLines());
        assertEquals(recordSeparator, csvFormat.getRecordSeparator());
        assertEquals(nullString, csvFormat.getNullString());
        assertArrayEquals(new String[]{"comment1", "comment2"}, csvFormat.getHeaderComments());
        assertArrayEquals(header, csvFormat.getHeader());
        assertEquals(skipHeaderRecord, csvFormat.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, csvFormat.getAllowMissingColumnNames());
        assertEquals(ignoreHeaderCase, csvFormat.getIgnoreHeaderCase());
        assertEquals(trim, csvFormat.getTrim());
        assertEquals(trailingDelimiter, csvFormat.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testStaticConstantsAndDefaults() {
        assertNotNull(CSVFormat.DEFAULT);
        assertEquals(',', CSVFormat.DEFAULT.getDelimiter());
        assertEquals('\r', CSVFormat.DEFAULT.getRecordSeparator().charAt(0));
        assertFalse(CSVFormat.DEFAULT.getIgnoreSurroundingSpaces());
        assertTrue(CSVFormat.DEFAULT.getIgnoreEmptyLines());

        assertNotNull(CSVFormat.EXCEL);
        assertFalse(CSVFormat.EXCEL.getIgnoreEmptyLines());
        assertTrue(CSVFormat.EXCEL.getAllowMissingColumnNames());

        assertNotNull(CSVFormat.INFORMIX_UNLOAD);
        assertEquals('|', CSVFormat.INFORMIX_UNLOAD.getDelimiter());
        assertEquals('\n', CSVFormat.INFORMIX_UNLOAD.getRecordSeparator().charAt(0));
        assertEquals('\\', CSVFormat.INFORMIX_UNLOAD.getEscapeCharacter());

        assertNotNull(CSVFormat.MYSQL);
        assertEquals('\t', CSVFormat.MYSQL.getDelimiter());
        assertEquals("\\N", CSVFormat.MYSQL.getNullString());
        assertNull(CSVFormat.MYSQL.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testNewFormatAndValueOf() {
        CSVFormat format = CSVFormat.newFormat(';');
        assertEquals(';', format.getDelimiter());

        CSVFormat defaultFormat = CSVFormat.valueOf("DEFAULT");
        assertEquals(CSVFormat.DEFAULT, defaultFormat);

        CSVFormat excelFormat = CSVFormat.valueOf("EXCEL");
        assertEquals(CSVFormat.EXCEL, excelFormat);
    }

    @Test
    @Timeout(8000)
    public void testEqualsAndHashCode() throws Exception {
        CSVFormat f1 = CSVFormat.DEFAULT.withDelimiter(';').withQuote('\'');
        CSVFormat f2 = CSVFormat.DEFAULT.withDelimiter(';').withQuote('\'');
        CSVFormat f3 = CSVFormat.DEFAULT.withDelimiter(',');

        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
        assertNotEquals(f1, f3);
        assertNotEquals(f1.hashCode(), f3.hashCode());
        assertNotEquals(f1, null);
        assertNotEquals(f1, "some string");
    }

    @Test
    @Timeout(8000)
    public void testFormatMethod() {
        CSVFormat format = CSVFormat.DEFAULT;
        String formatted = format.format("a", "b", "c");
        assertNotNull(formatted);
        assertTrue(formatted.contains("a"));
        assertTrue(formatted.contains("b"));
        assertTrue(formatted.contains("c"));
    }

    @Test
    @Timeout(8000)
    public void testWithMethods() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat updated;

        updated = base.withAllowMissingColumnNames(true);
        assertTrue(updated.getAllowMissingColumnNames());

        updated = base.withCommentMarker('#');
        assertEquals(Character.valueOf('#'), updated.getCommentMarker());

        updated = base.withDelimiter(';');
        assertEquals(';', updated.getDelimiter());

        updated = base.withEscape('\\');
        assertEquals(Character.valueOf('\\'), updated.getEscapeCharacter());

        updated = base.withIgnoreEmptyLines(false);
        assertFalse(updated.getIgnoreEmptyLines());

        updated = base.withIgnoreHeaderCase(true);
        assertTrue(updated.getIgnoreHeaderCase());

        updated = base.withIgnoreSurroundingSpaces(true);
        assertTrue(updated.getIgnoreSurroundingSpaces());

        updated = base.withNullString("null");
        assertEquals("null", updated.getNullString());

        updated = base.withQuote('\'');
        assertEquals(Character.valueOf('\''), updated.getQuoteCharacter());

        updated = base.withQuoteMode(QuoteMode.MINIMAL);
        assertEquals(QuoteMode.MINIMAL, updated.getQuoteMode());

        updated = base.withRecordSeparator("\r\n");
        assertEquals("\r\n", updated.getRecordSeparator());

        updated = base.withSkipHeaderRecord(true);
        assertTrue(updated.getSkipHeaderRecord());

        updated = base.withTrailingDelimiter(true);
        assertTrue(updated.getTrailingDelimiter());

        updated = base.withTrim(true);
        assertTrue(updated.getTrim());
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreakChar() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreakCharacter() throws Exception {
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        assertTrue((Boolean) isLineBreakCharacter.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));
    }

    @Test
    @Timeout(8000)
    public void testToStringArrayPrivateMethod() throws Exception {
        // Prepare input
        Object[] input = new Object[]{"a", "b", null, 123};

        // Access private method
        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        String[] result = (String[]) toStringArray.invoke(null, new Object[]{input});

        assertArrayEquals(new String[]{"a", "b", null, "123"}, result);
    }

    @Test
    @Timeout(8000)
    public void testTrimPrivateMethod() throws Exception {
        Method trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trimMethod.setAccessible(true);

        CharSequence input = "  abc  ";
        CharSequence output = (CharSequence) trimMethod.invoke(CSVFormat.DEFAULT, input);
        assertEquals("abc", output.toString());

        CharSequence nullInput = null;
        CharSequence nullOutput = (CharSequence) trimMethod.invoke(CSVFormat.DEFAULT, nullInput);
        assertNull(nullOutput);
    }

    @Test
    @Timeout(8000)
    public void testValidatePrivateMethod() throws Exception {
        // Create instance with valid parameters and call validate
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat = constructor.newInstance(',', '"', QuoteMode.ALL, '#', '\\',
                false, true, "\n", null, null, null,
                false, false, false, false, false);

        Method validate = CSVFormat.class.getDeclaredMethod("validate");
        validate.setAccessible(true);

        // Should not throw any exception
        validate.invoke(csvFormat);
    }
}