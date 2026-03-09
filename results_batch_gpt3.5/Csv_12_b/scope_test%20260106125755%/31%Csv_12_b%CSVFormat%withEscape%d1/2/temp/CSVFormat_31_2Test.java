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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_31_2Test {

    @Test
    @Timeout(8000)
    void testWithEscapeChar() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        // Call the public withEscape(char) method
        CSVFormat result = original.withEscape(escapeChar);

        // Verify that the result is not the same instance
        assertNotSame(original, result);

        // Use reflection to invoke private withEscape(Character) method and compare
        Method withEscapeCharMethod = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        withEscapeCharMethod.setAccessible(true);
        CSVFormat expected = (CSVFormat) withEscapeCharMethod.invoke(original, escapeChar);

        // The result from public method should equal the result from private method
        assertEquals(expected, result);

        // Verify that escape character is set correctly
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());

        // Also verify original instance is unchanged
        assertNull(original.getEscapeCharacter());
    }
}