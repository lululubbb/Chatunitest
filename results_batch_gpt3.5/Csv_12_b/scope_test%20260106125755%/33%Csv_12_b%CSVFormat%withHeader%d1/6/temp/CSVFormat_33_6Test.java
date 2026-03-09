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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CSVFormat_33_6Test {

    @Test
    @Timeout(8000)
    public void testWithHeaderCreatesNewInstanceWithGivenHeader() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] headers = new String[]{"col1", "col2", "col3"};

        CSVFormat result = original.withHeader(headers);

        assertNotNull(result);
        assertNotSame(original, result);
        assertArrayEquals(headers, result.getHeader());

        // Validate other fields remain unchanged
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderNullHeader() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeader((String[]) null);
        assertNotNull(result);
        assertNotSame(original, result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderEmptyHeader() {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] emptyHeaders = new String[0];
        CSVFormat result = original.withHeader(emptyHeaders);
        assertNotNull(result);
        assertNotSame(original, result);
        assertArrayEquals(emptyHeaders, result.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderReflectionInvoke() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] headers = new String[]{"a", "b"};

        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", String[].class);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(original, (Object) headers);

        assertNotNull(result);
        assertArrayEquals(headers, result.getHeader());
    }
}