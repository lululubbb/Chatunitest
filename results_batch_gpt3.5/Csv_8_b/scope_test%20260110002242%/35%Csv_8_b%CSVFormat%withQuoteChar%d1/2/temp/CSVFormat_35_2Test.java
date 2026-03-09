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

class CSVFormat_35_2Test {

    @Test
    @Timeout(8000)
    void testWithQuoteChar_primitiveChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\'';
        CSVFormat result = original.withQuoteChar(quoteChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteChar());
        // original should remain unchanged (immutability)
        assertEquals(CSVFormat.DEFAULT.getQuoteChar(), original.getQuoteChar());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteChar_nullCharacter() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        // Use reflection to invoke public withQuoteChar(Character)
        Method method = CSVFormat.class.getMethod("withQuoteChar", Character.class);
        CSVFormat result = (CSVFormat) method.invoke(original, new Object[] { (Character) null });
        assertNotNull(result);
        assertNull(result.getQuoteChar());
        // original should remain unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteChar(), original.getQuoteChar());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteChar_CharacterObject() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Character quoteChar = Character.valueOf('\"');
        // Use reflection to invoke public withQuoteChar(Character)
        Method method = CSVFormat.class.getMethod("withQuoteChar", Character.class);
        CSVFormat result = (CSVFormat) method.invoke(original, quoteChar);
        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteChar());
        // original should remain unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteChar(), original.getQuoteChar());
    }

}