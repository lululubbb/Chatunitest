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

public class CSVFormat_51_2Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_ValidCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\'';

        CSVFormat result = original.withQuote(quoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_NullCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character quoteChar = null;

        CSVFormat result = original.withQuote(quoteChar);

        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_LineBreakCharacters_ThrowsIllegalArgumentException() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Use reflection to access private static isLineBreak(char) method
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Test with CR character
        char cr = '\r';
        assertTrue((Boolean) isLineBreakMethod.invoke(null, cr));
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> original.withQuote(cr));
        assertEquals("The quoteChar cannot be a line break", ex1.getMessage());

        // Test with LF character
        char lf = '\n';
        assertTrue((Boolean) isLineBreakMethod.invoke(null, lf));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> original.withQuote(lf));
        assertEquals("The quoteChar cannot be a line break", ex2.getMessage());

        // Test with null (should not throw)
        assertDoesNotThrow(() -> original.withQuote((Character) null));
    }
}