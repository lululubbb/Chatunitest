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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class CSVFormat_9_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        // Use the predefined DEFAULT instance
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testGetEscape_DefaultNull() {
        // By default, escape is null in CSVFormat.DEFAULT
        assertNull(csvFormat.getEscape());
    }

    @Test
    @Timeout(8000)
    void testGetEscape_WithEscapeChar() {
        // Create CSVFormat instance with escape character set via withEscape(Character)
        CSVFormat formatWithEscape = csvFormat.withEscape('\\');
        assertNotNull(formatWithEscape.getEscape());
        assertEquals(Character.valueOf('\\'), formatWithEscape.getEscape());
    }

    @Test
    @Timeout(8000)
    void testGetEscape_ReflectionSetEscape() throws Exception {
        // Use reflection to set private final field 'escape' to a specific value and test getEscape()
        Field escapeField = CSVFormat.class.getDeclaredField("escape");
        escapeField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(escapeField, escapeField.getModifiers() & ~Modifier.FINAL);

        // Create a new CSVFormat instance with escape = null to allow modification
        CSVFormat format = csvFormat.withEscape((Character) null);

        // Set escape field to 'e'
        escapeField.set(format, Character.valueOf('e'));

        Character escape = format.getEscape();
        assertNotNull(escape);
        assertEquals(Character.valueOf('e'), escape);
    }

    @Test
    @Timeout(8000)
    void testGetEscape_NullEscape() {
        // Create format with escape explicitly set to null
        CSVFormat formatNullEscape = csvFormat.withEscape((Character) null);
        assertNull(formatNullEscape.getEscape());
    }
}