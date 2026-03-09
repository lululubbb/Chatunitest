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

class CSVFormat_35_6Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat changed = original.withIgnoreEmptyLines(true);

        assertNotSame(original, changed);
        assertTrue(changed.getIgnoreEmptyLines());
        // Original should remain unchanged
        assertTrue(original.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesFalse() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat changed = original.withIgnoreEmptyLines(false);

        assertNotSame(original, changed);
        assertFalse(changed.getIgnoreEmptyLines());
        // Original should remain unchanged
        assertTrue(original.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesDoesNotChangeOtherProperties() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat changed = original.withIgnoreEmptyLines(!original.getIgnoreEmptyLines());

        // Check all other fields remain equal
        assertEquals(original.getDelimiter(), changed.getDelimiter());
        assertEquals(original.getQuoteCharacter(), changed.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), changed.getQuoteMode());
        assertEquals(original.getCommentMarker(), changed.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), changed.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), changed.getIgnoreSurroundingSpaces());
        assertEquals(original.getRecordSeparator(), changed.getRecordSeparator());
        assertEquals(original.getNullString(), changed.getNullString());

        String[] originalHeader = original.getHeader();
        String[] changedHeader = changed.getHeader();
        if (originalHeader == null && changedHeader == null) {
            // both null, ok
        } else if (originalHeader != null && changedHeader != null) {
            assertArrayEquals(originalHeader, changedHeader);
        } else {
            fail("Header arrays do not match");
        }

        assertEquals(original.getSkipHeaderRecord(), changed.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), changed.getAllowMissingColumnNames());
    }
}