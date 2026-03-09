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

class CSVFormat_20_1Test {

    @Test
    @Timeout(8000)
    void testIsEscaping_whenEscapeIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to set private final field 'escape' to null in a new CSVFormat instance
        Field escapeField = CSVFormat.class.getDeclaredField("escape");
        escapeField.setAccessible(true);

        // Create a new CSVFormat instance with escape set to null using withEscape(null)
        CSVFormat formatWithNullEscape = format.withEscape((Character) null);

        // Verify that escape is null
        assertNull(escapeField.get(formatWithNullEscape));
        assertFalse(formatWithNullEscape.isEscaping());
    }

    @Test
    @Timeout(8000)
    void testIsEscaping_whenEscapeIsSet() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        assertTrue(format.isEscaping());
    }
}