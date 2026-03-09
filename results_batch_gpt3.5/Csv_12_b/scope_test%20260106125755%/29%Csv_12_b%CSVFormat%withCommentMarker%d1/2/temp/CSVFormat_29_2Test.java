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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class CSVFormat_29_2Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_validChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentMarker = '#';

        CSVFormat newFormat = format.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentMarker), newFormat.getCommentMarker());
        // Original format remains unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_nullCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat newFormat = format.withCommentMarker((Character) null);

        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
        // Original format remains unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_lineBreakChar_throws() {
        CSVFormat format = CSVFormat.DEFAULT;

        // Line break characters to test
        char[] lineBreaks = {'\n', '\r'};

        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                    () -> format.withCommentMarker(lb),
                    "Expected IllegalArgumentException for line break char: " + lb);
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_lineBreakCharacterObject_throws() {
        CSVFormat format = CSVFormat.DEFAULT;

        Character[] lineBreaks = {'\n', '\r'};

        for (Character lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                    () -> format.withCommentMarker(lb),
                    "Expected IllegalArgumentException for line break Character: " + lb);
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakPrivateMethod_char() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(null, '\n'));
        assertTrue((Boolean) method.invoke(null, '\r'));
        assertFalse((Boolean) method.invoke(null, 'a'));
        assertFalse((Boolean) method.invoke(null, '#'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakPrivateMethod_Character() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(null, (Character) '\n'));
        assertTrue((Boolean) method.invoke(null, (Character) '\r'));
        assertFalse((Boolean) method.invoke(null, (Character) 'a'));
        assertFalse((Boolean) method.invoke(null, (Character) '#'));
        assertFalse((Boolean) method.invoke(null, new Object[] { null }));
    }
}