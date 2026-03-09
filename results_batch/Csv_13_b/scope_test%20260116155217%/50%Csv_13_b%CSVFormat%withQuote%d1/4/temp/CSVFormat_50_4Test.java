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
import org.mockito.Mockito;

import java.lang.reflect.Method;

class CSVFormatWithQuoteTest {

    @Test
    @Timeout(8000)
    void testWithQuote_primitiveChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with a normal quote character
        CSVFormat result = baseFormat.withQuote('\'');
        assertNotNull(result);
        assertEquals(Character.valueOf('\''), result.getQuoteCharacter());

        // Test with double quote character (same as default)
        CSVFormat result2 = baseFormat.withQuote('"');
        assertNotNull(result2);
        assertEquals(Character.valueOf('"'), result2.getQuoteCharacter());

        // Test with backslash character
        CSVFormat result3 = baseFormat.withQuote('\\');
        assertNotNull(result3);
        assertEquals(Character.valueOf('\\'), result3.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_primitiveChar_nullChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with null character (char) - '\0'
        CSVFormat result = baseFormat.withQuote('\0');
        assertNotNull(result);
        assertEquals(Character.valueOf('\0'), result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_callsWithQuoteCharacter() throws Exception {
        CSVFormat baseFormat = Mockito.spy(CSVFormat.DEFAULT);

        // Use reflection to get the private withQuote(Character) method
        Method withQuoteCharMethod = CSVFormat.class.getDeclaredMethod("withQuote", Character.class);
        withQuoteCharMethod.setAccessible(true);

        // Call the public withQuote(char) method and verify it calls withQuote(Character)
        CSVFormat result = baseFormat.withQuote('Q');
        assertNotNull(result);
        assertEquals(Character.valueOf('Q'), result.getQuoteCharacter());

        // Also invoke private withQuote(Character) directly via reflection to test coverage
        CSVFormat result2 = (CSVFormat) withQuoteCharMethod.invoke(baseFormat, Character.valueOf('Z'));
        assertNotNull(result2);
        assertEquals(Character.valueOf('Z'), result2.getQuoteCharacter());
    }
}