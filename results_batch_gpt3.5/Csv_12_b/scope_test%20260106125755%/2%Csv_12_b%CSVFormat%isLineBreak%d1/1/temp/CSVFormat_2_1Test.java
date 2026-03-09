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
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CSVFormat_2_1Test {

    private static Method isLineBreakCharMethod;
    private static Method isLineBreakCharacterMethod;

    @BeforeAll
    public static void setUp() throws Exception {
        Class<?> csvFormatClass = Class.forName("org.apache.commons.csv.CSVFormat");

        isLineBreakCharacterMethod = csvFormatClass.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharacterMethod.setAccessible(true);

        isLineBreakCharMethod = csvFormatClass.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharWithLineBreakChars() throws Exception {
        // CR
        assertTrue((Boolean) isLineBreakCharacterMethod.invoke(null, '\r'));
        // LF
        assertTrue((Boolean) isLineBreakCharacterMethod.invoke(null, '\n'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharWithNonLineBreakChars() throws Exception {
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, ' '));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, ','));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, '\t'));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, (char) 0));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacterWithNull() throws Exception {
        // Passing null Character should return false
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, (Object) null));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacterWithLineBreakCharacters() throws Exception {
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, Character.valueOf('\r')));
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, Character.valueOf('\n')));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacterWithNonLineBreakCharacters() throws Exception {
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, Character.valueOf(' ')));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, Character.valueOf(',')));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, Character.valueOf('\t')));
    }
}