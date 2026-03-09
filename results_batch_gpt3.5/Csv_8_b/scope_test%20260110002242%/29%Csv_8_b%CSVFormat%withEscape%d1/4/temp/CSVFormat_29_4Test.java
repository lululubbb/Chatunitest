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

public class CSVFormat_29_4Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_char() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        // Invoke public withEscape(char) method
        CSVFormat result = original.withEscape(escapeChar);

        // Verify that the returned CSVFormat has the correct escape character
        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscape());

        // Verify that original CSVFormat is unchanged (immutability)
        assertNull(original.getEscape());

        // Check that calling withEscape on a CSVFormat with same escape returns equal object but not same instance
        CSVFormat sameEscape = result.withEscape(escapeChar);
        assertEquals(result, sameEscape);
        assertNotSame(result, sameEscape);

        // Use reflection to invoke public withEscape(Character) method to cover that branch as well
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);
        CSVFormat fromReflection = (CSVFormat) withEscapeCharMethod.invoke(original, escapeChar);
        assertNotNull(fromReflection);
        assertEquals(Character.valueOf(escapeChar), fromReflection.getEscape());
    }
}