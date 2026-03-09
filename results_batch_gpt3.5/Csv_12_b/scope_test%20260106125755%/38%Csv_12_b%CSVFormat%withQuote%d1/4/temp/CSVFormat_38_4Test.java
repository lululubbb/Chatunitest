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

public class CSVFormat_38_4Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_char() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\'';

        CSVFormat result = original.withQuote(quoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
        // Original should remain unchanged (immutability)
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), original.getQuoteCharacter());
        // Also test with quoteChar as DOUBLE_QUOTE_CHAR
        CSVFormat result2 = original.withQuote(CSVFormat.DEFAULT.getQuoteCharacter());
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), result2.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_Character_null() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Access public withQuote(Character) method
        Method withQuoteCharMethod = CSVFormat.class.getMethod("withQuote", Character.class);

        // Test with null Character
        CSVFormat result = (CSVFormat) withQuoteCharMethod.invoke(original, new Object[] { null });
        assertNotNull(result);
        assertNull(result.getQuoteCharacter());

        // Test with non-null Character
        Character quoteChar = '\"';
        CSVFormat result2 = (CSVFormat) withQuoteCharMethod.invoke(original, quoteChar);
        assertNotNull(result2);
        assertEquals(quoteChar, result2.getQuoteCharacter());

        // Test with different quote char
        Character quoteChar2 = '\'';
        CSVFormat result3 = (CSVFormat) withQuoteCharMethod.invoke(original, quoteChar2);
        assertNotNull(result3);
        assertEquals(quoteChar2, result3.getQuoteCharacter());

        // Test that original remains unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), original.getQuoteCharacter());
    }
}