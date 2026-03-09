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

public class CSVFormat_2_1Test {

    private static Method isLineBreakCharMethod;
    private static Method isLineBreakCharacterMethod;

    @BeforeAll
    public static void setUp() throws Exception {
        // Access private static method isLineBreak(char)
        isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);

        // Access private static method isLineBreak(Character)
        isLineBreakCharacterMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacterMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar_withCR() throws Exception {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, '\r');
        assertTrue(result, "CR should be recognized as line break");
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar_withLF() throws Exception {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, '\n');
        assertTrue(result, "LF should be recognized as line break");
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar_withNonLineBreak() throws Exception {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, 'a');
        assertFalse(result, "Non line break char should not be recognized");
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter_withNull() throws Exception {
        // For null, pass single null argument as Object (varargs)
        boolean result = (boolean) isLineBreakCharacterMethod.invoke(null, (Object) null);
        assertFalse(result, "Null Character should return false");
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter_withLineBreakChar() throws Exception {
        Character cr = '\r';
        boolean result = (boolean) isLineBreakCharacterMethod.invoke(null, cr);
        assertTrue(result, "Character CR should be recognized as line break");
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter_withNonLineBreakChar() throws Exception {
        Character c = 'x';
        boolean result = (boolean) isLineBreakCharacterMethod.invoke(null, c);
        assertFalse(result, "Character 'x' should not be recognized as line break");
    }
}