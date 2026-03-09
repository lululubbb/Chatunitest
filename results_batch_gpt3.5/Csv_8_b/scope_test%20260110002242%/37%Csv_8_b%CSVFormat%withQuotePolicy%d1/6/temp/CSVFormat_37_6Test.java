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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.Quote;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_37_6Test {

    @Test
    @Timeout(8000)
    void testWithQuotePolicyCreatesNewInstanceWithGivenQuotePolicy() {
        CSVFormat original = CSVFormat.DEFAULT;

        // Test with null quotePolicy
        CSVFormat resultNull = original.withQuotePolicy(null);
        assertNotSame(original, resultNull);
        assertNull(resultNull.getQuotePolicy());
        assertEquals(original.getDelimiter(), resultNull.getDelimiter());
        assertEquals(original.getQuoteChar(), resultNull.getQuoteChar());
        assertEquals(original.getCommentStart(), resultNull.getCommentStart());
        assertEquals(original.getEscape(), resultNull.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), resultNull.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), resultNull.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), resultNull.getRecordSeparator());
        assertEquals(original.getNullString(), resultNull.getNullString());
        assertArrayEquals(original.getHeader(), resultNull.getHeader());
        assertEquals(original.getSkipHeaderRecord(), resultNull.getSkipHeaderRecord());

        // Test with a non-null Quote policy
        Quote testQuotePolicy = Quote.ALL; // Use a valid Quote enum constant available in Apache Commons CSV
        CSVFormat resultWithQuote = original.withQuotePolicy(testQuotePolicy);
        assertNotSame(original, resultWithQuote);
        assertEquals(testQuotePolicy, resultWithQuote.getQuotePolicy());
        assertEquals(original.getDelimiter(), resultWithQuote.getDelimiter());
        assertEquals(original.getQuoteChar(), resultWithQuote.getQuoteChar());
        assertEquals(original.getCommentStart(), resultWithQuote.getCommentStart());
        assertEquals(original.getEscape(), resultWithQuote.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), resultWithQuote.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), resultWithQuote.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), resultWithQuote.getRecordSeparator());
        assertEquals(original.getNullString(), resultWithQuote.getNullString());
        assertArrayEquals(original.getHeader(), resultWithQuote.getHeader());
        assertEquals(original.getSkipHeaderRecord(), resultWithQuote.getSkipHeaderRecord());
    }
}