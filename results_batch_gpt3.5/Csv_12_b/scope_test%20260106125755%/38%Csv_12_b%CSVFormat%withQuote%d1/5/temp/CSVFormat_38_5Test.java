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

public class CSVFormat_38_5Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_char() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Invoke public withQuote(char) method
        CSVFormat result = original.withQuote('"');
        assertNotNull(result);
        assertEquals(Character.valueOf('"'), result.getQuoteCharacter());

        // Test with null character (edge case)
        CSVFormat resultNull = original.withQuote('\0');
        assertNotNull(resultNull);
        assertEquals(Character.valueOf('\0'), resultNull.getQuoteCharacter());

        // Test that withQuote(char) calls withQuote(Character) internally using reflection
        Method withQuoteCharMethod = CSVFormat.class.getMethod("withQuote", char.class);
        CSVFormat invokedResult = (CSVFormat) withQuoteCharMethod.invoke(original, '\'');
        assertNotNull(invokedResult);
        assertEquals(Character.valueOf('\''), invokedResult.getQuoteCharacter());

        // Also test withQuote(Character) directly via reflection
        Method withQuoteCharacterMethod = CSVFormat.class.getMethod("withQuote", Character.class);
        CSVFormat directResult = (CSVFormat) withQuoteCharacterMethod.invoke(original, Character.valueOf('\''));
        assertNotNull(directResult);
        assertEquals(Character.valueOf('\''), directResult.getQuoteCharacter());

        // Test withQuote(null) via reflection - must use Object[] to avoid varargs ambiguity
        CSVFormat nullQuoteResult = (CSVFormat) withQuoteCharacterMethod.invoke(original, new Object[] { null });
        assertNotNull(nullQuoteResult);
        assertNull(nullQuoteResult.getQuoteCharacter());
    }
}