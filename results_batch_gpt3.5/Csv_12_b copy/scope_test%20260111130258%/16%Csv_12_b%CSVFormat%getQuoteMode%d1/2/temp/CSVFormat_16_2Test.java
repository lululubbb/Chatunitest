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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_16_2Test {

    @Test
    @Timeout(8000)
    public void testGetQuoteMode() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withQuoteMode(QuoteMode.ALL)
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withRecordSeparator("\r\n");

        // When
        QuoteMode quoteMode = csvFormat.getQuoteMode();

        // Then
        assertEquals(QuoteMode.ALL, quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_Default() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        QuoteMode quoteMode = csvFormat.getQuoteMode();

        // Then
        assertEquals(null, quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_RFC4180() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.RFC4180;

        // When
        QuoteMode quoteMode = csvFormat.getQuoteMode();

        // Then
        assertEquals(null, quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_Excel() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.EXCEL;

        // When
        QuoteMode quoteMode = csvFormat.getQuoteMode();

        // Then
        assertEquals(null, quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_TDF() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.TDF;

        // When
        QuoteMode quoteMode = csvFormat.getQuoteMode();

        // Then
        assertEquals(null, quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_MYSQL() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.MYSQL;

        // When
        QuoteMode quoteMode = csvFormat.getQuoteMode();

        // Then
        assertEquals(null, quoteMode);
    }
}