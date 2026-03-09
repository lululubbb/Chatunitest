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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_9_4Test {

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escape = format.getEscapeCharacter();
        assertNull(escape, "Default format should have null escape character");
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_WithEscapeChar() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape, "Escape character should not be null");
        assertEquals(Character.valueOf('\\'), escape);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_WithEscapeCharacterObject() {
        Character escapeChar = Character.valueOf('!');
        CSVFormat format = CSVFormat.DEFAULT.withEscape(escapeChar);
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape);
        assertEquals(escapeChar, escape);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_WithNullEscape() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        Character escape = format.getEscapeCharacter();
        assertNull(escape);
    }
}