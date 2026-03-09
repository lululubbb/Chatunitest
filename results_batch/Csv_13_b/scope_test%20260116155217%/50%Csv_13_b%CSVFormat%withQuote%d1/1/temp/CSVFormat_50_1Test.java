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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

public class CSVFormat_50_1Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_primitiveChar() {
        // Test with normal quote character
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withQuote(Character.valueOf('"'));
        assertNotNull(updated);
        assertEquals(Character.valueOf('"'), updated.getQuoteCharacter());

        // Test with different quote character
        CSVFormat updated2 = original.withQuote(Character.valueOf('\''));
        assertNotNull(updated2);
        assertEquals(Character.valueOf('\''), updated2.getQuoteCharacter());

        // Test with quote character same as original to check immutability or equality
        Character defaultQuoteChar = CSVFormat.DEFAULT.getQuoteCharacter();
        CSVFormat updated3 = original.withQuote(defaultQuoteChar);
        assertNotNull(updated3);
        assertEquals(defaultQuoteChar, updated3.getQuoteCharacter());
    }
}