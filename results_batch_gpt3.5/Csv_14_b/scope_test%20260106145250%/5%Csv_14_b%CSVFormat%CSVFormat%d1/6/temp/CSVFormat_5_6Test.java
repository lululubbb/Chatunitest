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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class CSVFormat_5_6Test {

    private Constructor<CSVFormat> constructor;

    @BeforeEach
    public void setUp() throws Exception {
        constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
    }

    private CSVFormat createInstance(char delimiter, Character quoteChar, QuoteMode quoteMode,
                                     Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
                                     boolean ignoreEmptyLines, String recordSeparator, String nullString,
                                     Object[] headerComments, String[] header, boolean skipHeaderRecord,
                                     boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim,
                                     boolean trailingDelimiter) throws Exception {
        return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testConstructorAndGetters() throws Exception {
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
        String[] header = new String[]{"col1", "col2"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = true;
        boolean ignoreHeaderCase = true;
        boolean trim = true;
        boolean trailingDelimiter = true;

        CSVFormat format = createInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter);

        assertEquals(delimiter, format.getDelimiter());
        assertEquals(quoteChar, format.getQuoteCharacter());
        assertEquals(quoteMode, format.getQuoteMode());
        assertEquals(commentStart, format.getCommentMarker());
        assertEquals(escape, format.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, format.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, format.getIgnoreEmptyLines());
        assertEquals(recordSeparator, format.getRecordSeparator());
        assertEquals(nullString, format.getNullString());
        assertArrayEquals(new String[]{"comment1", "comment2"}, format.getHeaderComments());
        assertArrayEquals(header, format.getHeader());
        assertEquals(skipHeaderRecord, format.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, format.getAllowMissingColumnNames());
        assertEquals(ignoreHeaderCase, format.getIgnoreHeaderCase());
        assertEquals(trim, format.getTrim());
        assertEquals(trailingDelimiter, format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testConstructorNullHeaderAndHeaderComments() throws Exception {
        CSVFormat format = createInstance(',', null, null, null, null,
                false, true, "\r\n", null,
                null, null, false, false,
                false, false, false);

        assertNull(format.getHeader());
        assertNotNull(format.getHeaderComments());
        assertEquals(0, format.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    public void testConstructorInvalidDelimiterThrows() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            createInstance('\n', null, null, null, null,
                    false, true, "\r\n", null,
                    null, null, false, false,
                    false, false, false);
        });
        assertTrue(exception.getMessage().contains("The delimiter cannot be a line break"));
    }

    @Test
    @Timeout(8000)
    public void testEqualsAndHashCode() throws Exception {
        CSVFormat format1 = createInstance(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, false, "\n", "NULL",
                new Object[]{"c1"}, new String[]{"h1", "h2"}, true, true,
                true, true, true);

        CSVFormat format2 = createInstance(',', '"', QuoteMode.MINIMAL, '#', '\\',
                true, false, "\n", "NULL",
                new Object[]{"c1"}, new String[]{"h1", "h2"}, true, true,
                true, true, true);

        CSVFormat format3 = createInstance(';', '"', QuoteMode.MINIMAL, '#', '\\',
                true, false, "\n", "NULL",
                new Object[]{"c1"}, new String[]{"h1", "h2"}, true, true,
                true, true, true);

        assertEquals(format1, format2);
        assertEquals(format1.hashCode(), format2.hashCode());
        assertNotEquals(format1, format3);
    }

    @Test
    @Timeout(8000)
    public void testStaticConstants() {
        assertEquals(',', CSVFormat.DEFAULT.getDelimiter());
        assertEquals('"', CSVFormat.DEFAULT.getQuoteCharacter());
        assertFalse(CSVFormat.DEFAULT.getIgnoreSurroundingSpaces());
        assertTrue(CSVFormat.DEFAULT.getIgnoreEmptyLines());
        assertEquals("\r\n", CSVFormat.DEFAULT.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testNewFormat() {
        CSVFormat format = CSVFormat.newFormat(';');
        assertEquals(';', format.getDelimiter());
        assertNull(format.getQuoteCharacter());
        assertNull(format.getQuoteMode());
        assertNull(format.getCommentMarker());
        assertNull(format.getEscapeCharacter());
        assertFalse(format.getIgnoreSurroundingSpaces());
        assertTrue(format.getIgnoreEmptyLines());
        assertEquals("\r\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testValueOfKnown() {
        CSVFormat format = CSVFormat.valueOf("DEFAULT");
        assertNotNull(format);
        assertEquals(CSVFormat.DEFAULT, format);
    }

    @Test
    @Timeout(8000)
    public void testValueOfUnknown() {
        assertThrows(IllegalArgumentException.class, () -> {
            CSVFormat.valueOf("UNKNOWN");
        });
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter() throws Exception {
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));
    }

    @Test
    @Timeout(8000)
    public void testToStringArray() throws Exception {
        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        Object[] input = new Object[]{"a", 1, null};
        String[] result = (String[]) toStringArray.invoke(createInstance(',', null, null, null, null,
                false, true, "\r\n", null, null, null,
                false, false, false, false, false), new Object[]{input});
        assertArrayEquals(new String[]{"a", "1", null}, result);

        String[] emptyResult = (String[]) toStringArray.invoke(createInstance(',', null, null, null, null,
                false, true, "\r\n", null, null, null,
                false, false, false, false, false), new Object[]{null});
        assertNotNull(emptyResult);
        assertEquals(0, emptyResult.length);
    }

    @Test
    @Timeout(8000)
    public void testValidate() throws Exception {
        Method validate = CSVFormat.class.getDeclaredMethod("validate");
        validate.setAccessible(true);

        CSVFormat validFormat = createInstance(',', '"', null, null, null,
                false, true, "\r\n", null, null, null,
                false, false, false, false, false);
        validate.invoke(validFormat);

        CSVFormat invalidDelimiterFormat = createInstance('\n', null, null, null, null,
                false, true, "\r\n", null, null, null,
                false, false, false, false, false);
        Exception exception = assertThrows(Exception.class, () -> validate.invoke(invalidDelimiterFormat));
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertTrue(exception.getCause().getMessage().contains("The delimiter cannot be a line break"));
    }

}