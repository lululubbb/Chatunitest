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

public class CSVFormat_29_2Test {

    @Test
    @Timeout(8000)
    public void testWithEscapeChar() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Invoke public method withEscape(char)
        CSVFormat result = original.withEscape('\\');

        // The withEscape(char) calls withEscape(Character), so result should not be original
        assertNotSame(original, result);

        // Using reflection to invoke public withEscape(Character) method
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        CSVFormat result2 = (CSVFormat) withEscapeCharMethod.invoke(original, Character.valueOf('\\'));

        assertNotSame(original, result2);

        // The escape character should be set to '\\'
        assertEquals(Character.valueOf('\\'), result.getEscape());
        assertEquals(Character.valueOf('\\'), result2.getEscape());

        // Check that original's escape is still null (DEFAULT has null escape)
        assertNull(original.getEscape());

        // Test withEscape with different escape character
        CSVFormat result3 = original.withEscape('\"');
        assertEquals(Character.valueOf('\"'), result3.getEscape());

        // Test withEscape with special character (e.g. comma)
        CSVFormat result4 = original.withEscape(',');
        assertEquals(Character.valueOf(','), result4.getEscape());

        // Test withEscape with a character that might be used as escape in other formats
        CSVFormat result5 = original.withEscape('A');
        assertEquals(Character.valueOf('A'), result5.getEscape());
    }
}