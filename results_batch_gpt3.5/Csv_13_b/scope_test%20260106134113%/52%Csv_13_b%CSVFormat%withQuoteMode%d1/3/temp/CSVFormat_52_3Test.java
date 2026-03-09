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
    void testWithQuoteModeCreatesNewInstanceWithGivenQuoteMode() {
        QuoteMode originalQuoteMode = QuoteMode.ALL;
        QuoteMode newQuoteMode = QuoteMode.MINIMAL;

        CSVFormat originalFormat = CSVFormat.DEFAULT.withQuoteMode(originalQuoteMode);
        CSVFormat newFormat = originalFormat.withQuoteMode(newQuoteMode);

        assertNotNull(newFormat);
        assertNotSame(originalFormat, newFormat, "withQuoteMode should return a new CSVFormat instance");
        assertEquals(newQuoteMode, newFormat.getQuoteMode(), "QuoteMode should be updated to new value");
        // Other fields should remain unchanged
        assertEquals(originalFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(originalFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(originalFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(originalFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(originalFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(originalFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(originalFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(originalFormat.getNullString(), newFormat.getNullString());
        assertArrayEquals(originalFormat.getHeader(), newFormat.getHeader());
        assertArrayEquals(originalFormat.getHeaderComments(), newFormat.getHeaderComments());
        assertEquals(originalFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(originalFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(originalFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteModeNullQuoteMode() {
        CSVFormat originalFormat = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        CSVFormat newFormat = originalFormat.withQuoteMode(null);

        assertNotNull(newFormat);
        assertNull(newFormat.getQuoteMode(), "QuoteMode should be null when set to null");
        assertNotSame(originalFormat, newFormat);
    }

    @Test
    @Timeout(8000)
    void testWithQuoteModeSameQuoteModeReturnsDifferentInstance() {
        CSVFormat originalFormat = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        CSVFormat newFormat = originalFormat.withQuoteMode(QuoteMode.ALL);

        assertNotSame(originalFormat, newFormat, "Should return a new instance even if QuoteMode is the same");
        assertEquals(originalFormat.getQuoteMode(), newFormat.getQuoteMode());
    }
}