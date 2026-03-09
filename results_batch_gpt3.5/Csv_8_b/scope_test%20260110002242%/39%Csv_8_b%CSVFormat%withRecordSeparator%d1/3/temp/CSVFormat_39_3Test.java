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
    void testWithRecordSeparator_NewInstanceWithGivenString() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String newRecordSeparator = "\n";
        CSVFormat updated = original.withRecordSeparator(newRecordSeparator);

        assertNotSame(original, updated, "withRecordSeparator should return a new instance");
        assertEquals(newRecordSeparator, updated.getRecordSeparator(), "Record separator should be updated");
        // All other fields should be equal to original
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteChar(), updated.getQuoteChar());
        assertEquals(original.getQuotePolicy(), updated.getQuotePolicy());
        assertEquals(original.getCommentStart(), updated.getCommentStart());
        assertEquals(original.getEscape(), updated.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_EmptyString() {
        CSVFormat original = CSVFormat.DEFAULT;
        String newRecordSeparator = "";
        CSVFormat updated = original.withRecordSeparator(newRecordSeparator);

        assertNotSame(original, updated);
        assertEquals(newRecordSeparator, updated.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_NullString() {
        CSVFormat original = CSVFormat.DEFAULT;
        String newRecordSeparator = null;
        CSVFormat updated = original.withRecordSeparator(newRecordSeparator);

        assertNotSame(original, updated);
        assertNull(updated.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_SpecialCharacters() {
        CSVFormat original = CSVFormat.DEFAULT;
        String newRecordSeparator = "\r\n";
        CSVFormat updated = original.withRecordSeparator(newRecordSeparator);

        assertNotSame(original, updated);
        assertEquals(newRecordSeparator, updated.getRecordSeparator());
    }
}