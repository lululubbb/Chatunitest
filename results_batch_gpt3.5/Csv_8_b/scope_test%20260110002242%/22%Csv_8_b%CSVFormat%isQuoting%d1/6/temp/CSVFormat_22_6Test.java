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

class CSVFormatIsQuotingTest {

    @Test
    @Timeout(8000)
    void testIsQuotingWhenQuoteCharIsNotNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        // DEFAULT has quoteChar = DOUBLE_QUOTE_CHAR (not null)
        assertTrue(format.isQuoting());
    }

    @Test
    @Timeout(8000)
    void testIsQuotingWhenQuoteCharIsNull() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteChar((Character) null);
        assertFalse(format.isQuoting());
    }

    @Test
    @Timeout(8000)
    void testIsQuotingWithCustomQuoteChar() {
        CSVFormat format = CSVFormat.newFormat(';').withQuoteChar('#');
        assertTrue(format.isQuoting());
    }
}