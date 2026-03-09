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

class CSVFormat_50_5Test {

    @Test
    @Timeout(8000)
    void testWithQuote_withChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\'';

        CSVFormat result = original.withQuote(quoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
        // Original should remain unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), original.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withChar_doubleQuote() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '"';

        CSVFormat result = original.withQuote(quoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), original.getQuoteCharacter());
    }
}