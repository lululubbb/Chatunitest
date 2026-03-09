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

class CSVFormat_33_1Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_validChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat result = format.withCommentMarker(Character.valueOf(commentChar));

        assertNotNull(result);
        assertEquals(Character.valueOf(commentChar), result.getCommentMarker());
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), result.getQuoteMode());
        assertEquals(format.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertArrayEquals(format.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_nullChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentChar = null;

        CSVFormat result = format.withCommentMarker(commentChar);

        assertNotNull(result);
        assertNull(result.getCommentMarker());
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), result.getQuoteMode());
        assertEquals(format.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertArrayEquals(format.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakChar_throwsException() {
        CSVFormat format = CSVFormat.DEFAULT;

        char[] lineBreakChars = {'\r', '\n'};
        for (char c : lineBreakChars) {
            Character commentChar = c;
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> format.withCommentMarker(commentChar));
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_privateMethod() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        Boolean resultCR = (Boolean) isLineBreakChar.invoke(null, '\r');
        Boolean resultLF = (Boolean) isLineBreakChar.invoke(null, '\n');
        Boolean resultOther = (Boolean) isLineBreakChar.invoke(null, 'a');

        assertTrue(resultCR);
        assertTrue(resultLF);
        assertFalse(resultOther);

        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        Boolean resultNull = (Boolean) isLineBreakCharacter.invoke(null, (Character) null);
        Boolean resultCharCR = (Boolean) isLineBreakCharacter.invoke(null, (Character) '\r');
        Boolean resultCharLF = (Boolean) isLineBreakCharacter.invoke(null, (Character) '\n');
        Boolean resultCharOther = (Boolean) isLineBreakCharacter.invoke(null, (Character) 'b');

        assertFalse(resultNull);
        assertTrue(resultCharCR);
        assertTrue(resultCharLF);
        assertFalse(resultCharOther);
    }
}