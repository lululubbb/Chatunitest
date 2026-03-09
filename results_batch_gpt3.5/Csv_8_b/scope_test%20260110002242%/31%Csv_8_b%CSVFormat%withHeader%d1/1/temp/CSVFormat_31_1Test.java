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

class CSVFormatWithHeaderTest {

    @Test
    @Timeout(8000)
    void testWithHeaderCreatesNewInstanceWithGivenHeader() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        String[] headers = new String[] {"col1", "col2", "col3"};
        CSVFormat updated = original.withHeader(headers);

        // Verify that a new instance is created and original is unchanged
        assertNotSame(original, updated);

        // Verify that header is set correctly in new instance
        assertArrayEquals(headers, updated.getHeader());

        // Verify other fields remain the same
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteChar(), updated.getQuoteChar());
        assertEquals(original.getQuotePolicy(), updated.getQuotePolicy());
        assertEquals(original.getCommentStart(), updated.getCommentStart());
        assertEquals(original.getEscape(), updated.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderNullHeader() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat updated = original.withHeader((String[]) null);

        assertNotSame(original, updated);
        assertNull(updated.getHeader());

        // Verify other fields remain the same
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteChar(), updated.getQuoteChar());
        assertEquals(original.getQuotePolicy(), updated.getQuotePolicy());
        assertEquals(original.getCommentStart(), updated.getCommentStart());
        assertEquals(original.getEscape(), updated.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderEmptyHeader() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        String[] headers = new String[0];
        CSVFormat updated = original.withHeader(headers);

        assertNotSame(original, updated);
        assertArrayEquals(headers, updated.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderReflectionInvocation() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] headers = new String[] {"a", "b"};

        Method method = CSVFormat.class.getDeclaredMethod("withHeader", String[].class);
        method.setAccessible(true);
        CSVFormat updated = (CSVFormat) method.invoke(original, (Object) headers);

        assertNotNull(updated);
        assertArrayEquals(headers, updated.getHeader());
    }
}