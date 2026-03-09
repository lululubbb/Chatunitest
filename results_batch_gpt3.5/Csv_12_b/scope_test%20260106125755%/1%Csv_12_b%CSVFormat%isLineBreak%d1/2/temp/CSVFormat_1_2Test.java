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

    @BeforeAll
    static void setUp() throws NoSuchMethodException {
        // The method parameter type is primitive char, not Character.class
        isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withLF() throws InvocationTargetException, IllegalAccessException {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, '\n');
        assertTrue(result, "LF (\\n) should be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withCR() throws InvocationTargetException, IllegalAccessException {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, '\r');
        assertTrue(result, "CR (\\r) should be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withOtherChar() throws InvocationTargetException, IllegalAccessException {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, 'a');
        assertFalse(result, "'a' should not be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withSpace() throws InvocationTargetException, IllegalAccessException {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, ' ');
        assertFalse(result, "Space should not be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withTab() throws InvocationTargetException, IllegalAccessException {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, '\t');
        assertFalse(result, "Tab should not be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withZeroChar() throws InvocationTargetException, IllegalAccessException {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, '\0');
        assertFalse(result, "Null char should not be recognized as line break");
    }
}