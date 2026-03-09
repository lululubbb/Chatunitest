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

class CSVFormatWithCommentMarkerTest {

    private CSVFormat baseFormat;

    @BeforeEach
    public void setUp() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_validCharacter() {
        Character commentMarker = '#';
        CSVFormat newFormat = baseFormat.withCommentMarker(commentMarker);
        assertNotNull(newFormat);
        assertEquals(commentMarker, newFormat.getCommentMarker());
        // Ensure other properties are unchanged
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(baseFormat.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(baseFormat.getHeader(), newFormat.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_nullCharacter() {
        CSVFormat newFormat = baseFormat.withCommentMarker((Character) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
        // Other properties remain same
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_lineBreakCharactersThrows() throws Throwable {
        Character[] lineBreaks = new Character[] { '\n', '\r' };

        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        for (Character lb : lineBreaks) {
            // Confirm isLineBreak returns true for these characters
            Boolean result = (Boolean) isLineBreakMethod.invoke(null, lb.charValue());
            assertTrue(result);

            Character commentMarker = lb;
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                baseFormat.withCommentMarker(commentMarker);
            });
            assertEquals("The comment start marker character cannot be a line break", exception.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_charOverload_valid() {
        char commentMarker = '#';
        CSVFormat newFormat = baseFormat.withCommentMarker(commentMarker);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentMarker), newFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_charOverload_lineBreakThrows() {
        char[] lineBreaks = new char[] { '\n', '\r' };
        for (char lb : lineBreaks) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                baseFormat.withCommentMarker(lb);
            });
            assertEquals("The comment start marker character cannot be a line break", exception.getMessage());
        }
    }
}