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

class CSVFormat_31_4Test {

    @Test
    @Timeout(8000)
    void testWithHeaderCreatesNewInstanceWithGivenHeader() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] headers = new String[] {"col1", "col2", "col3"};

        CSVFormat result = original.withHeader(headers);

        // Check that a new instance is returned and not the same instance
        assertNotSame(original, result);

        // Check that the header array is set correctly
        assertArrayEquals(headers, result.getHeader());

        // Check that other fields are the same as original
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteChar(), result.getQuoteChar());
        assertEquals(original.getQuotePolicy(), result.getQuotePolicy());
        assertEquals(original.getCommentStart(), result.getCommentStart());
        assertEquals(original.getEscape(), result.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithEmptyHeaders() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] headers = new String[0];

        CSVFormat result = original.withHeader(headers);

        assertNotSame(original, result);
        assertArrayEquals(headers, result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithNullHeaders() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat result = original.withHeader((String[]) null);

        assertNotSame(original, result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderPrivateConstructorInvocation() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] headers = new String[] {"a", "b"};

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", String[].class);
        withHeaderMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(original, (Object) headers);

        assertNotNull(result);
        assertArrayEquals(headers, result.getHeader());
    }
}