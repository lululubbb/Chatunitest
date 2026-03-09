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

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CSVFormat_38_1Test {

    @Test
    @Timeout(8000)
    @DisplayName("Test withQuote(char) delegates to withQuote(Character) and returns correct CSVFormat")
    public void testWithQuoteChar() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Use reflection to get the public withQuote(char) method
        Method withQuoteCharMethod = CSVFormat.class.getDeclaredMethod("withQuote", char.class);
        withQuoteCharMethod.setAccessible(true);

        // Use reflection to get the public withQuote(Character) method
        Method withQuoteCharacterMethod = CSVFormat.class.getDeclaredMethod("withQuote", Character.class);
        withQuoteCharacterMethod.setAccessible(true);

        // Invoke withQuote(char) normally and get result
        CSVFormat resultFromChar = (CSVFormat) withQuoteCharMethod.invoke(original, '"');

        // Invoke withQuote(Character) directly to compare
        CSVFormat expected = (CSVFormat) withQuoteCharacterMethod.invoke(original, Character.valueOf('"'));

        // Assert that both results are equal (same instance or equal content)
        assertEquals(expected, resultFromChar);

        // Also check that the quoteCharacter is set correctly in the result
        assertEquals(Character.valueOf('"'), resultFromChar.getQuoteCharacter());

        // Test with a different quote char
        CSVFormat resultFromChar2 = (CSVFormat) withQuoteCharMethod.invoke(original, '\'');
        CSVFormat expected2 = (CSVFormat) withQuoteCharacterMethod.invoke(original, Character.valueOf('\''));
        assertEquals(expected2, resultFromChar2);
        assertEquals(Character.valueOf('\''), resultFromChar2.getQuoteCharacter());

        // Test with quote char that is same as original (DEFAULT uses DOUBLE_QUOTE_CHAR '"')
        Character defaultQuoteChar = original.getQuoteCharacter();
        char defaultQuoteCharPrimitive = defaultQuoteChar != null ? defaultQuoteChar.charValue() : '\0';

        CSVFormat resultFromChar3 = (CSVFormat) withQuoteCharMethod.invoke(original, defaultQuoteCharPrimitive);
        CSVFormat expected3 = (CSVFormat) withQuoteCharacterMethod.invoke(original, defaultQuoteChar);
        assertEquals(expected3, resultFromChar3);
        assertEquals(defaultQuoteChar, resultFromChar3.getQuoteCharacter());
    }
}