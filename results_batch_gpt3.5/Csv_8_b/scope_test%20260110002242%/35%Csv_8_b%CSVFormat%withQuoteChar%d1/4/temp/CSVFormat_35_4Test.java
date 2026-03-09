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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

public class CSVFormat_35_4Test {

    @Test
    @Timeout(8000)
    void testWithQuoteChar_withPrimitiveChar() {
        CSVFormat original = CSVFormat.DEFAULT;

        // Use a different quote char
        char newQuoteChar = '\'';
        CSVFormat modified = original.withQuoteChar(newQuoteChar);

        assertNotNull(modified);
        assertNotSame(original, modified);
        assertEquals(Character.valueOf(newQuoteChar), modified.getQuoteChar());

        // Setting quote char to the same as original returns same or equal object
        CSVFormat sameQuote = original.withQuoteChar(CSVFormat.DEFAULT.getQuoteChar());
        assertEquals(CSVFormat.DEFAULT.getQuoteChar(), sameQuote.getQuoteChar());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteChar_withNullQuoteChar() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Use reflection to invoke public withQuoteChar(Character) with null
        java.lang.reflect.Method method = CSVFormat.class.getMethod("withQuoteChar", Character.class);
        CSVFormat modified = (CSVFormat) method.invoke(original, new Object[] { (Character) null });

        assertNotNull(modified);
        assertNotSame(original, modified);
        assertNull(modified.getQuoteChar());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteChar_withQuoteCharCharacter() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Use reflection to invoke public withQuoteChar(Character) with a Character
        java.lang.reflect.Method method = CSVFormat.class.getMethod("withQuoteChar", Character.class);
        Character newQuoteChar = '\'';
        CSVFormat modified = (CSVFormat) method.invoke(original, new Object[] { newQuoteChar });

        assertNotNull(modified);
        assertNotSame(original, modified);
        assertEquals(newQuoteChar, modified.getQuoteChar());
    }
}