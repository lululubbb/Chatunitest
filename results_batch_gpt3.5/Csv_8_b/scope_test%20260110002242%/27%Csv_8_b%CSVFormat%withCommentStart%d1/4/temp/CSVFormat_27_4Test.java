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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_27_4Test {

    @Test
    @Timeout(8000)
    public void testWithCommentStart_validChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '#'; // using char
        CSVFormat updated = original.withCommentStart(commentChar);
        assertNotNull(updated);
        assertEquals(Character.valueOf(commentChar), updated.getCommentStart());
        // original should be unchanged
        assertNull(original.getCommentStart());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentStart_nullChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withCommentStart((Character) null);
        assertNotNull(updated);
        assertNull(updated.getCommentStart());
        // original should be unchanged
        assertNull(original.getCommentStart());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentStart_lineBreakChar_throws() {
        CSVFormat original = CSVFormat.DEFAULT;
        char[] lineBreaks = { '\r', '\n' };

        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                original.withCommentStart(lb);
            });
            assertEquals("The comment start character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_privateStaticMethod() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        Method isLineBreakCharacter = null;
        for (Method m : CSVFormat.class.getDeclaredMethods()) {
            if ("isLineBreak".equals(m.getName())
                    && m.getParameterCount() == 1
                    && m.getParameterTypes()[0].equals(Character.class)) {
                isLineBreakCharacter = m;
                break;
            }
        }
        assertNotNull(isLineBreakCharacter);
        isLineBreakCharacter.setAccessible(true);

        // char version
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));

        // Character version with non-null
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\r')));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\n')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('a')));
        // Character version with null
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));
    }
}