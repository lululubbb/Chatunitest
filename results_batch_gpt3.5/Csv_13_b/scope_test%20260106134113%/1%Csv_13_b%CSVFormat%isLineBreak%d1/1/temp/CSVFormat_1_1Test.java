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
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CSVFormat_1_1Test {

    private static Method isLineBreakCharMethod;
    private static Method isLineBreakCharacterMethod;

    @BeforeAll
    static void setUp() throws Exception {
        isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);
        isLineBreakCharacterMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacterMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_char_LF() throws Exception {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, '\n'); // LF
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_char_CR() throws Exception {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, '\r'); // CR
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_char_Other() throws Exception {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, 'a');
        assertFalse(result);
        result = (boolean) isLineBreakCharMethod.invoke(null, ',');
        assertFalse(result);
        result = (boolean) isLineBreakCharMethod.invoke(null, '\t');
        assertFalse(result);
        result = (boolean) isLineBreakCharMethod.invoke(null, ' ');
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_Character_LF() throws Exception {
        Boolean result = (Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('\n'));
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_Character_CR() throws Exception {
        Boolean result = (Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('\r'));
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_Character_Null() throws Exception {
        Boolean result = (Boolean) isLineBreakCharacterMethod.invoke(null, new Object[] { null });
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_Character_Other() throws Exception {
        Boolean result = (Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('x'));
        assertFalse(result);
        result = (Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf(','));
        assertFalse(result);
        result = (Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('\t'));
        assertFalse(result);
    }
}