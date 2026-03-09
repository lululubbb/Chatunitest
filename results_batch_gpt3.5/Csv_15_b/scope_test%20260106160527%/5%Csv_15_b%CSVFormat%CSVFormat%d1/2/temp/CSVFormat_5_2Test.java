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
import java.nio.charset.Charset;
import java.nio.file.Files;
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class CSVFormat_5_2Test {

    private Constructor<CSVFormat> constructor;

    @BeforeEach
    public void setup() throws Exception {
        constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
    }

    private CSVFormat createInstance(
            char delimiter,
            Character quoteChar,
            QuoteMode quoteMode,
            Character commentStart,
            Character escape,
            boolean ignoreSurroundingSpaces,
            boolean ignoreEmptyLines,
            String recordSeparator,
            String nullString,
            Object[] headerComments,
            String[] header,
            boolean skipHeaderRecord,
            boolean allowMissingColumnNames,
            boolean ignoreHeaderCase,
            boolean trim,
            boolean trailingDelimiter,
            boolean autoFlush) throws Exception {
        return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString, headerComments, header, skipHeaderRecord,
                allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter, autoFlush);
    }

    @Test
    @Timeout(8000)
    public void testConstructorAndFields() throws Exception {
        char delimiter = ';';
        Character quoteChar = '"';
        QuoteMode quoteMode = QuoteMode.MINIMAL;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        Object[] headerComments = new Object[] {"comment1", "comment2"};
        String[] header = new String[] {"col1", "col2"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = true;
        boolean ignoreHeaderCase = true;
        boolean trim = true;
        boolean trailingDelimiter = true;
        boolean autoFlush = true;

        CSVFormat format = createInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter, autoFlush);

        // Validate fields via reflection
        Field fDelimiter = CSVFormat.class.getDeclaredField("delimiter");
        fDelimiter.setAccessible(true);
        assertEquals(delimiter, fDelimiter.getChar(format));

        Field fQuoteChar = CSVFormat.class.getDeclaredField("quoteCharacter");
        fQuoteChar.setAccessible(true);
        assertEquals(quoteChar, fQuoteChar.get(format));

        Field fQuoteMode = CSVFormat.class.getDeclaredField("quoteMode");
        fQuoteMode.setAccessible(true);
        assertEquals(quoteMode, fQuoteMode.get(format));

        Field fCommentMarker = CSVFormat.class.getDeclaredField("commentMarker");
        fCommentMarker.setAccessible(true);
        assertEquals(commentStart, fCommentMarker.get(format));

        Field fEscapeCharacter = CSVFormat.class.getDeclaredField("escapeCharacter");
        fEscapeCharacter.setAccessible(true);
        assertEquals(escape, fEscapeCharacter.get(format));

        Field fIgnoreSurroundingSpaces = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        fIgnoreSurroundingSpaces.setAccessible(true);
        assertEquals(ignoreSurroundingSpaces, fIgnoreSurroundingSpaces.getBoolean(format));

        Field fIgnoreEmptyLines = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        fIgnoreEmptyLines.setAccessible(true);
        assertEquals(ignoreEmptyLines, fIgnoreEmptyLines.getBoolean(format));

        Field fRecordSeparator = CSVFormat.class.getDeclaredField("recordSeparator");
        fRecordSeparator.setAccessible(true);
        assertEquals(recordSeparator, fRecordSeparator.get(format));

        Field fNullString = CSVFormat.class.getDeclaredField("nullString");
        fNullString.setAccessible(true);
        assertEquals(nullString, fNullString.get(format));

        Field fHeaderComments = CSVFormat.class.getDeclaredField("headerComments");
        fHeaderComments.setAccessible(true);
        String[] headerCommentsArr = (String[]) fHeaderComments.get(format);
        assertArrayEquals(new String[] {"comment1", "comment2"}, headerCommentsArr);

        Field fHeader = CSVFormat.class.getDeclaredField("header");
        fHeader.setAccessible(true);
        String[] headerArr = (String[]) fHeader.get(format);
        assertArrayEquals(header, headerArr);

        Field fSkipHeaderRecord = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        fSkipHeaderRecord.setAccessible(true);
        assertEquals(skipHeaderRecord, fSkipHeaderRecord.getBoolean(format));

        Field fAllowMissingColumnNames = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        fAllowMissingColumnNames.setAccessible(true);
        assertEquals(allowMissingColumnNames, fAllowMissingColumnNames.getBoolean(format));

        Field fIgnoreHeaderCase = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        fIgnoreHeaderCase.setAccessible(true);
        assertEquals(ignoreHeaderCase, fIgnoreHeaderCase.getBoolean(format));

        Field fTrim = CSVFormat.class.getDeclaredField("trim");
        fTrim.setAccessible(true);
        assertEquals(trim, fTrim.getBoolean(format));

        Field fTrailingDelimiter = CSVFormat.class.getDeclaredField("trailingDelimiter");
        fTrailingDelimiter.setAccessible(true);
        assertEquals(trailingDelimiter, fTrailingDelimiter.getBoolean(format));

        Field fAutoFlush = CSVFormat.class.getDeclaredField("autoFlush");
        fAutoFlush.setAccessible(true);
        assertEquals(autoFlush, fAutoFlush.getBoolean(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateCalled() throws Exception {
        // We cannot directly test validate() but ensure no exception thrown for valid input
        CSVFormat format = createInstance(',', '"', QuoteMode.MINIMAL, null, null,
                false, true, "\r\n", null, null, null,
                false, false, false, false, false, false);
        assertNotNull(format);
    }

    @Test
    @Timeout(8000)
    public void testPredefinedConstants() {
        assertEquals(',', CSVFormat.DEFAULT.getDelimiter());
        assertEquals('"', CSVFormat.DEFAULT.getQuoteCharacter());
        assertEquals("\r\n", CSVFormat.DEFAULT.getRecordSeparator());
        assertFalse(CSVFormat.DEFAULT.getIgnoreSurroundingSpaces());
        assertTrue(CSVFormat.DEFAULT.getIgnoreEmptyLines());

        assertEquals(',', CSVFormat.EXCEL.getDelimiter());
        assertFalse(CSVFormat.EXCEL.getIgnoreEmptyLines());
        assertTrue(CSVFormat.EXCEL.getAllowMissingColumnNames());

        assertEquals('|', CSVFormat.INFORMIX_UNLOAD.getDelimiter());
        assertEquals('\\', CSVFormat.INFORMIX_UNLOAD.getEscapeCharacter());
        assertEquals("\n", CSVFormat.INFORMIX_UNLOAD.getRecordSeparator());

        assertEquals(',', CSVFormat.INFORMIX_UNLOAD_CSV.getDelimiter());
        assertEquals("\n", CSVFormat.INFORMIX_UNLOAD_CSV.getRecordSeparator());

        assertEquals('\t', CSVFormat.MYSQL.getDelimiter());
        assertEquals('\\', CSVFormat.MYSQL.getEscapeCharacter());
        assertFalse(CSVFormat.MYSQL.getIgnoreEmptyLines());
        assertNull(CSVFormat.MYSQL.getQuoteCharacter());
        assertEquals("\\N", CSVFormat.MYSQL.getNullString());
        assertEquals(QuoteMode.ALL_NON_NULL, CSVFormat.MYSQL.getQuoteMode());

        assertEquals(',', CSVFormat.POSTGRESQL_CSV.getDelimiter());
        assertEquals('"', CSVFormat.POSTGRESQL_CSV.getEscapeCharacter());
        assertFalse(CSVFormat.POSTGRESQL_CSV.getIgnoreEmptyLines());
        assertEquals("", CSVFormat.POSTGRESQL_CSV.getNullString());
        assertEquals(QuoteMode.ALL_NON_NULL, CSVFormat.POSTGRESQL_CSV.getQuoteMode());

        assertEquals('\t', CSVFormat.POSTGRESQL_TEXT.getDelimiter());
        assertEquals("\\N", CSVFormat.POSTGRESQL_TEXT.getNullString());

        assertEquals('\t', CSVFormat.TDF.getDelimiter());
        assertTrue(CSVFormat.TDF.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testToStringArrayPrivateMethod() throws Exception {
        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        String[] resultNull = (String[]) toStringArray.invoke(null, new Object[] { null });
        assertNull(resultNull);

        Object[] input = new Object[] { "a", 123, null };
        String[] expected = new String[] { "a", "123", null };
        String[] result = (String[]) toStringArray.invoke(null, new Object[] { input });
        assertArrayEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    public void testTrimPrivateMethod() throws Exception {
        Method trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trimMethod.setAccessible(true);

        String input = "  abc  ";
        CharSequence result = (CharSequence) trimMethod.invoke(null, input);
        assertEquals("abc", result.toString());

        String input2 = "abc";
        CharSequence result2 = (CharSequence) trimMethod.invoke(null, input2);
        assertEquals("abc", result2.toString());

        CharSequence nullResult = (CharSequence) trimMethod.invoke(null, (Object) null);
        assertNull(nullResult);
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
}