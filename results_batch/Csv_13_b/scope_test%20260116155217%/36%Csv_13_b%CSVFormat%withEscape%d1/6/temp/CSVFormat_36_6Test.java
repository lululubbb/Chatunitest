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

class CSVFormatWithEscapeTest {

    @Test
    @Timeout(8000)
    void testWithEscape_validEscapeCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char escape = '\\';

        CSVFormat result = original.withEscape(escape);

        assertNotNull(result);
        assertEquals(Character.valueOf(escape), result.getEscapeCharacter());
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
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
    void testWithEscape_nullEscapeCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat result = original.withEscape((Character) null);

        assertNotNull(result);
        assertNull(result.getEscapeCharacter());
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
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
    void testWithEscape_escapeIsLineBreak_cr() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        char escape = '\r';

        Method isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);
        boolean isLineBreak = (boolean) isLineBreakCharMethod.invoke(null, escape);
        assertTrue(isLineBreak);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            original.withEscape(escape);
        });
        assertEquals("The escape character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_escapeIsLineBreak_lf() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        char escape = '\n';

        Method isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);
        boolean isLineBreak = (boolean) isLineBreakCharMethod.invoke(null, escape);
        assertTrue(isLineBreak);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            original.withEscape(escape);
        });
        assertEquals("The escape character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_charVariants() throws Exception {
        Method isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);

        assertTrue((boolean) isLineBreakCharMethod.invoke(null, '\r'));
        assertTrue((boolean) isLineBreakCharMethod.invoke(null, '\n'));
        assertFalse((boolean) isLineBreakCharMethod.invoke(null, 'a'));
        assertFalse((boolean) isLineBreakCharMethod.invoke(null, ' '));

        Method isLineBreakCharacterMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacterMethod.setAccessible(true);

        assertTrue((boolean) isLineBreakCharacterMethod.invoke(null, (Character) '\r'));
        assertTrue((boolean) isLineBreakCharacterMethod.invoke(null, (Character) '\n'));
        assertFalse((boolean) isLineBreakCharacterMethod.invoke(null, (Character) 'a'));
        assertFalse((boolean) isLineBreakCharacterMethod.invoke(null, (Character) ' '));
        assertFalse((boolean) isLineBreakCharacterMethod.invoke(null, (Character) null));
    }
}