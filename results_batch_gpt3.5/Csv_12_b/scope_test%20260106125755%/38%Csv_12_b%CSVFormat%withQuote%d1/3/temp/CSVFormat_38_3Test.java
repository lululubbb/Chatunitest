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

class CSVFormatWithQuoteTest {

    @Test
    @Timeout(8000)
    void testWithQuote_withValidChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character quoteChar = '\'';

        CSVFormat result = original.withQuote(quoteChar);

        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteCharacter());
        // Original should remain unchanged (immutability)
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), original.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withDoubleQuoteChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character quoteChar = '"';

        CSVFormat result = original.withQuote(quoteChar);

        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteCharacter());
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), original.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withNullChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character quoteChar = null;

        CSVFormat result = original.withQuote(quoteChar);

        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), original.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withBackslashChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character quoteChar = '\\';

        CSVFormat result = original.withQuote(quoteChar);

        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteCharacter());
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), original.getQuoteCharacter());
    }
}