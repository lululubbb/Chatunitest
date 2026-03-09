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
import org.junit.jupiter.api.Test;

class CSVFormat_39_1Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_String() {
        CSVFormat original = CSVFormat.DEFAULT;
        String newRecordSeparator = "\n";
        CSVFormat modified = original.withRecordSeparator(newRecordSeparator);

        // Check that the new instance is not the same as original (immutability)
        assertNotSame(original, modified);

        // Check that the record separator is updated
        assertEquals(newRecordSeparator, modified.getRecordSeparator());

        // Check that other fields remain the same
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteChar(), modified.getQuoteChar());
        assertEquals(original.getQuotePolicy(), modified.getQuotePolicy());
        assertEquals(original.getCommentStart(), modified.getCommentStart());
        assertEquals(original.getEscape(), modified.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());

        // Test with null record separator
        CSVFormat nullSeparatorFormat = original.withRecordSeparator((String) null);
        assertNull(nullSeparatorFormat.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_EmptyString() {
        CSVFormat original = CSVFormat.DEFAULT;
        String emptySeparator = "";
        CSVFormat modified = original.withRecordSeparator(emptySeparator);

        assertNotSame(original, modified);
        assertEquals(emptySeparator, modified.getRecordSeparator());
    }
}