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

class CSVFormat_50_2Test {

    @Test
    @Timeout(8000)
    void testWithQuote_primitiveChar() {
        CSVFormat original = CSVFormat.DEFAULT;

        // Call withQuote(char) which delegates to withQuote(Character)
        CSVFormat result = original.withQuote('\'');
        assertNotNull(result);
        assertNotSame(original, result);
        assertEquals(Character.valueOf('\''), result.getQuoteCharacter());

        // Also test withQuote with null character (edge case)
        CSVFormat resultNull = original.withQuote('\0');
        assertNotNull(resultNull);
        assertNotSame(original, resultNull);
        assertEquals(Character.valueOf('\0'), resultNull.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_Character_null() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Use reflection to access public withQuote(Character) method
        Method method = CSVFormat.class.getMethod("withQuote", Character.class);

        // Test with null Character
        CSVFormat result = (CSVFormat) method.invoke(original, new Object[]{null});
        assertNotNull(result);
        assertNotSame(original, result);
        assertNull(result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_Character_nonNull() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Use reflection to access public withQuote(Character) method
        Method method = CSVFormat.class.getMethod("withQuote", Character.class);

        // Test with non-null Character
        Character quoteChar = Character.valueOf('\"');
        CSVFormat result = (CSVFormat) method.invoke(original, quoteChar);
        assertNotNull(result);
        assertNotSame(original, result);
        assertEquals(quoteChar, result.getQuoteCharacter());
    }
}