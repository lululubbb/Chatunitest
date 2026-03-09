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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_52_2Test {

    @Test
    @Timeout(8000)
    void testWithQuoteMode_NullQuoteMode() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat changed = original.withQuoteMode(null);
        assertNotNull(changed);
        assertNotSame(original, changed);
        assertNull(changed.getQuoteMode());
        // other fields remain unchanged
        assertEquals(original.getDelimiter(), changed.getDelimiter());
        assertEquals(original.getQuoteCharacter(), changed.getQuoteCharacter());
        assertEquals(original.getCommentMarker(), changed.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), changed.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), changed.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), changed.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), changed.getRecordSeparator());
        assertEquals(original.getNullString(), changed.getNullString());
        assertArrayEquals(original.getHeaderComments(), changed.getHeaderComments());
        assertArrayEquals(original.getHeader(), changed.getHeader());
        assertEquals(original.getSkipHeaderRecord(), changed.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), changed.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), changed.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteMode_NonNullQuoteMode() {
        CSVFormat original = CSVFormat.DEFAULT;
        QuoteMode quoteMode = QuoteMode.ALL;
        CSVFormat changed = original.withQuoteMode(quoteMode);
        assertNotNull(changed);
        assertNotSame(original, changed);
        assertEquals(quoteMode, changed.getQuoteMode());
        // other fields remain unchanged
        assertEquals(original.getDelimiter(), changed.getDelimiter());
        assertEquals(original.getQuoteCharacter(), changed.getQuoteCharacter());
        assertEquals(original.getCommentMarker(), changed.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), changed.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), changed.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), changed.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), changed.getRecordSeparator());
        assertEquals(original.getNullString(), changed.getNullString());
        assertArrayEquals(original.getHeaderComments(), changed.getHeaderComments());
        assertArrayEquals(original.getHeader(), changed.getHeader());
        assertEquals(original.getSkipHeaderRecord(), changed.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), changed.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), changed.getIgnoreHeaderCase());
    }
}