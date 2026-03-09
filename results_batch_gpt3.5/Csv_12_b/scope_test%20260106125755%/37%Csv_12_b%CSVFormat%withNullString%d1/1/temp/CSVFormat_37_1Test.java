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

class CSVFormat_37_1Test {

    @Test
    @Timeout(8000)
    void testWithNullString() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Case: nullString is null
        CSVFormat formatNull = baseFormat.withNullString(null);
        assertNotNull(formatNull);
        assertNull(formatNull.getNullString());
        assertEquals(baseFormat.getDelimiter(), formatNull.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), formatNull.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), formatNull.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), formatNull.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), formatNull.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), formatNull.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), formatNull.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), formatNull.getRecordSeparator());
        assertArrayEquals(baseFormat.getHeader(), formatNull.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), formatNull.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), formatNull.getAllowMissingColumnNames());

        // Case: nullString is non-null
        String nullStr = "NULL";
        CSVFormat formatWithNull = baseFormat.withNullString(nullStr);
        assertNotNull(formatWithNull);
        assertEquals(nullStr, formatWithNull.getNullString());
        assertEquals(baseFormat.getDelimiter(), formatWithNull.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), formatWithNull.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), formatWithNull.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), formatWithNull.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), formatWithNull.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), formatWithNull.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), formatWithNull.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), formatWithNull.getRecordSeparator());
        assertArrayEquals(baseFormat.getHeader(), formatWithNull.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), formatWithNull.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), formatWithNull.getAllowMissingColumnNames());

        // Verify immutability: original format unchanged
        assertNull(baseFormat.getNullString());
    }
}