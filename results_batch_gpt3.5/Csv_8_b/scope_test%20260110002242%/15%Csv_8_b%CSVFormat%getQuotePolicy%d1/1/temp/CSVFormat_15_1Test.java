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

class CSVFormat_15_1Test {

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        Quote quotePolicy = format.getQuotePolicy();
        assertNull(quotePolicy, "Default CSVFormat quotePolicy should be null");
    }

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_WithQuotePolicy() throws Exception {
        Quote customQuote = Quote.MINIMAL;
        CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(customQuote);
        Quote quotePolicy = format.getQuotePolicy();
        assertSame(customQuote, quotePolicy, "Quote policy should be the one set");
    }

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_NullQuotePolicy() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(null);
        Quote quotePolicy = format.getQuotePolicy();
        assertNull(quotePolicy, "Quote policy should be null when set to null");
    }
}