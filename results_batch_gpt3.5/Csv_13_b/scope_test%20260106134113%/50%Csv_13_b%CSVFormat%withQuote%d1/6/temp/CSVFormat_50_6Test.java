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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class CSVFormat_50_6Test {

    @Test
    @Timeout(8000)
    void testWithQuotePrimitiveChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\'';

        CSVFormat result = original.withQuote(quoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
        // Original remains unchanged
        assertEquals(Character.valueOf('"'), original.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteNullCharacter() throws Exception {
        // Using reflection to access public withQuote(Character) method
        CSVFormat original = CSVFormat.DEFAULT;
        Character quoteChar = null;

        Method withQuoteCharMethod = CSVFormat.class.getMethod("withQuote", Character.class);
        CSVFormat result = (CSVFormat) withQuoteCharMethod.invoke(original, quoteChar);

        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        // Original remains unchanged
        assertEquals(Character.valueOf('"'), original.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteCharacterSameAsOriginal() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Character quoteChar = original.getQuoteCharacter();

        Method withQuoteCharMethod = CSVFormat.class.getMethod("withQuote", Character.class);
        CSVFormat result = (CSVFormat) withQuoteCharMethod.invoke(original, quoteChar);

        // Should be same instance if no change
        assertSame(original, result);
    }

    @Test
    @Timeout(8000)
    void testWithQuoteCharacterDifferent() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Character newQuoteChar = '\'';

        Method withQuoteCharMethod = CSVFormat.class.getMethod("withQuote", Character.class);
        CSVFormat result = (CSVFormat) withQuoteCharMethod.invoke(original, newQuoteChar);

        assertNotNull(result);
        assertEquals(newQuoteChar, result.getQuoteCharacter());
        assertNotSame(original, result);
    }
}