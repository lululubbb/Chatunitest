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

class CSVFormat_34_6Test {

    @Test
    @Timeout(8000)
    void testWithNullString() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Test with a non-null string
        String nullStr = "NULL";
        CSVFormat modified = original.withNullString(nullStr);
        assertNotSame(original, modified);
        assertEquals(nullStr, modified.getNullString());
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteChar(), modified.getQuoteChar());
        assertEquals(original.getQuotePolicy(), modified.getQuotePolicy());
        assertEquals(original.getCommentStart(), modified.getCommentStart());
        assertEquals(original.getEscape(), modified.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());

        // Test with null string (null)
        CSVFormat modifiedNull = original.withNullString(null);
        assertNotSame(original, modifiedNull);
        assertNull(modifiedNull.getNullString());

        // Test chaining withNullString multiple times
        CSVFormat chain1 = original.withNullString("first");
        CSVFormat chain2 = chain1.withNullString("second");
        assertEquals("second", chain2.getNullString());
        assertEquals("first", chain1.getNullString());
        assertNotSame(chain1, chain2);
    }
}