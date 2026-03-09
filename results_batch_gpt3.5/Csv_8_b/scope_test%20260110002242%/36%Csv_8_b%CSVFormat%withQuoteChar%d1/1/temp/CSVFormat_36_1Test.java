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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_36_1Test {

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_validChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char newQuoteChar = '\'';

        CSVFormat result = format.withQuoteChar(newQuoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(newQuoteChar), result.getQuoteChar());
        // Other properties should remain the same
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuotePolicy(), result.getQuotePolicy());
        assertEquals(format.getCommentStart(), result.getCommentStart());
        assertEquals(format.getEscape(), result.getEscape());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_nullChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character newQuoteChar = null;

        CSVFormat result = format.withQuoteChar(newQuoteChar);

        assertNotNull(result);
        assertNull(result.getQuoteChar());
        // Other properties should remain the same
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuotePolicy(), result.getQuotePolicy());
        assertEquals(format.getCommentStart(), result.getCommentStart());
        assertEquals(format.getEscape(), result.getEscape());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_lineBreakCharThrows() {
        CSVFormat format = CSVFormat.DEFAULT;

        // Using reflection to get private static method isLineBreak(char)
        try {
            Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
            isLineBreakMethod.setAccessible(true);

            // Test all line break characters that cause exception
            char[] lineBreakChars = new char[] {'\r', '\n'};
            for (char c : lineBreakChars) {
                boolean isLineBreak = (boolean) isLineBreakMethod.invoke(null, c);
                assertTrue(isLineBreak);

                char quoteChar = c;
                IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                        () -> format.withQuoteChar(quoteChar));
                assertEquals("The quoteChar cannot be a line break", thrown.getMessage());
            }
        } catch (ReflectiveOperationException e) {
            fail("Reflection failure: " + e.getMessage());
        }
    }
}