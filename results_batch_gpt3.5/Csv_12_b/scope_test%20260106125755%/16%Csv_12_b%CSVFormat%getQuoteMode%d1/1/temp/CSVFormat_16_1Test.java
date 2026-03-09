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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_16_1Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode mode = format.getQuoteMode();
        assertNull(mode, "Default CSVFormat quoteMode should be null");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        QuoteMode mode = format.getQuoteMode();
        assertEquals(QuoteMode.ALL, mode, "QuoteMode should be ALL");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithQuoteModeNone() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.NONE);
        QuoteMode mode = format.getQuoteMode();
        assertEquals(QuoteMode.NONE, mode, "QuoteMode should be NONE");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithQuoteModeMinimal() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        QuoteMode mode = format.getQuoteMode();
        assertEquals(QuoteMode.MINIMAL, mode, "QuoteMode should be MINIMAL");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithQuoteModeNonNull() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                QuoteMode.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                String[].class,
                boolean.class,
                boolean.class
        );
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                ',',
                Character.valueOf('"'),
                QuoteMode.NON_NUMERIC,
                null,
                null,
                false,
                false,
                "\r\n",
                null,
                (String[]) null,
                false,
                true
        );
        QuoteMode mode = format.getQuoteMode();
        assertEquals(QuoteMode.NON_NUMERIC, mode, "QuoteMode should be NON_NUMERIC");
    }
}