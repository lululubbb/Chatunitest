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
import org.junit.jupiter.api.DisplayName;

class CSVFormat_2_4Test {

    private static Method isLineBreakCharMethod;
    private static Method isLineBreakCharacterMethod;

    @BeforeAll
    static void setUp() throws Exception {
        Class<?> clazz = Class.forName("org.apache.commons.csv.CSVFormat");
        isLineBreakCharacterMethod = clazz.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharacterMethod.setAccessible(true);
        isLineBreakCharMethod = clazz.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test isLineBreak(char) with line break characters")
    void testIsLineBreakChar_withLineBreaks() throws Exception {
        assertTrue((Boolean) isLineBreakCharacterMethod.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakCharacterMethod.invoke(null, '\r'));
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test isLineBreak(char) with non-line break characters")
    void testIsLineBreakChar_withNonLineBreaks() throws Exception {
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, ','));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, ' '));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, '\t'));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, '\\'));
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, '\"'));
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test isLineBreak(Character) with null")
    void testIsLineBreakCharacter_withNull() throws Exception {
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, new Object[] { null }));
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test isLineBreak(Character) with line break Characters")
    void testIsLineBreakCharacter_withLineBreaks() throws Exception {
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, Character.valueOf('\r')));
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test isLineBreak(Character) with non-line break Characters")
    void testIsLineBreakCharacter_withNonLineBreaks() throws Exception {
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, Character.valueOf(',')));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, Character.valueOf(' ')));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, Character.valueOf('\t')));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, Character.valueOf('\\')));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, Character.valueOf('\"')));
    }

}