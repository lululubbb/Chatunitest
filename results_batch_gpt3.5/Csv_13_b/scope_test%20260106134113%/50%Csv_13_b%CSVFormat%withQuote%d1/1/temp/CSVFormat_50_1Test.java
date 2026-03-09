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

class CSVFormatWithQuoteTest {

    @Test
    @Timeout(8000)
    void testWithQuote_withPrimitiveChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char quoteChar = '\'';
        CSVFormat newFormat = format.withQuote(quoteChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(quoteChar), newFormat.getQuoteCharacter());
        // Original format remains unchanged (immutable)
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withNullCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        java.lang.reflect.Method method = CSVFormat.class.getDeclaredMethod("withQuote", Character.class);
        method.setAccessible(true);
        CSVFormat result = (CSVFormat) method.invoke(format, new Object[] { null });
        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withSameQuoteCharacterReturnsSameInstance() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = format.getQuoteCharacter();
        java.lang.reflect.Method method = CSVFormat.class.getDeclaredMethod("withQuote", Character.class);
        method.setAccessible(true);
        CSVFormat result = (CSVFormat) method.invoke(format, quoteChar);
        // Should return same instance if quoteChar is unchanged
        assertSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withDifferentQuoteCharacterReturnsNewInstance() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Character newQuoteChar = '\'';
        // If DEFAULT's quoteCharacter is already '\'', pick a different one to ensure new instance
        if (newQuoteChar.equals(format.getQuoteCharacter())) {
            newQuoteChar = '"';
        }
        java.lang.reflect.Method method = CSVFormat.class.getDeclaredMethod("withQuote", Character.class);
        method.setAccessible(true);
        CSVFormat result = (CSVFormat) method.invoke(format, newQuoteChar);
        assertNotNull(result);
        assertEquals(newQuoteChar, result.getQuoteCharacter());
        assertNotSame(format, result);
    }
}