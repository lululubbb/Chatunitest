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

import org.junit.jupiter.api.Test;

public class CSVFormat_36_2Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_validEscapeChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat newFormat = format.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscapeCharacter());
        // Other properties remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(format.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(format.getNullString(), newFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_nullEscapeChar() {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat newFormat = format.withEscape((Character) null);

        assertNotNull(newFormat);
        assertNull(newFormat.getEscapeCharacter());
        // Other properties remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(format.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(format.getNullString(), newFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_lineBreakEscapeCharThrows() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to access private static boolean isLineBreak(char c)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Test with line break characters that should cause exception
        char[] lineBreakChars = new char[] { '\n', '\r' };

        for (char c : lineBreakChars) {
            // Confirm isLineBreak returns true for these chars
            Boolean isLineBreak = (Boolean) isLineBreakMethod.invoke(null, c);
            assertTrue(isLineBreak, "Expected isLineBreak to return true for char: " + (int) c);

            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withEscape(c);
            });
            assertEquals("The escape character cannot be a line break", thrown.getMessage());
        }
    }
}