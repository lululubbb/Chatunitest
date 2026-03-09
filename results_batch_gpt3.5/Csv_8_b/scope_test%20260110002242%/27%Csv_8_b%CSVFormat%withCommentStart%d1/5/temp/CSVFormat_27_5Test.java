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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_27_5Test {

    @Test
    @Timeout(8000)
    public void testWithCommentStart_ValidCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat newFormat = format.withCommentStart(commentChar);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentStart());
        // Original format unchanged
        assertNull(format.getCommentStart());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentStart_NullCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentChar = null;

        CSVFormat newFormat = format.withCommentStart(commentChar);

        assertNotNull(newFormat);
        assertNull(newFormat.getCommentStart());
        // Original format unchanged
        assertNull(format.getCommentStart());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentStart_LineBreakCharacter_Throws() {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to access private static method isLineBreak(Character)
        try {
            Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
            isLineBreakMethod.setAccessible(true);

            // Test with line break characters: '\n' and '\r'
            Character lf = '\n';
            Character cr = '\r';

            assertTrue((Boolean) isLineBreakMethod.invoke(null, lf));
            assertTrue((Boolean) isLineBreakMethod.invoke(null, cr));

            // Check that withCommentStart throws IllegalArgumentException for line breaks
            IllegalArgumentException thrownLf = assertThrows(IllegalArgumentException.class, () -> {
                format.withCommentStart(lf);
            });
            assertEquals("The comment start character cannot be a line break", thrownLf.getMessage());

            IllegalArgumentException thrownCr = assertThrows(IllegalArgumentException.class, () -> {
                format.withCommentStart(cr);
            });
            assertEquals("The comment start character cannot be a line break", thrownCr.getMessage());

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_PrivateStaticCharMethod() throws Exception {
        Method isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);

        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, '#'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_PrivateStaticCharacterMethod() throws Exception {
        Method isLineBreakCharObjMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharObjMethod.setAccessible(true);

        assertTrue((Boolean) isLineBreakCharObjMethod.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) isLineBreakCharObjMethod.invoke(null, Character.valueOf('\r')));
        assertFalse((Boolean) isLineBreakCharObjMethod.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakCharObjMethod.invoke(null, Character.valueOf('#')));
        assertFalse((Boolean) isLineBreakCharObjMethod.invoke(null, (Character) null));
    }
}