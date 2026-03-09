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

class CSVFormat_40_1Test {

    @Test
    @Timeout(8000)
    void testWithQuoteMode() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        QuoteMode newQuoteMode = QuoteMode.ALL;

        CSVFormat resultFormat = baseFormat.withQuoteMode(newQuoteMode);

        // The returned instance should not be the same as the original
        assertNotSame(baseFormat, resultFormat);

        // The quoteMode of the new instance should be the one passed
        assertEquals(newQuoteMode, resultFormat.getQuoteMode());

        // All other properties should remain unchanged
        assertEquals(baseFormat.getDelimiter(), resultFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), resultFormat.getQuoteCharacter());
        assertEquals(baseFormat.getCommentMarker(), resultFormat.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), resultFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), resultFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), resultFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), resultFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), resultFormat.getNullString());
        assertArrayEquals(baseFormat.getHeader(), resultFormat.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), resultFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), resultFormat.getAllowMissingColumnNames());

        // Test with null quoteMode
        CSVFormat nullQuoteModeFormat = baseFormat.withQuoteMode(null);
        assertNull(nullQuoteModeFormat.getQuoteMode());
        assertEquals(baseFormat.getDelimiter(), nullQuoteModeFormat.getDelimiter());
    }
}