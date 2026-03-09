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
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Method;

public class CSVFormat_36_1Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_validEscapeCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat newFormat = format.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscapeCharacter());
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_nullEscapeCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escapeChar = null;

        CSVFormat newFormat = format.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertNull(newFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_lineBreakEscapeCharacterThrows() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to access private static method isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test with line break characters that should cause exception
        Character[] lineBreaks = new Character[] {'\r', '\n'};

        for (Character lb : lineBreaks) {
            // Confirm isLineBreak returns true for these characters
            Boolean result = (Boolean) isLineBreakMethod.invoke(null, lb);
            assertTrue(result);

            // Expect IllegalArgumentException when passing line break as escape
            Character escapeChar = lb;
            Executable exec = () -> format.withEscape(escapeChar);
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, exec);
            assertEquals("The escape character cannot be a line break", thrown.getMessage());
        }
    }
}