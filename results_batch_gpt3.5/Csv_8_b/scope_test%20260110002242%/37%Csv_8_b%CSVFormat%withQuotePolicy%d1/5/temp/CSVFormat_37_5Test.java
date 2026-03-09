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
import org.apache.commons.csv.Quote;
import org.junit.jupiter.api.Test;

class CSVFormat_37_5Test {

    @Test
    @Timeout(8000)
    void testWithQuotePolicy_NullPolicy() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withQuotePolicy(null);
        assertNotNull(result);
        assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
        assertEquals(baseFormat.getQuoteChar(), result.getQuoteChar());
        assertNull(result.getQuotePolicy());
        assertEquals(baseFormat.getCommentStart(), result.getCommentStart());
        assertEquals(baseFormat.getEscape(), result.getEscape());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), result.getNullString());
        assertArrayEquals(baseFormat.getHeader(), result.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithQuotePolicy_NonNullPolicy() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Quote customQuotePolicy = Quote.MINIMAL;
        CSVFormat result = baseFormat.withQuotePolicy(customQuotePolicy);
        assertNotNull(result);
        assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
        // The quoteChar is unchanged by withQuotePolicy, so keep the same expectation
        assertEquals(baseFormat.getQuoteChar(), result.getQuoteChar());
        assertEquals(customQuotePolicy, result.getQuotePolicy());
        assertEquals(baseFormat.getCommentStart(), result.getCommentStart());
        assertEquals(baseFormat.getEscape(), result.getEscape());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), result.getNullString());
        assertArrayEquals(baseFormat.getHeader(), result.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }
}