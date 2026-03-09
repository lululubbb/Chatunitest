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

public class CSVFormat_1_1Test {

    private static Method isLineBreakMethod;

    @BeforeAll
    public static void setUp() throws Exception {
        // The method parameter type is primitive char, not Character.class
        isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakWithLF() throws Exception {
        assertTrue((Boolean) isLineBreakMethod.invoke(null, '\n'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakWithCR() throws Exception {
        assertTrue((Boolean) isLineBreakMethod.invoke(null, '\r'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakWithOtherChar() throws Exception {
        assertFalse((Boolean) isLineBreakMethod.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, ','));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, ' '));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, '\t'));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, '\\'));
    }
}