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

public class CSVFormat_2_4Test {

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
    public void testIsLineBreakChar() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getIsLineBreakCharMethod();

        // Test line break characters
        assertTrue((Boolean) method.invoke(null, '\r'));
        assertTrue((Boolean) method.invoke(null, '\n'));

        // Test non-line break characters
        assertFalse((Boolean) method.invoke(null, 'a'));
        assertFalse((Boolean) method.invoke(null, ','));
        assertFalse((Boolean) method.invoke(null, '\t'));
        assertFalse((Boolean) method.invoke(null, ' '));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getIsLineBreakCharacterMethod();

        // null input returns false
        assertFalse((Boolean) method.invoke(null, (Object) null));

        // line break Characters
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\r')));
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\n')));

        // non-line break Characters
        assertFalse((Boolean) method.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) method.invoke(null, Character.valueOf(',')));
        assertFalse((Boolean) method.invoke(null, Character.valueOf('\t')));
        assertFalse((Boolean) method.invoke(null, Character.valueOf(' ')));
    }
}