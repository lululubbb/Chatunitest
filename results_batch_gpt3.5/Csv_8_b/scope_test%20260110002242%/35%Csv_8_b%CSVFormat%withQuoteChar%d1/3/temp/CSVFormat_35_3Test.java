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

public class CSVFormat_35_3Test {

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_primitiveChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\'';
        CSVFormat result = original.withQuoteChar(quoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteChar());
        // Ensure original is unchanged (immutability)
        assertEquals(CSVFormat.DEFAULT.getQuoteChar(), original.getQuoteChar());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_nullCharacter() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Use reflection to invoke public withQuoteChar(Character)
        Method withQuoteCharCharMethod = CSVFormat.class.getMethod("withQuoteChar", Character.class);

        CSVFormat result = (CSVFormat) withQuoteCharCharMethod.invoke(original, (Object) null);

        assertNotNull(result);
        assertNull(result.getQuoteChar());
        // Original unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteChar(), original.getQuoteChar());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteChar_CharacterObject() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Character quoteChar = '\"';

        // Use reflection to invoke public withQuoteChar(Character)
        Method withQuoteCharCharMethod = CSVFormat.class.getMethod("withQuoteChar", Character.class);

        CSVFormat result = (CSVFormat) withQuoteCharCharMethod.invoke(original, quoteChar);

        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteChar());
        // Original unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteChar(), original.getQuoteChar());
    }
}