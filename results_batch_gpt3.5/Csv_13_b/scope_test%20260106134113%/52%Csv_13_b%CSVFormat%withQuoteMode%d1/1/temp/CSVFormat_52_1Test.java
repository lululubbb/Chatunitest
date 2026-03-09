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

class CSVFormatWithQuoteModeTest {

    @Test
    @Timeout(8000)
    void testWithQuoteModeReturnsNewInstanceWithGivenQuoteMode() {
        CSVFormat original = CSVFormat.DEFAULT;
        QuoteMode newQuoteMode = QuoteMode.ALL;

        CSVFormat updated = original.withQuoteMode(newQuoteMode);

        assertNotSame(original, updated, "withQuoteMode should return a new CSVFormat instance");
        assertEquals(newQuoteMode, updated.getQuoteMode(), "QuoteMode should be updated");
        // All other fields should remain unchanged
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeaderComments(), updated.getHeaderComments());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteModeNullQuoteMode() {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat updated = original.withQuoteMode(null);

        assertNotSame(original, updated, "withQuoteMode should return a new CSVFormat instance even with null");
        assertNull(updated.getQuoteMode(), "QuoteMode should be null when null is passed");
    }
}