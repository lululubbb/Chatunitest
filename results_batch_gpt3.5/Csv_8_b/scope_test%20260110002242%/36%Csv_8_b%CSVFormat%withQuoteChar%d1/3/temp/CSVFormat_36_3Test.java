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

public class CSVFormat_36_3Test {

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_validChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char newQuoteChar = '\'';

        CSVFormat result = baseFormat.withQuoteChar(newQuoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(newQuoteChar), result.getQuoteChar());
        // All other fields should remain the same as baseFormat except quoteChar
        assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
        assertEquals(baseFormat.getQuotePolicy(), result.getQuotePolicy());
        assertEquals(baseFormat.getCommentStart(), result.getCommentStart());
        assertEquals(baseFormat.getEscape(), result.getEscape());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), result.getNullString());
        assertArrayEquals(baseFormat.getHeader(), result.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_nullChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        // Use primitive char with special value to indicate null, or use withQuoteChar(Character) via reflection
        // Since withQuoteChar(Character) exists but withQuoteChar(char) does not accept null,
        // we use reflection to call withQuoteChar(Character) with null

        try {
            Method withQuoteCharMethod = CSVFormat.class.getDeclaredMethod("withQuoteChar", Character.class);
            CSVFormat result = (CSVFormat) withQuoteCharMethod.invoke(baseFormat, (Character) null);

            assertNotNull(result);
            assertNull(result.getQuoteChar());
            // Other fields remain same
            assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
            assertEquals(baseFormat.getQuotePolicy(), result.getQuotePolicy());
            assertEquals(baseFormat.getCommentStart(), result.getCommentStart());
            assertEquals(baseFormat.getEscape(), result.getEscape());
            assertEquals(baseFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
            assertEquals(baseFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
            assertEquals(baseFormat.getRecordSeparator(), result.getRecordSeparator());
            assertEquals(baseFormat.getNullString(), result.getNullString());
            assertArrayEquals(baseFormat.getHeader(), result.getHeader());
            assertEquals(baseFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_lineBreakCharsThrows() throws Exception {
        // Use reflection to access private static method isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Characters that are line breaks: '\n' (LF), '\r' (CR)
        Character[] lineBreaks = new Character[] {'\n', '\r'};

        for (Character lb : lineBreaks) {
            // Confirm isLineBreak returns true for these chars
            Boolean isLB = (Boolean) isLineBreakMethod.invoke(null, lb);
            assertTrue(isLB);

            CSVFormat baseFormat = CSVFormat.DEFAULT;
            Character quoteChar = lb;

            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                baseFormat.withQuoteChar(quoteChar);
            });
            assertEquals("The quoteChar cannot be a line break", thrown.getMessage());
        }
    }
}