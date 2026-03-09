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

public class CSVFormat_36_2Test {

    @Test
    @Timeout(8000)
    public void testWithEscapeValidCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat result = format.withEscape(escapeChar);

        assertNotNull(result);
        assertNotSame(format, result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        // Other properties remain unchanged
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), result.getQuoteMode());
        assertEquals(format.getCommentMarker(), result.getCommentMarker());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertArrayEquals(format.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeNullCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat result = format.withEscape((Character) null);

        assertNotNull(result);
        assertNotSame(format, result);
        assertNull(result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeLineBreakCharacters() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        char[] lineBreaks = new char[] {'\n', '\r'};

        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withEscape(lb);
            });
            assertEquals("The escape character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakPrivateMethod() throws Exception {
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Test line break characters return true
        assertTrue((Boolean) isLineBreakMethod.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakMethod.invoke(null, '\r'));

        // Test non-line break characters return false
        assertFalse((Boolean) isLineBreakMethod.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, ','));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, ' '));

        // Test null input with Character overload
        Method isLineBreakMethodChar = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethodChar.setAccessible(true);
        Boolean result = (Boolean) isLineBreakMethodChar.invoke(null, (Character) null);
        assertFalse(result);
    }
}