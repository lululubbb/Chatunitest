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

class CSVFormatIsLineBreakTest {

    private static Method isLineBreakMethod;

    @BeforeAll
    static void setUp() throws Exception {
        // The method isLineBreak takes a Character object, not primitive char
        isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withLF() throws Exception {
        boolean result = (boolean) isLineBreakMethod.invoke(null, Character.valueOf('\n'));
        assertTrue(result, "LF should be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withCR() throws Exception {
        boolean result = (boolean) isLineBreakMethod.invoke(null, Character.valueOf('\r'));
        assertTrue(result, "CR should be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withOtherChar() throws Exception {
        boolean result = (boolean) isLineBreakMethod.invoke(null, Character.valueOf('a'));
        assertFalse(result, "Non CR/LF char should not be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withTab() throws Exception {
        boolean result = (boolean) isLineBreakMethod.invoke(null, Character.valueOf('\t'));
        assertFalse(result, "Tab should not be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_withSpace() throws Exception {
        boolean result = (boolean) isLineBreakMethod.invoke(null, Character.valueOf(' '));
        assertFalse(result, "Space should not be recognized as line break");
    }
}