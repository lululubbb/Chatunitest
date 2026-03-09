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

class CSVFormat_1_1Test {

    private static Method isLineBreakCharMethod;

    @BeforeAll
    static void setUp() throws Exception {
        isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakWithLF() throws Exception {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, Character.valueOf('\n'));
        assertTrue(result, "LF should be recognized as a line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakWithCR() throws Exception {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, Character.valueOf('\r'));
        assertTrue(result, "CR should be recognized as a line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakWithOtherChars() throws Exception {
        char[] nonLineBreakChars = {'a', ' ', '\t', ',', '"', '\\', '0', '1'};
        for (char c : nonLineBreakChars) {
            boolean result = (boolean) isLineBreakCharMethod.invoke(null, Character.valueOf(c));
            assertFalse(result, "Character '" + c + "' should NOT be recognized as a line break");
        }
    }
}