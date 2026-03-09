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
    void testWithQuote_primitiveChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char quoteChar = '\'';
        CSVFormat result = format.withQuote(quoteChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
        // Original format should remain unchanged (immutability)
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_nullCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to invoke public withQuote(Character) with null
        Method withQuoteCharMethod = CSVFormat.class.getMethod("withQuote", Character.class);
        CSVFormat result = (CSVFormat) withQuoteCharMethod.invoke(format, (Object) null);
        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        // Original format should remain unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_sameQuoteCharacterReturnsSameInstance() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = format.getQuoteCharacter();
        Method withQuoteCharMethod = CSVFormat.class.getMethod("withQuote", Character.class);
        CSVFormat result = (CSVFormat) withQuoteCharMethod.invoke(format, quoteChar);
        assertSame(format, result);
    }
}