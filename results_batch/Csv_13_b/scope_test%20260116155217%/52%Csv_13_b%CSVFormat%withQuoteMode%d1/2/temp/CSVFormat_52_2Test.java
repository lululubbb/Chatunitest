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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

class CSVFormat_52_2Test {

    @Test
    @Timeout(8000)
    void testWithQuoteModeReturnsNewInstanceWithGivenQuoteMode() {
        CSVFormat original = CSVFormat.DEFAULT;
        QuoteMode newQuoteMode = QuoteMode.ALL;

        CSVFormat updated = original.withQuoteMode(newQuoteMode);

        assertNotSame(original, updated, "withQuoteMode should return a new CSVFormat instance");
        assertEquals(newQuoteMode, updated.getQuoteMode(), "QuoteMode should be updated");
        // Assert other fields are unchanged
        assertEquals(original.getDelimiter(), updated.getDelimiter(), "Delimiter should remain unchanged");
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter(), "QuoteCharacter should remain unchanged");
        assertEquals(original.getCommentMarker(), updated.getCommentMarker(), "CommentMarker should remain unchanged");
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter(), "EscapeCharacter should remain unchanged");
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces(), "IgnoreSurroundingSpaces should remain unchanged");
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines(), "IgnoreEmptyLines should remain unchanged");
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator(), "RecordSeparator should remain unchanged");
        assertEquals(original.getNullString(), updated.getNullString(), "NullString should remain unchanged");
        assertArrayEquals(original.getHeaderComments(), updated.getHeaderComments(), "HeaderComments should remain unchanged");
        assertArrayEquals(original.getHeader(), updated.getHeader(), "Header should remain unchanged");
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord(), "SkipHeaderRecord should remain unchanged");
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames(), "AllowMissingColumnNames should remain unchanged");
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase(), "IgnoreHeaderCase should remain unchanged");
    }

    @Test
    @Timeout(8000)
    void testWithQuoteModeAllowsNullQuoteMode() {
        CSVFormat original = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        CSVFormat updated = original.withQuoteMode(null);

        assertNotSame(original, updated, "withQuoteMode should return a new CSVFormat instance");
        assertNull(updated.getQuoteMode(), "QuoteMode should be null when set to null");
    }
}