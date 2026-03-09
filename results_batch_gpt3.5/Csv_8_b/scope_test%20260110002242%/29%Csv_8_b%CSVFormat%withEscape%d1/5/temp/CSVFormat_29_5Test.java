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
import java.lang.reflect.Method;

class CSVFormat_29_5Test {

    @Test
    @Timeout(8000)
    void testWithEscapeChar() throws Exception {
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        // Call public withEscape(char) method
        CSVFormat result = defaultFormat.withEscape('\\');

        // Verify that the returned CSVFormat has escape Character set to '\\'
        assertNotNull(result);
        assertEquals(Character.valueOf('\\'), result.getEscape());

        // Also verify that the original instance is not modified (immutable)
        assertNull(defaultFormat.getEscape());

        // Using reflection to invoke public withEscape(Character) method
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        // Invoke withEscape(Character) with null to test null handling
        CSVFormat resultNull = (CSVFormat) withEscapeCharMethod.invoke(defaultFormat, new Object[] { null });
        assertNotNull(resultNull);
        assertNull(resultNull.getEscape());

        // Invoke withEscape(Character) with a character
        CSVFormat resultChar = (CSVFormat) withEscapeCharMethod.invoke(defaultFormat, new Object[] { Character.valueOf('e') });
        assertNotNull(resultChar);
        assertEquals(Character.valueOf('e'), resultChar.getEscape());
    }
}