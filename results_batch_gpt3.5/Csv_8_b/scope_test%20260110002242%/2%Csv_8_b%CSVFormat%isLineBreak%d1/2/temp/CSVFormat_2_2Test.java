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

public class CSVFormat_2_2Test {

    @Test
    @Timeout(8000)
    void testIsLineBreakCharacterNull() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        Boolean result = (Boolean) method.invoke(null, new Object[] { null });
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakCharacterLineBreaks() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        Character[] lineBreakChars = {'\r', '\n'};
        for (Character c : lineBreakChars) {
            Boolean result = (Boolean) method.invoke(null, c);
            assertTrue(result, "Expected true for line break char: " + (int) c.charValue());
        }
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakCharacterNonLineBreak() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        Character[] nonLineBreakChars = {'a', ' ', '\t', '0', 'z'};
        for (Character c : nonLineBreakChars) {
            Boolean result = (Boolean) method.invoke(null, c);
            assertFalse(result, "Expected false for non-line break char: " + c);
        }
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakCharLineBreaks() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);
        char[] lineBreakChars = {'\r', '\n'};
        for (char c : lineBreakChars) {
            Boolean result = (Boolean) method.invoke(null, c);
            assertTrue(result, "Expected true for line break char: " + (int) c);
        }
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakCharNonLineBreak() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);
        char[] nonLineBreakChars = {'a', ' ', '\t', '0', 'z'};
        for (char c : nonLineBreakChars) {
            Boolean result = (Boolean) method.invoke(null, c);
            assertFalse(result, "Expected false for non-line break char: " + c);
        }
    }
}