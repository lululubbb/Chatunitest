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

public class CSVFormat_31_6Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_char() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        // Call the public withEscape(char) method
        CSVFormat result = original.withEscape(escapeChar);

        // Use reflection to access the private withEscape(Character) method
        Method privateWithEscape = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        privateWithEscape.setAccessible(true);
        CSVFormat reflectedResult = (CSVFormat) privateWithEscape.invoke(original, escapeChar);

        // Validate that both methods produce the same result
        assertNotNull(result);
        assertNotNull(reflectedResult);
        assertEquals(reflectedResult, result);

        // Check that the escape character is set correctly
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());

        // Check that original instance remains unchanged (immutability)
        assertNotEquals(original.getEscapeCharacter(), result.getEscapeCharacter());

        // Test with different escape char
        CSVFormat result2 = original.withEscape('\"');
        assertEquals(Character.valueOf('\"'), result2.getEscapeCharacter());

        // Test with default escape char (null) via private method
        CSVFormat nullEscapeFormat = (CSVFormat) privateWithEscape.invoke(original, (Character) null);
        assertNull(nullEscapeFormat.getEscapeCharacter());
    }
}