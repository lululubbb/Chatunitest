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
import java.lang.reflect.Method;

class CSVFormat_40_4Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = callWithSkipHeaderRecord(original, true);

        assertNotSame(original, modified, "withSkipHeaderRecord should return a new instance");
        assertTrue(modified.getSkipHeaderRecord(), "skipHeaderRecord should be true");
        // All other fields should be equal
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteChar(), modified.getQuoteChar());
        assertEquals(original.getQuotePolicy(), modified.getQuotePolicy());
        assertEquals(original.getCommentStart(), modified.getCommentStart());
        assertEquals(original.getEscape(), modified.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeader(), modified.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordFalse() throws Exception {
        CSVFormat original = callWithSkipHeaderRecord(CSVFormat.DEFAULT, true);
        CSVFormat modified = callWithSkipHeaderRecord(original, false);

        assertNotSame(original, modified, "withSkipHeaderRecord should return a new instance");
        assertFalse(modified.getSkipHeaderRecord(), "skipHeaderRecord should be false");
        // All other fields should be equal
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteChar(), modified.getQuoteChar());
        assertEquals(original.getQuotePolicy(), modified.getQuotePolicy());
        assertEquals(original.getCommentStart(), modified.getCommentStart());
        assertEquals(original.getEscape(), modified.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeader(), modified.getHeader());
    }

    private CSVFormat callWithSkipHeaderRecord(CSVFormat format, boolean skipHeaderRecord) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("withSkipHeaderRecord", boolean.class);
        method.setAccessible(true);
        return (CSVFormat) method.invoke(format, skipHeaderRecord);
    }
}