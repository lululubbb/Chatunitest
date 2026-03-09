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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_33_5Test {

    @Test
    @Timeout(8000)
    void testWithHeaderCreatesNewInstanceWithGivenHeader() {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] headers = new String[]{"col1", "col2", "col3"};

        CSVFormat result = original.withHeader(headers);

        // Original instance should remain unchanged
        assertNull(original.getHeader());

        // New instance should have the specified header
        assertArrayEquals(headers, result.getHeader());

        // Other properties should be equal between original and new instance
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
    void testWithHeaderWithEmptyHeaderArray() {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] headers = new String[0];

        CSVFormat result = original.withHeader(headers);

        assertNotNull(result.getHeader());
        assertEquals(0, result.getHeader().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithNullHeaderArray() {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat result = original.withHeader((String[]) null);

        assertNull(result.getHeader());
    }

}