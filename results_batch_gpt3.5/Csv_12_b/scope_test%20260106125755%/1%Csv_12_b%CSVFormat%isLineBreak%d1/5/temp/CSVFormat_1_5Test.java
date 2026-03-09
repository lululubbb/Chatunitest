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

class CSVFormat_1_5Test {

    private static Method isLineBreakCharMethod;

    @BeforeAll
    static void setUp() throws NoSuchMethodException {
        isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakWithLF() throws InvocationTargetException, IllegalAccessException {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, '\n');
        assertTrue(result, "LF (\\n) should be recognized as a line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakWithCR() throws InvocationTargetException, IllegalAccessException {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, '\r');
        assertTrue(result, "CR (\\r) should be recognized as a line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakWithOtherChar() throws InvocationTargetException, IllegalAccessException {
        char[] nonLineBreakChars = {'a', ' ', ',', '\t', '\"', '\\', '0', '1'};
        for (char c : nonLineBreakChars) {
            boolean result = (boolean) isLineBreakCharMethod.invoke(null, c);
            assertFalse(result, "Character '" + c + "' should NOT be recognized as a line break");
        }
    }
}