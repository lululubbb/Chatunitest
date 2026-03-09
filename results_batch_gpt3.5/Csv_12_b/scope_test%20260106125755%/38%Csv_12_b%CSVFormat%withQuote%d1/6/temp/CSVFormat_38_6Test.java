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

public class CSVFormat_38_6Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_char() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\'';

        // Invoke public withQuote(char) method
        CSVFormat result = original.withQuote(quoteChar);

        // The result should not be null
        assertNotNull(result);

        // Using reflection to invoke private withQuote(Character) method to verify equivalence
        Method withQuoteCharMethod = CSVFormat.class.getDeclaredMethod("withQuote", Character.class);
        withQuoteCharMethod.setAccessible(true);
        CSVFormat expected = (CSVFormat) withQuoteCharMethod.invoke(original, quoteChar);

        // The result from withQuote(char) should be equal to withQuote(Character)
        assertEquals(expected, result);

        // Test with quoteChar as DOUBLE_QUOTE_CHAR (default)
        Character defaultQuoteChar = CSVFormat.DEFAULT.getQuoteCharacter();
        if (defaultQuoteChar != null) {
            CSVFormat defaultResult = original.withQuote(defaultQuoteChar);
            assertNotNull(defaultResult);
        }

        // Test with quoteChar as null char (edge case)
        CSVFormat nullCharResult = original.withQuote('\0');
        assertNotNull(nullCharResult);
    }
}