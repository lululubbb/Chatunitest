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

class CSVFormatWithQuoteTest {

    private CSVFormat defaultFormat;

    @BeforeEach
    public void setUp() {
        defaultFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_validChar() {
        char quoteChar = '"';
        CSVFormat newFormat = defaultFormat.withQuote(quoteChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(quoteChar), newFormat.getQuoteCharacter());
        // Other properties remain unchanged
        assertEquals(defaultFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(defaultFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(defaultFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(defaultFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(defaultFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(defaultFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(defaultFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(defaultFormat.getNullString(), newFormat.getNullString());
        assertArrayEquals(defaultFormat.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(defaultFormat.getHeader(), newFormat.getHeader());
        assertEquals(defaultFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(defaultFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(defaultFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_nullQuote() {
        CSVFormat newFormat = defaultFormat.withQuote((Character) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getQuoteCharacter());
        // Other properties remain unchanged
        assertEquals(defaultFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(defaultFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(defaultFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(defaultFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(defaultFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(defaultFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(defaultFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(defaultFormat.getNullString(), newFormat.getNullString());
        assertArrayEquals(defaultFormat.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(defaultFormat.getHeader(), newFormat.getHeader());
        assertEquals(defaultFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(defaultFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(defaultFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_lineBreakCharThrows() throws Exception {
        // Access private static method isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test line break characters that cause exception
        Character[] lineBreaks = { '\n', '\r' };
        for (Character lb : lineBreaks) {
            // Confirm isLineBreak returns true for these chars
            Boolean result = (Boolean) isLineBreakMethod.invoke(null, lb);
            assertTrue(result);

            // Expect IllegalArgumentException when calling withQuote with line break char (char overload)
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
                defaultFormat.withQuote(lb.charValue());
            });
            assertEquals("The quoteChar cannot be a line break", ex.getMessage());

            // Also expect IllegalArgumentException when calling withQuote with Character overload
            ex = assertThrows(IllegalArgumentException.class, () -> {
                defaultFormat.withQuote(lb);
            });
            assertEquals("The quoteChar cannot be a line break", ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_nonLineBreakChars() {
        // Test some chars that are not line breaks
        Character[] chars = { 'a', ' ', '\t', '1', '#', null };
        for (Character ch : chars) {
            if (ch != null) {
                // Should not throw
                CSVFormat newFormatChar = defaultFormat.withQuote(ch.charValue());
                assertNotNull(newFormatChar);
                assertEquals(ch, newFormatChar.getQuoteCharacter());

                CSVFormat newFormatObj = defaultFormat.withQuote(ch);
                assertNotNull(newFormatObj);
                assertEquals(ch, newFormatObj.getQuoteCharacter());
            } else {
                // null case tested above but test here again
                CSVFormat newFormat = defaultFormat.withQuote(ch);
                assertNotNull(newFormat);
                assertNull(newFormat.getQuoteCharacter());
            }
        }
    }
}