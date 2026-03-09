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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_2_3Test {

    private static Method getIsLineBreakCharMethod() throws NoSuchMethodException {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);
        return method;
    }

    private static Method getIsLineBreakCharacterMethod() throws NoSuchMethodException {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar_withCR() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getIsLineBreakCharMethod();
        // CR (carriage return) should be line break
        assertTrue((Boolean) method.invoke(null, '\r'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar_withLF() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getIsLineBreakCharMethod();
        // LF (line feed) should be line break
        assertTrue((Boolean) method.invoke(null, '\n'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar_withOtherChar() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getIsLineBreakCharMethod();
        // Other characters should not be line break
        assertFalse((Boolean) method.invoke(null, 'a'));
        assertFalse((Boolean) method.invoke(null, ','));
        assertFalse((Boolean) method.invoke(null, ' '));
        assertFalse((Boolean) method.invoke(null, '\t'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter_withNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getIsLineBreakCharacterMethod();
        // null Character should return false
        assertFalse((Boolean) method.invoke(null, new Object[] { null }));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter_withLineBreakCharacters() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getIsLineBreakCharacterMethod();
        // Characters CR and LF should be line break
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\r')));
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\n')));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter_withNonLineBreakCharacters() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getIsLineBreakCharacterMethod();
        // Characters other than CR and LF should not be line break
        assertFalse((Boolean) method.invoke(null, Character.valueOf('x')));
        assertFalse((Boolean) method.invoke(null, Character.valueOf(' ')));
        assertFalse((Boolean) method.invoke(null, Character.valueOf('\t')));
    }
}