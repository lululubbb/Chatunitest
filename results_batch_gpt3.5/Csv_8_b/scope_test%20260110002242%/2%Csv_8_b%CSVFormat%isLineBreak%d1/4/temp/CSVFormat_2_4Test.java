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

class CSVFormat_2_4Test {

    @Test
    @Timeout(8000)
    void testIsLineBreakWithNullCharacter() throws Exception {
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);
        Boolean result = (Boolean) isLineBreakMethod.invoke(null, (Object) null);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakWithLineBreakCharacters() throws Exception {
        Method isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);

        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        char[] lineBreakChars = { '\r', '\n' };
        for (char c : lineBreakChars) {
            // test private isLineBreak(char)
            Boolean resultChar = (Boolean) isLineBreakCharMethod.invoke(null, c);
            assertTrue(resultChar, "Expected true for char: " + (int) c);

            // test private isLineBreak(Character)
            Boolean resultCharObj = (Boolean) isLineBreakMethod.invoke(null, Character.valueOf(c));
            assertTrue(resultCharObj, "Expected true for Character: " + (int) c);
        }
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakWithNonLineBreakCharacters() throws Exception {
        Method isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);

        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        char[] nonLineBreakChars = { 'a', ' ', ',', '\t', '0', '1' };
        for (char c : nonLineBreakChars) {
            // test private isLineBreak(char)
            Boolean resultChar = (Boolean) isLineBreakCharMethod.invoke(null, c);
            assertFalse(resultChar, "Expected false for char: " + c);

            // test private isLineBreak(Character)
            Boolean resultCharObj = (Boolean) isLineBreakMethod.invoke(null, Character.valueOf(c));
            assertFalse(resultCharObj, "Expected false for Character: " + c);
        }
    }
}