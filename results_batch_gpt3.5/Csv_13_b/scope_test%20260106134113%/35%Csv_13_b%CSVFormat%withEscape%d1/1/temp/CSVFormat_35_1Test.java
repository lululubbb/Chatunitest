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
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class CSVFormat_35_1Test {

    @Test
    @Timeout(8000)
    @DisplayName("Test withEscape(char) delegates to withEscape(Character) and returns correct CSVFormat")
    public void testWithEscapeChar() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to access public withEscape(char) method
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", char.class);

        // Invoke withEscape(char) with a sample escape char
        char escapeChar = '\\';
        CSVFormat result = (CSVFormat) withEscapeCharMethod.invoke(format, escapeChar);

        // The result should not be null
        assertNotNull(result);

        // The escapeCharacter field of result should be equal to Character.valueOf(escapeChar)
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeCharacterValue = (Character) escapeCharacterField.get(result);

        assertEquals(Character.valueOf(escapeChar), escapeCharacterValue);

        // Also verify that original format remains unchanged (immutable)
        Character originalEscapeCharacter = (Character) escapeCharacterField.get(format);
        assertNotEquals(escapeCharacterValue, originalEscapeCharacter);
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test withEscape(Character) with null returns CSVFormat with null escapeCharacter")
    public void testWithEscapeCharacterNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to access public withEscape(Character) method
        Method withEscapeCharacterMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        // Invoke withEscape(Character) with null
        CSVFormat result = (CSVFormat) withEscapeCharacterMethod.invoke(format, new Object[] { null });

        // The result should not be null
        assertNotNull(result);

        // The escapeCharacter field of result should be null
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeCharacterValue = (Character) escapeCharacterField.get(result);

        assertNull(escapeCharacterValue);
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test withEscape(Character) with non-null escapeCharacter sets correctly")
    public void testWithEscapeCharacterNonNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to access public withEscape(Character) method
        Method withEscapeCharacterMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        Character escapeChar = '\\';

        // Invoke withEscape(Character) with non-null character
        CSVFormat result = (CSVFormat) withEscapeCharacterMethod.invoke(format, escapeChar);

        // The result should not be null
        assertNotNull(result);

        // The escapeCharacter field of result should be equal to escapeChar
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeCharacterValue = (Character) escapeCharacterField.get(result);

        assertEquals(escapeChar, escapeCharacterValue);

        // The other properties remain unchanged (e.g. delimiter)
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char originalDelimiter = delimiterField.getChar(format);
        char resultDelimiter = delimiterField.getChar(result);
        assertEquals(originalDelimiter, resultDelimiter);
    }
}