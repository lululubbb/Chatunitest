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

class CSVFormatWithCommentMarkerTest {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_validCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentMarker = '#';

        CSVFormat result = original.withCommentMarker(commentMarker);

        assertNotNull(result);
        assertEquals(Character.valueOf(commentMarker), result.getCommentMarker());
        // Ensure original is unchanged (immutability)
        assertNull(original.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_nullCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character commentMarker = null;

        CSVFormat result = original.withCommentMarker(commentMarker);

        assertNotNull(result);
        assertNull(result.getCommentMarker());
        // Ensure original is unchanged (immutability)
        assertNull(original.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_lineBreakCharactersThrows() {
        CSVFormat original = CSVFormat.DEFAULT;

        char[] lineBreaks = new char[] { '\n', '\r' };

        for (char lb : lineBreaks) {
            Executable executable = () -> original.withCommentMarker(lb);
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, executable);
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreak_char() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);

        // Test line break chars return true
        assertTrue((Boolean) method.invoke(null, '\n'));
        assertTrue((Boolean) method.invoke(null, '\r'));

        // Test non-line break chars return false
        assertFalse((Boolean) method.invoke(null, 'a'));
        assertFalse((Boolean) method.invoke(null, ','));
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreak_Character() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);

        // Test line break chars return true
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\r')));

        // Test null returns false
        assertFalse((Boolean) method.invoke(null, (Character) null));

        // Test non-line break chars return false
        assertFalse((Boolean) method.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) method.invoke(null, Character.valueOf(',')));
    }
}