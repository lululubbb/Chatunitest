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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_51_4Test {

    private CSVFormat defaultFormat;

    @BeforeEach
    public void setUp() {
        defaultFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_NewQuoteCharacter() {
        char newQuote = '\'';
        CSVFormat result = defaultFormat.withQuote(newQuote);
        assertNotNull(result);
        assertEquals(Character.valueOf(newQuote), result.getQuoteCharacter());
        // Other fields remain same as defaultFormat except quoteCharacter
        assertEquals(defaultFormat.getDelimiter(), result.getDelimiter());
        assertEquals(defaultFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(defaultFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(defaultFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(defaultFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(defaultFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(defaultFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(defaultFormat.getNullString(), result.getNullString());
        assertArrayEquals(defaultFormat.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(defaultFormat.getHeader(), result.getHeader());
        assertEquals(defaultFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(defaultFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(defaultFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_NullQuoteCharacter() {
        CSVFormat result = defaultFormat.withQuote((Character) null);
        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        // Other fields remain same as defaultFormat except quoteCharacter
        assertEquals(defaultFormat.getDelimiter(), result.getDelimiter());
        assertEquals(defaultFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(defaultFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(defaultFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(defaultFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(defaultFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(defaultFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(defaultFormat.getNullString(), result.getNullString());
        assertArrayEquals(defaultFormat.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(defaultFormat.getHeader(), result.getHeader());
        assertEquals(defaultFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(defaultFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(defaultFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_ThrowsOnLineBreak() throws Exception {
        // Use reflection to access private static isLineBreak(char) method
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Find a character that is line break: CR, LF
        char cr = '\r';
        char lf = '\n';

        // Verify private method returns true for CR and LF
        assertTrue((Boolean) isLineBreakMethod.invoke(null, cr));
        assertTrue((Boolean) isLineBreakMethod.invoke(null, lf));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, 'a'));

        // Test withQuote throws IllegalArgumentException on CR
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> defaultFormat.withQuote(cr));
        assertEquals("The quoteChar cannot be a line break", ex1.getMessage());

        // Test withQuote throws IllegalArgumentException on LF
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> defaultFormat.withQuote(lf));
        assertEquals("The quoteChar cannot be a line break", ex2.getMessage());
    }
}