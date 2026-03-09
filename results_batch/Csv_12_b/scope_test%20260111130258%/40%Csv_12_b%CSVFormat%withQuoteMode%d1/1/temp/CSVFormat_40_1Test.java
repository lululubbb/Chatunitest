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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class CSVFormat_40_1Test {

    @Test
    @Timeout(8000)
    public void testWithQuoteMode() {
        // Create a mock CSVFormat object
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // Define the desired QuoteMode
        QuoteMode quoteMode = QuoteMode.ALL;

        // Call the focal method with the desired QuoteMode
        CSVFormat newCsvFormat = csvFormat.withQuoteMode(quoteMode);

        // Validate the new CSVFormat object
        assertEquals(',', newCsvFormat.getDelimiter());
        assertEquals(Character.valueOf('\"'), newCsvFormat.getQuoteCharacter());
        assertEquals(quoteMode, newCsvFormat.getQuoteMode());
        assertEquals(null, newCsvFormat.getCommentMarker());
        assertEquals(null, newCsvFormat.getEscapeCharacter());
        assertEquals(false, newCsvFormat.getIgnoreSurroundingSpaces());
        assertEquals(true, newCsvFormat.getIgnoreEmptyLines());
        assertEquals("\r\n", newCsvFormat.getRecordSeparator());
        assertEquals(null, newCsvFormat.getNullString());
        assertEquals(null, newCsvFormat.getHeader());
        assertEquals(false, newCsvFormat.getSkipHeaderRecord());
        assertEquals(false, newCsvFormat.getAllowMissingColumnNames());
    }
}