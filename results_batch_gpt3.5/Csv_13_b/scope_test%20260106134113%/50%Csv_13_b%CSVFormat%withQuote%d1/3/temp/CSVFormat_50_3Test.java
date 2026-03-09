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

class CSVFormat_50_3Test {

    @Test
    @Timeout(8000)
    void testWithQuote_char() {
        // Use a sample quote char different from default
        char quoteChar = '\'';
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat result = format.withQuote(quoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
        // Original format should remain unchanged (immutability)
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_char_nullQuote() {
        // Test with quote char as double quote - should be set properly
        char quoteChar = CSVFormat.DEFAULT.getQuoteCharacter();
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat result = format.withQuote(quoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_char_edgeCases() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to invoke public withQuote(Character) to test null argument
        Method method = CSVFormat.class.getMethod("withQuote", Character.class);

        // Passing null Character should return a CSVFormat with null quoteCharacter
        CSVFormat resultNull = (CSVFormat) method.invoke(format, new Object[] { null });
        assertNotNull(resultNull);
        assertNull(resultNull.getQuoteCharacter());

        // Passing a valid Character
        Character quoteChar = Character.valueOf('\"');
        CSVFormat resultChar = (CSVFormat) method.invoke(format, quoteChar);
        assertNotNull(resultChar);
        assertEquals(quoteChar, resultChar.getQuoteCharacter());
    }
}