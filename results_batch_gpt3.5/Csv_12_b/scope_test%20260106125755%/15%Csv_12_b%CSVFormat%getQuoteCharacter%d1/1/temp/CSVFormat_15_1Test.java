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

public class CSVFormat_15_1Test {

    @Test
    @Timeout(8000)
    public void testGetQuoteCharacter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals(Character.valueOf('"'), quoteChar);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteCharacter_NullQuote() {
        CSVFormat format = CSVFormat.MYSQL;
        Character quoteChar = format.getQuoteCharacter();
        assertNull(quoteChar);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteCharacter_CustomQuote() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('\'');
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals(Character.valueOf('\''), quoteChar);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteCharacter_WithQuoteNull() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote((Character) null);
        Character quoteChar = format.getQuoteCharacter();
        assertNull(quoteChar);
    }
}