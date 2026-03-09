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
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_5_5Test {

    private Constructor<CSVFormat> constructor;

    @BeforeEach
    public void setUp() throws Exception {
        constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
    }

    private CSVFormat createInstance(char delimiter, Character quoteChar, QuoteMode quoteMode, Character commentStart,
            Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines, String recordSeparator,
            String nullString, Object[] headerComments, String[] header, boolean skipHeaderRecord,
            boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim, boolean trailingDelimiter,
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
        QuoteMode quoteMode = QuoteMode.ALL;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        Object[] headerComments = new Object[] { "comment1", 123 };
        String[] header = new String[] { "col1", "col2" };
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = true;
        boolean ignoreHeaderCase = true;
        boolean trim = true;
        boolean trailingDelimiter = true;
        boolean autoFlush = true;

        CSVFormat format = createInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter, autoFlush);

        // Validate fields via getters
        assertEquals(delimiter, format.getDelimiter());
        assertEquals(quoteChar, format.getQuoteCharacter());
        assertEquals(quoteMode, format.getQuoteMode());
        assertEquals(commentStart, format.getCommentMarker());
        assertEquals(escape, format.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, format.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, format.getIgnoreEmptyLines());
        assertEquals(recordSeparator, format.getRecordSeparator());
        assertEquals(nullString, format.getNullString());
        assertArrayEquals(new String[] { "comment1", "123" }, format.getHeaderComments());
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
    public void testConstructorWithNullHeaderAndNullHeaderComments() throws Exception {
        CSVFormat format = createInstance(',', null, null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false, false);
        assertNull(format.getHeader());
        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    public void testValidateCalled() throws Exception {
        // Use spy to verify validate() is called from constructor
        CSVFormat spyFormat = spy(createInstance(',', null, null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false, false));
        // Cannot verify constructor call, but validate is private and final, so no direct verification
        // Instead, check that object is created and fields are consistent
        assertEquals(',', spyFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testToStringArrayPrivateMethod() throws Exception {
        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        // Null input returns null
        Object result = toStringArray.invoke(null, new Object[] { (Object) null });
        assertNull(result);

        // Empty array returns empty String[]
        result = toStringArray.invoke(null, (Object) new Object[] {});
        assertArrayEquals(new String[0], (String[]) result);

        // Array with mixed types
        Object[] input = new Object[] { "abc", 123, null };
        result = toStringArray.invoke(null, (Object) input);
        assertArrayEquals(new String[] { "abc", "123", null }, (String[]) result);
    }

    @Test
    @Timeout(8000)
    public void testTrimPrivateMethod() throws Exception {
        Method trim = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trim.setAccessible(true);

        // Null input returns null
        Object result = trim.invoke(null, new Object[] { null });
        assertNull(result);

        // No trim when trim flag is false
        CSVFormat format = createInstance(',', null, null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false, false);
        Field trimField = CSVFormat.class.getDeclaredField("trim");
        trimField.setAccessible(true);
        trimField.set(format, false);
        result = trim.invoke(format, (CharSequence) "  abc  ");
        assertEquals("  abc  ", result);

        // Trim when trim flag is true
        trimField.set(format, true);
        result = trim.invoke(format, (CharSequence) "  abc  ");
        assertEquals("abc", result);

        // Trim null returns null
        result = trim.invoke(format, (CharSequence) null);
        assertNull(result);
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