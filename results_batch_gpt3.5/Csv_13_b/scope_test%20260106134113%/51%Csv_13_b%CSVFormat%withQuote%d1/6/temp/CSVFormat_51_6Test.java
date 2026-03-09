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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_51_6Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_NullQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withQuote((Character) null);
        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteMode(), result.getQuoteMode());
        assertEquals(format.getCommentMarker(), result.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertArrayEquals(format.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_ValidQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = '"';
        CSVFormat result = format.withQuote(quoteChar);
        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteCharacter());
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteMode(), result.getQuoteMode());
        assertEquals(format.getCommentMarker(), result.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertArrayEquals(format.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_LineBreakQuoteChar_ThrowsException() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Using reflection to access private static isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test with line break characters that should cause exception
        Character[] lineBreakChars = {'\n', '\r'};
        for (Character c : lineBreakChars) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withQuote(c);
            });
            assertEquals("The quoteChar cannot be a line break", thrown.getMessage());

            // Also verify the private method returns true for these chars
            Boolean result = (Boolean) isLineBreakMethod.invoke(null, c);
            assertTrue(result);
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_PrivateMethod() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        // Test chars that are line breaks
        char[] lineBreakChars = {'\n', '\r'};
        for (char c : lineBreakChars) {
            boolean resultChar = (boolean) isLineBreakChar.invoke(null, c);
            assertTrue(resultChar);
            boolean resultCharacter = (boolean) isLineBreakCharacter.invoke(null, c);
            assertTrue(resultCharacter);
        }

        // Test chars that are not line breaks
        char[] nonLineBreakChars = {'a', ',', ' '};
        for (char c : nonLineBreakChars) {
            boolean resultChar = (boolean) isLineBreakChar.invoke(null, c);
            assertFalse(resultChar);
            boolean resultCharacter = (boolean) isLineBreakCharacter.invoke(null, c);
            assertFalse(resultCharacter);
        }

        // Test null Character input for isLineBreak(Character)
        Boolean nullResult = (Boolean) isLineBreakCharacter.invoke(null, new Object[]{null});
        assertFalse(nullResult);
    }
}