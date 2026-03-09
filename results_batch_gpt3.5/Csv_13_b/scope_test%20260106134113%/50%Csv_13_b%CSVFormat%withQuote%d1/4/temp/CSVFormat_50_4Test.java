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

class CSVFormat_50_4Test {

    @Test
    @Timeout(8000)
    void testWithQuote_char() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with regular quote character
        CSVFormat result = baseFormat.withQuote('\'');
        assertNotNull(result);
        assertEquals(Character.valueOf('\''), result.getQuoteCharacter());

        // Test with double quote character
        result = baseFormat.withQuote('"');
        assertNotNull(result);
        assertEquals(Character.valueOf('"'), result.getQuoteCharacter());

        // Test with backslash character
        result = baseFormat.withQuote('\\');
        assertNotNull(result);
        assertEquals(Character.valueOf('\\'), result.getQuoteCharacter());

        // Test with null character (0) - although char primitive can't be null, test edge case
        result = baseFormat.withQuote('\0');
        assertNotNull(result);
        assertEquals(Character.valueOf('\0'), result.getQuoteCharacter());

        // Test with same quote character as default
        Character defaultQuoteChar = CSVFormat.DEFAULT.getQuoteCharacter();
        // Defensive check for null to avoid NullPointerException in assertEquals
        if (defaultQuoteChar == null) {
            result = baseFormat.withQuote('\0'); // use '\0' if default is null
            assertNotNull(result);
            assertEquals(Character.valueOf('\0'), result.getQuoteCharacter());
        } else {
            result = baseFormat.withQuote(defaultQuoteChar.charValue());
            assertNotNull(result);
            assertEquals(defaultQuoteChar, result.getQuoteCharacter());
        }
    }
}