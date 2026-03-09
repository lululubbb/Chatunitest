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

public class CSVFormat_2_6Test {

    @Test
    @Timeout(8000)
    public void testIsLineBreak_NullCharacter() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        Boolean result = (Boolean) method.invoke(null, new Object[] { null });
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_ValidLineBreakCharacters() throws Exception {
        Method methodChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        methodChar.setAccessible(true);
        Method methodCharObj = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        methodCharObj.setAccessible(true);

        char[] lineBreakChars = {'\r', '\n'};
        for (char c : lineBreakChars) {
            // test private isLineBreak(char)
            Boolean resultChar = (Boolean) methodChar.invoke(null, c);
            assertTrue(resultChar, "Expected true for line break char: " + (int)c);

            // test private isLineBreak(Character)
            Boolean resultCharObj = (Boolean) methodCharObj.invoke(null, Character.valueOf(c));
            assertTrue(resultCharObj, "Expected true for line break Character: " + (int)c);
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_NonLineBreakCharacters() throws Exception {
        Method methodChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        methodChar.setAccessible(true);
        Method methodCharObj = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        methodCharObj.setAccessible(true);

        char[] nonLineBreakChars = {'a', ',', ' ', '\t', '1', '0'};
        for (char c : nonLineBreakChars) {
            // test private isLineBreak(char)
            Boolean resultChar = (Boolean) methodChar.invoke(null, c);
            assertFalse(resultChar, "Expected false for non-line break char: " + c);

            // test private isLineBreak(Character)
            Boolean resultCharObj = (Boolean) methodCharObj.invoke(null, Character.valueOf(c));
            assertFalse(resultCharObj, "Expected false for non-line break Character: " + c);
        }
    }
}