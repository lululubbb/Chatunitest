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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatWithQuoteTest {

    private CSVFormat defaultFormat;

    @BeforeEach
    public void setUp() {
        defaultFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_withValidQuoteChar() {
        char quoteChar = '"';
        CSVFormat newFormat = defaultFormat.withQuote(quoteChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(quoteChar), newFormat.getQuoteCharacter());
        // other properties remain unchanged
        assertEquals(defaultFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(defaultFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(defaultFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(defaultFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(defaultFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(defaultFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(defaultFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(defaultFormat.getNullString(), newFormat.getNullString());
        assertArrayEquals(defaultFormat.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(defaultFormat.getHeader(), newFormat.getHeader());
        assertEquals(defaultFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(defaultFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(defaultFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_withNullQuoteChar() {
        CSVFormat newFormat = defaultFormat.withQuote((Character) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getQuoteCharacter());
        // other properties remain unchanged
        assertEquals(defaultFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(defaultFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(defaultFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(defaultFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(defaultFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(defaultFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(defaultFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(defaultFormat.getNullString(), newFormat.getNullString());
        assertArrayEquals(defaultFormat.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(defaultFormat.getHeader(), newFormat.getHeader());
        assertEquals(defaultFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(defaultFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(defaultFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_withLineBreakChar_throwsIllegalArgumentException()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Use reflection to invoke private static method isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // CR char
        Character crChar = Constants.CR;
        assertTrue((Boolean) isLineBreakMethod.invoke(null, crChar));
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> defaultFormat.withQuote(crChar));
        assertEquals("The quoteChar cannot be a line break", thrown.getMessage());

        // LF char
        Character lfChar = Constants.LF;
        assertTrue((Boolean) isLineBreakMethod.invoke(null, lfChar));
        thrown = assertThrows(IllegalArgumentException.class, () -> defaultFormat.withQuote(lfChar));
        assertEquals("The quoteChar cannot be a line break", thrown.getMessage());

        // null is allowed as per method, no exception expected
        Character nullChar = null;
        assertFalse((Boolean) isLineBreakMethod.invoke(null, nullChar));
        CSVFormat newFormat = defaultFormat.withQuote(nullChar);
        assertNotNull(newFormat);
        assertNull(newFormat.getQuoteCharacter());

        // CRLF is a String, not Character, so not tested here
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_privateStaticMethod() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        // Test char version
        assertTrue((Boolean) isLineBreakChar.invoke(null, Constants.CR));
        assertTrue((Boolean) isLineBreakChar.invoke(null, Constants.LF));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, Constants.COMMA));

        // Test Character version
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Constants.CR));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Constants.LF));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, 'a'));
        // For null argument, wrap it in an Object array to avoid varargs warning
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, new Object[] { null }));
    }
}