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

class CSVFormatWithDelimiterTest {

    @Test
    @Timeout(8000)
    public void testWithDelimiter_validDelimiter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        char newDelimiter = ';';
        CSVFormat newFormat = format.withDelimiter(newDelimiter);

        assertNotNull(newFormat);
        assertEquals(newDelimiter, newFormat.getDelimiter());
        // Other properties should remain unchanged
        assertEquals(format.getQuoteChar(), newFormat.getQuoteChar());
        assertEquals(format.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(format.getCommentStart(), newFormat.getCommentStart());
        assertEquals(format.getEscape(), newFormat.getEscape());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_lineBreakDelimiters() throws Exception {
        // Access private static method isLineBreak(char) via reflection
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test line break characters that should cause exception
        char[] lineBreakChars = {'\r', '\n'};

        for (char c : lineBreakChars) {
            assertTrue((Boolean) isLineBreakMethod.invoke(null, Character.valueOf(c)));
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                CSVFormat.DEFAULT.withDelimiter(c);
            });
            assertEquals("The delimiter cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_nonLineBreakCharacters() throws Exception {
        // Access private static method isLineBreak(char) via reflection
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        char[] nonLineBreakChars = {'a', ' ', ',', ';', '\t', '1'};

        for (char c : nonLineBreakChars) {
            assertFalse((Boolean) isLineBreakMethod.invoke(null, Character.valueOf(c)));
            CSVFormat newFormat = CSVFormat.DEFAULT.withDelimiter(c);
            assertEquals(c, newFormat.getDelimiter());
        }
    }
}