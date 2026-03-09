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

class CSVFormat_37_4Test {

    @Test
    @Timeout(8000)
    void testWithQuotePolicy() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Quote newQuotePolicy = Quote.MINIMAL;

        CSVFormat result = original.withQuotePolicy(newQuotePolicy);

        // original should remain unchanged
        assertNull(original.getQuotePolicy());

        // result should be a new instance
        assertNotSame(original, result);

        // result should have the new quote policy
        assertEquals(newQuotePolicy, result.getQuotePolicy());

        // other properties should be the same as original
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteChar(), result.getQuoteChar());
        assertEquals(original.getCommentStart(), result.getCommentStart());
        assertEquals(original.getEscape(), result.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());

        // test with null quote policy
        CSVFormat resultNull = original.withQuotePolicy(null);
        assertNull(resultNull.getQuotePolicy());
        assertNotSame(original, resultNull);
    }
}