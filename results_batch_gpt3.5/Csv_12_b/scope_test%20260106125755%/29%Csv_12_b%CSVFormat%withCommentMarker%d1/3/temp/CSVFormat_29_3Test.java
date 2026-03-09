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

public class CSVFormat_29_3Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_validChar() {
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
    void testWithCommentMarker_null() {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat newFormat = format.withCommentMarker((Character) null);

        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
        // Original format remains unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakChar_throws() {
        CSVFormat format = CSVFormat.DEFAULT;
        char[] lineBreaks = {'\n', '\r'};

        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withCommentMarker(lb);
            });
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreak_char() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, '#'));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreak_Character() throws Exception {
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\r')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('#')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));
    }
}