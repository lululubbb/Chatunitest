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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_36_4Test {

    @Test
    @Timeout(8000)
    void testWithQuoteChar_validChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char newQuoteChar = '\'';

        CSVFormat newFormat = format.withQuoteChar(newQuoteChar);

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertEquals(Character.valueOf(newQuoteChar), newFormat.getQuoteChar());
        // Other properties remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
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
    void testWithQuoteChar_null() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character newQuoteChar = null;

        CSVFormat newFormat = format.withQuoteChar(newQuoteChar);

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertNull(newFormat.getQuoteChar());
        // Other properties remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
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
    void testWithQuoteChar_lineBreakCharThrows() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test with line break characters: '\n' (LF), '\r' (CR)
        Character lf = '\n';
        Character cr = '\r';

        // Confirm isLineBreak returns true for these characters
        assertTrue((Boolean) isLineBreakMethod.invoke(null, lf));
        assertTrue((Boolean) isLineBreakMethod.invoke(null, cr));

        // Expect IllegalArgumentException for LF
        IllegalArgumentException exLF = assertThrows(IllegalArgumentException.class,
                () -> format.withQuoteChar(lf));
        assertEquals("The quoteChar cannot be a line break", exLF.getMessage());

        // Expect IllegalArgumentException for CR
        IllegalArgumentException exCR = assertThrows(IllegalArgumentException.class,
                () -> format.withQuoteChar(cr));
        assertEquals("The quoteChar cannot be a line break", exCR.getMessage());
    }
}