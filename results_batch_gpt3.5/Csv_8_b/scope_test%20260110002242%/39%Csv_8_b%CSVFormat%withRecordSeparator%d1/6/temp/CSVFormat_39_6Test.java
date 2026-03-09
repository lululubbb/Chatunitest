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

class CSVFormatWithRecordSeparatorTest {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_String() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String newSeparator = "\n";
        CSVFormat updated = original.withRecordSeparator(newSeparator);

        // Verify that a new instance is returned and original is unchanged
        assertNotSame(original, updated);
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteChar(), updated.getQuoteChar());
        assertEquals(original.getQuotePolicy(), updated.getQuotePolicy());
        assertEquals(original.getCommentStart(), updated.getCommentStart());
        assertEquals(original.getEscape(), updated.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(newSeparator, updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());

        // Test with null recordSeparator
        CSVFormat nullSeparatorFormat = original.withRecordSeparator((String) null);
        assertNotSame(original, nullSeparatorFormat);
        assertNull(nullSeparatorFormat.getRecordSeparator());
    }
}