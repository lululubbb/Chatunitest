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

class CSVFormat_21_3Test {

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSetWhenEscapeCharacterIsNull() throws Exception {
        // Create a new CSVFormat instance with escapeCharacter null using withEscape((Character) null)
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);

        // Confirm escapeCharacter is null via the public getter (no need for reflection)
        Character escapeChar = format.getEscapeCharacter();
        assertNull(escapeChar, "Escape character should be null");

        // Invoke isEscapeCharacterSet
        boolean result = format.isEscapeCharacterSet();

        assertFalse(result, "Escape character is null, should return false");
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSetWhenEscapeCharacterIsSet() throws Exception {
        // Create CSVFormat instance with escapeCharacter set via withEscape method
        CSVFormat format = CSVFormat.DEFAULT.withEscape(Character.valueOf('\\'));

        // Use the public getter to get escapeCharacter field value
        Character escapeChar = format.getEscapeCharacter();

        assertNotNull(escapeChar, "Escape character should not be null");

        // Invoke isEscapeCharacterSet
        boolean result = format.isEscapeCharacterSet();

        assertTrue(result, "Escape character is set, should return true");
    }
}