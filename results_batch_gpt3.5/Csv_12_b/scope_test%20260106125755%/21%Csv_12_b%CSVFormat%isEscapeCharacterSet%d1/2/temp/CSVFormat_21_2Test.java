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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class CSVFormat_21_2Test {

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_whenEscapeCharacterIsNull() throws Exception {
        // Using default CSVFormat.DEFAULT which has escapeCharacter == null
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_whenEscapeCharacterIsSet() throws Exception {
        // Using withEscape to create a new CSVFormat with escapeCharacter set
        CSVFormat formatWithEscape = CSVFormat.DEFAULT.withEscape('\\');
        assertTrue(formatWithEscape.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_reflectionAccess() throws Exception {
        // Create CSVFormat with escapeCharacter set to null via withEscape
        CSVFormat format = CSVFormat.DEFAULT.withEscape(null);

        // Use reflection to set private final field escapeCharacter to non-null Character
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(escapeCharacterField, escapeCharacterField.getModifiers() & ~Modifier.FINAL);

        // Set escapeCharacter to '\\' (Character object, not char primitive)
        escapeCharacterField.set(format, Character.valueOf('\\'));
        assertTrue(format.isEscapeCharacterSet());

        // Set escapeCharacter to null again
        escapeCharacterField.set(format, null);
        assertFalse(format.isEscapeCharacterSet());
    }
}