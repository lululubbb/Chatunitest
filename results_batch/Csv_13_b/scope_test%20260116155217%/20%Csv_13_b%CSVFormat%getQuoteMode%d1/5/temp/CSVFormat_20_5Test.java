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
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class CSVFormat_20_5Test {

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
        QuoteMode customQuoteMode = QuoteMode.ALL;
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(customQuoteMode);
        QuoteMode mode = format.getQuoteMode();
        assertEquals(customQuoteMode, mode, "CSVFormat quoteMode should be the one set");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithNullQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(null);
        QuoteMode mode = format.getQuoteMode();
        assertNull(mode, "CSVFormat quoteMode should be null when set to null");
    }

    // Reflection method retained if needed
    private QuoteMode getQuoteModeReflectively(CSVFormat format) {
        try {
            Field field = CSVFormat.class.getDeclaredField("quoteMode");
            field.setAccessible(true);
            return (QuoteMode) field.get(format);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}