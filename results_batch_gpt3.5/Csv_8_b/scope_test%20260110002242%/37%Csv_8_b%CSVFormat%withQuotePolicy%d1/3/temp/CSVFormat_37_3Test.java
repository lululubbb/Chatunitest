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

class CSVFormat_37_3Test {

    @Test
    @Timeout(8000)
    void testWithQuotePolicy_NullQuotePolicy() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withQuotePolicy(null);
        assertNotNull(result);
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteChar(), result.getQuoteChar());
        assertNull(result.getQuotePolicy());
        assertEquals(format.getCommentStart(), result.getCommentStart());
        assertEquals(format.getEscape(), result.getEscape());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithQuotePolicy_NonNullQuotePolicy() {
        CSVFormat format = CSVFormat.DEFAULT;
        Quote quotePolicy = Quote.MINIMAL;
        CSVFormat result = format.withQuotePolicy(quotePolicy);
        assertNotNull(result);
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteChar(), result.getQuoteChar());
        assertEquals(quotePolicy, result.getQuotePolicy());
        assertEquals(format.getCommentStart(), result.getCommentStart());
        assertEquals(format.getEscape(), result.getEscape());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithQuotePolicy_Immutability() {
        CSVFormat format = CSVFormat.DEFAULT;
        Quote quotePolicy = Quote.ALL;
        CSVFormat result = format.withQuotePolicy(quotePolicy);
        // The original instance should remain unchanged
        assertNull(format.getQuotePolicy());
        assertEquals(quotePolicy, result.getQuotePolicy());
        assertNotSame(format, result);
    }
}