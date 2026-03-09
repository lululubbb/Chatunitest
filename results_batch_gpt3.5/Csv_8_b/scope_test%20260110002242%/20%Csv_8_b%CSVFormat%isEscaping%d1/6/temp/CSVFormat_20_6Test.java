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

class CSVFormat_20_6Test {

    @Test
    @Timeout(8000)
    void testIsEscapingWhenEscapeIsNull() throws Exception {
        // Using DEFAULT which has escape == null
        CSVFormat format = CSVFormat.DEFAULT;

        // forcibly set escape field to null via reflection to ensure test condition
        Field escapeField = CSVFormat.class.getDeclaredField("escape");
        escapeField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(escapeField, escapeField.getModifiers() & ~Modifier.FINAL);

        escapeField.set(format, null);

        assertFalse(format.isEscaping());
    }

    @Test
    @Timeout(8000)
    void testIsEscapingWhenEscapeIsSet() throws Exception {
        // Create a CSVFormat instance with escape character set
        CSVFormat formatWithEscape = CSVFormat.DEFAULT.withEscape('\\');

        // forcibly set escape field to '\\' via reflection to ensure test condition
        Field escapeField = CSVFormat.class.getDeclaredField("escape");
        escapeField.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(escapeField, escapeField.getModifiers() & ~Modifier.FINAL);

        escapeField.set(formatWithEscape, Character.valueOf('\\'));

        assertTrue(formatWithEscape.isEscaping());
    }
}