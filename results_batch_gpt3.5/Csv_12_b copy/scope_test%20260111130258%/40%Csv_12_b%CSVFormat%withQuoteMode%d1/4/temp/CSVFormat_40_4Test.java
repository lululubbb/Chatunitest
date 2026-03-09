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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CSVFormat_40_4Test {

    @Test
    @Timeout(8000)
    public void testWithQuoteMode() {
        // Given
        QuoteMode quoteMode = QuoteMode.ALL;
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withDelimiter(',')
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n");

        // When
        CSVFormat newCsvFormat = csvFormat.withQuoteMode(quoteMode);

        // Then
        assertEquals(quoteMode, newCsvFormat.getQuoteMode());
    }

    // Additional tests for branch coverage

    @Test
    @Timeout(8000)
    public void testWithQuoteMode_NullQuoteMode() {
        // Given
        QuoteMode quoteMode = null;
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withDelimiter(',')
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n");

        // When
        CSVFormat newCsvFormat = csvFormat.withQuoteMode(quoteMode);

        // Then
        assertEquals(quoteMode, newCsvFormat.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteMode_RFC4180() {
        // Given
        QuoteMode quoteMode = QuoteMode.ALL;
        CSVFormat csvFormat = CSVFormat.RFC4180;

        // When
        CSVFormat newCsvFormat = csvFormat.withQuoteMode(quoteMode);

        // Then
        assertEquals(quoteMode, newCsvFormat.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteMode_Excel() {
        // Given
        QuoteMode quoteMode = QuoteMode.ALL;
        CSVFormat csvFormat = CSVFormat.EXCEL;

        // When
        CSVFormat newCsvFormat = csvFormat.withQuoteMode(quoteMode);

        // Then
        assertEquals(quoteMode, newCsvFormat.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteMode_TDF() {
        // Given
        QuoteMode quoteMode = QuoteMode.ALL;
        CSVFormat csvFormat = CSVFormat.TDF;

        // When
        CSVFormat newCsvFormat = csvFormat.withQuoteMode(quoteMode);

        // Then
        assertEquals(quoteMode, newCsvFormat.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteMode_MYSQL() {
        // Given
        QuoteMode quoteMode = QuoteMode.ALL;
        CSVFormat csvFormat = CSVFormat.MYSQL;

        // When
        CSVFormat newCsvFormat = csvFormat.withQuoteMode(quoteMode);

        // Then
        assertEquals(quoteMode, newCsvFormat.getQuoteMode());
    }
}