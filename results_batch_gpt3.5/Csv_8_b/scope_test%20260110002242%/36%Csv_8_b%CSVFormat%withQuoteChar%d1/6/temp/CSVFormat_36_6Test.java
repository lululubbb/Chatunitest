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
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Method;

public class CSVFormat_36_6Test {

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_validChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newQuoteChar = '\'';

        CSVFormat updated = original.withQuoteChar(newQuoteChar);

        assertNotSame(original, updated);
        assertEquals(Character.valueOf(newQuoteChar), updated.getQuoteChar());
        // Other fields remain unchanged
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuotePolicy(), updated.getQuotePolicy());
        assertEquals(original.getCommentStart(), updated.getCommentStart());
        assertEquals(original.getEscape(), updated.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_nullChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character newQuoteChar = null;

        CSVFormat updated = original.withQuoteChar(newQuoteChar);

        assertNotSame(original, updated);
        assertNull(updated.getQuoteChar());
        // Other fields remain unchanged
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuotePolicy(), updated.getQuotePolicy());
        assertEquals(original.getCommentStart(), updated.getCommentStart());
        assertEquals(original.getEscape(), updated.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_lineBreakChars() throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;
        Method isLineBreakMethod = clazz.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        char[] lineBreaks = new char[] { '\n', '\r' };

        for (char lb : lineBreaks) {
            // Confirm isLineBreak returns true for these characters
            boolean isLineBreak = (boolean) isLineBreakMethod.invoke(null, Character.valueOf(lb));
            assertTrue(isLineBreak, "Expected isLineBreak to return true for: " + (int) lb);

            CSVFormat original = CSVFormat.DEFAULT;

            char finalLb = lb;
            Executable executable = () -> original.withQuoteChar(finalLb);

            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, executable);
            assertEquals("The quoteChar cannot be a line break", thrown.getMessage());
        }
    }

}