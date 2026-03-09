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

class CSVFormatWithHeaderTest {

    @Test
    @Timeout(8000)
    void testWithHeaderCreatesNewInstanceWithGivenHeaders() {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] headers = {"Name", "Age", "City"};

        CSVFormat newFormat = original.withHeader(headers);

        assertNotNull(newFormat);
        assertNotSame(original, newFormat);
        assertArrayEquals(headers, newFormat.getHeader());

        // Check that other fields remain unchanged
        assertEquals(original.getDelimiter(), newFormat.getDelimiter());
        assertEquals(original.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(original.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(original.getNullString(), newFormat.getNullString());
        assertEquals(original.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithEmptyArray() {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] headers = new String[0];

        CSVFormat newFormat = original.withHeader(headers);

        assertNotNull(newFormat);
        assertNotSame(original, newFormat);
        assertArrayEquals(headers, newFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithNull() {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat newFormat = original.withHeader((String[]) null);

        assertNotNull(newFormat);
        assertNotSame(original, newFormat);
        assertNull(newFormat.getHeader());
    }
}