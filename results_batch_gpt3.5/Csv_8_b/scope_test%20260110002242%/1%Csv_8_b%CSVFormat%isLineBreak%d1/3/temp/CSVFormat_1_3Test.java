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

class CSVFormat_1_3Test {

    @Test
    @Timeout(8000)
    void testIsLineBreak_withLF() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null, Character.valueOf('\n')); // LF
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withCR() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null, Character.valueOf('\r')); // CR
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withOtherChar() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null, Character.valueOf('a'));
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withTab() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null, Character.valueOf('\t'));
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withBackslash() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null, Character.valueOf('\\'));
        assertFalse(result);
    }
}