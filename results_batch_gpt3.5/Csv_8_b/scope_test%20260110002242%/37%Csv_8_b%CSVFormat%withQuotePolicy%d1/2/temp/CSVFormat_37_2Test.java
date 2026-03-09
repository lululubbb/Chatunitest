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

import java.lang.reflect.Field;

class CSVFormat_37_2Test {

    @Test
    @Timeout(8000)
    void testWithQuotePolicy() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Quote quotePolicy = Quote.MINIMAL;

        CSVFormat modified = original.withQuotePolicy(quotePolicy);

        assertNotNull(modified);
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteChar(), modified.getQuoteChar());
        assertEquals(quotePolicy, modified.getQuotePolicy());
        assertEquals(original.getCommentStart(), modified.getCommentStart());
        assertEquals(original.getEscape(), modified.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());

        // Also verify original remains unchanged by accessing the private field via reflection
        Field quotePolicyField = CSVFormat.class.getDeclaredField("quotePolicy");
        quotePolicyField.setAccessible(true);
        assertNull(quotePolicyField.get(original));
    }

    @Test
    @Timeout(8000)
    void testWithQuotePolicyNull() {
        CSVFormat original = CSVFormat.DEFAULT.withQuotePolicy(Quote.ALL);
        CSVFormat modified = original.withQuotePolicy(null);

        assertNotNull(modified);
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteChar(), modified.getQuoteChar());
        assertNull(modified.getQuotePolicy());
        assertEquals(original.getCommentStart(), modified.getCommentStart());
        assertEquals(original.getEscape(), modified.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
    }
}