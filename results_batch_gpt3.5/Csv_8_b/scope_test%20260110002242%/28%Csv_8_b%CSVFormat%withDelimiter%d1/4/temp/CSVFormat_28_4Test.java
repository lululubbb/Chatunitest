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

public class CSVFormat_28_4Test {

    @Test
    @Timeout(8000)
    void testWithDelimiter_ValidDelimiter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newDelimiter = ';';

        CSVFormat result = original.withDelimiter(newDelimiter);

        assertNotNull(result);
        assertNotSame(original, result);
        assertEquals(newDelimiter, result.getDelimiter());
        assertEquals(original.getQuoteChar(), result.getQuoteChar());
        assertEquals(original.getQuotePolicy(), result.getQuotePolicy());
        assertEquals(original.getCommentStart(), result.getCommentStart());
        assertEquals(original.getEscape(), result.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_LineBreaksThrowException() throws Exception {
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Test with '\r'
        char cr = '\r';
        boolean resultCr = (boolean) isLineBreakMethod.invoke(null, cr);
        assertTrue(resultCr);

        // Test with '\n'
        char lf = '\n';
        boolean resultLf = (boolean) isLineBreakMethod.invoke(null, lf);
        assertTrue(resultLf);

        // Delimiters that are line breaks must throw
        char[] lineBreakChars = {'\r', '\n'};
        for (char c : lineBreakChars) {
            Executable executable = () -> CSVFormat.DEFAULT.withDelimiter(c);
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, executable);
            assertEquals("The delimiter cannot be a line break", ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_NonLineBreaksDoNotThrow() {
        char[] validDelimiters = {';', ':', '|', 'A', ' '};
        for (char delimiter : validDelimiters) {
            assertDoesNotThrow(() -> {
                CSVFormat result = CSVFormat.DEFAULT.withDelimiter(delimiter);
                assertEquals(delimiter, result.getDelimiter());
            });
        }
    }
}