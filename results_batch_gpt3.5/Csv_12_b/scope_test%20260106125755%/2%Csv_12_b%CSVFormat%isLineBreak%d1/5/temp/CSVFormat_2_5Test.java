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

public class CSVFormat_2_5Test {

    private static Method isLineBreakCharMethod;
    private static Method isLineBreakCharacterMethod;

    @BeforeAll
    public static void setUp() throws Exception {
        isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);

        isLineBreakCharacterMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacterMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_char_withCR() throws Exception {
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\r'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_char_withLF() throws Exception {
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\n'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_char_withNonLineBreak() throws Exception {
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, ','));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, '\t'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_Character_null() throws Exception {
        // When invoking with null, we must pass an Object array with a single null element
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, new Object[] { null }));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_Character_withLineBreak() throws Exception {
        assertTrue((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('\r')));
        assertTrue((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('\n')));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_Character_withNonLineBreak() throws Exception {
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf(' ')));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf(',')));
    }
}