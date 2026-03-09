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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CSVFormatIsLineBreakTest {

    private static Method isLineBreakCharMethod;
    private static Method isLineBreakCharacterMethod;

    @BeforeAll
    public static void setUp() throws NoSuchMethodException {
        // private static boolean isLineBreak(final char c)
        isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);
        // private static boolean isLineBreak(final Character c)
        isLineBreakCharacterMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacterMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar_withCR() throws InvocationTargetException, IllegalAccessException {
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\r'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar_withLF() throws InvocationTargetException, IllegalAccessException {
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\n'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar_withOtherChar() throws InvocationTargetException, IllegalAccessException {
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, ','));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, ' '));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, '\t'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter_withNull() throws InvocationTargetException, IllegalAccessException {
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, (Object) null));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter_withLineBreakChars() throws InvocationTargetException, IllegalAccessException {
        assertTrue((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('\r')));
        assertTrue((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('\n')));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter_withNonLineBreakChars() throws InvocationTargetException, IllegalAccessException {
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf(' ')));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf(',')));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('\t')));
    }
}