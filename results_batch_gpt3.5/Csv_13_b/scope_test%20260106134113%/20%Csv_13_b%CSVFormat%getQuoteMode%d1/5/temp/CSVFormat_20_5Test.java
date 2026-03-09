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

class CSVFormat_20_5Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode quoteMode = format.getQuoteMode();
        assertNull(quoteMode, "Default CSVFormat should have null quoteMode");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithQuoteMode() {
        QuoteMode customQuoteMode = QuoteMode.ALL;
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(customQuoteMode);
        QuoteMode quoteMode = format.getQuoteMode();
        assertEquals(customQuoteMode, quoteMode, "QuoteMode should be the one set via withQuoteMode");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithNullQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(null);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNull(quoteMode, "QuoteMode can be set to null explicitly");
    }
}