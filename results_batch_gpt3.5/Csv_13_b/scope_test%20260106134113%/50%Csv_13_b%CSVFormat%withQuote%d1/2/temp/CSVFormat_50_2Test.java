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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CSVFormat_50_2Test {

    @Test
    @Timeout(8000)
    public void testWithQuotePrimitiveChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with a normal quote char
        CSVFormat format = baseFormat.withQuote('"');
        assertNotNull(format);
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());

        // Test with a different quote char
        CSVFormat format2 = baseFormat.withQuote('\'');
        assertNotNull(format2);
        assertEquals(Character.valueOf('\''), format2.getQuoteCharacter());

        // Test with null character (edge case)
        CSVFormat format3 = baseFormat.withQuote('\0');
        assertNotNull(format3);
        assertEquals(Character.valueOf('\0'), format3.getQuoteCharacter());

        // Test that the original instance is not the same as the new one (immutability)
        assertNotSame(baseFormat, format);
        assertNotSame(baseFormat, format2);
        assertNotSame(baseFormat, format3);
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteCharacterMethodUsingReflection() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Method withQuoteCharacter = CSVFormat.class.getDeclaredMethod("withQuote", Character.class);
        withQuoteCharacter.setAccessible(true);

        // Invoke with a valid quote character
        CSVFormat result = (CSVFormat) withQuoteCharacter.invoke(baseFormat, Character.valueOf('\''));
        assertNotNull(result);
        assertEquals(Character.valueOf('\''), result.getQuoteCharacter());

        // Invoke with null quote character
        CSVFormat resultNull = (CSVFormat) withQuoteCharacter.invoke(baseFormat, new Object[]{null});
        assertNotNull(resultNull);
        assertNull(resultNull.getQuoteCharacter());

        // Check that original instance remains unchanged
        assertEquals(Character.valueOf('"'), baseFormat.getQuoteCharacter());
    }
}