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

public class CSVFormat_36_3Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_validEscapeCharacter() throws Exception {
        // Arrange
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        // Act
        CSVFormat result = defaultFormat.withEscape('e');

        // Assert
        assertNotNull(result);
        assertEquals(Character.valueOf('e'), result.getEscapeCharacter());
        assertEquals(defaultFormat.getDelimiter(), result.getDelimiter());
        assertEquals(defaultFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(defaultFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(defaultFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(defaultFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(defaultFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(defaultFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(defaultFormat.getNullString(), result.getNullString());
        assertArrayEquals(defaultFormat.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(defaultFormat.getHeader(), result.getHeader());
        assertEquals(defaultFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(defaultFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(defaultFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_nullEscapeCharacter() throws Exception {
        // Arrange
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        // Act
        CSVFormat result = defaultFormat.withEscape((Character) null);

        // Assert
        assertNotNull(result);
        assertNull(result.getEscapeCharacter());
        assertEquals(defaultFormat.getDelimiter(), result.getDelimiter());
        assertEquals(defaultFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(defaultFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(defaultFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(defaultFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(defaultFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(defaultFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(defaultFormat.getNullString(), result.getNullString());
        assertArrayEquals(defaultFormat.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(defaultFormat.getHeader(), result.getHeader());
        assertEquals(defaultFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(defaultFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(defaultFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_lineBreakEscapeCharacter_throwsIllegalArgumentException() throws Exception {
        // Arrange
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        // Reflection to access private static method isLineBreak(char)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Verify that line break characters cause exception
        char[] lineBreakChars = new char[] { '\n', '\r' };

        for (char lineBreak : lineBreakChars) {
            // Confirm isLineBreak returns true for lineBreak
            boolean isLineBreak = (boolean) isLineBreakMethod.invoke(null, lineBreak);
            assertTrue(isLineBreak, "Expected isLineBreak to return true for character: " + (int) lineBreak);

            // Act & Assert
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                defaultFormat.withEscape(lineBreak);
            });
            assertEquals("The escape character cannot be a line break", thrown.getMessage());
        }
    }

}