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
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Reader;

import org.junit.jupiter.api.Test;

public class CSVFormat_35_6Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(true);

        // Then
        assertNotNull(newCsvFormat);
        assertEquals(csvFormat.getDelimiter(), newCsvFormat.getDelimiter());
        assertEquals(csvFormat.getQuoteCharacter(), newCsvFormat.getQuoteCharacter());
        assertEquals(csvFormat.getQuoteMode(), newCsvFormat.getQuoteMode());
        assertEquals(csvFormat.getCommentMarker(), newCsvFormat.getCommentMarker());
        assertEquals(csvFormat.getEscapeCharacter(), newCsvFormat.getEscapeCharacter());
        assertEquals(csvFormat.getIgnoreSurroundingSpaces(), newCsvFormat.getIgnoreSurroundingSpaces());
        assertEquals(true, newCsvFormat.getIgnoreEmptyLines());
        assertEquals(csvFormat.getRecordSeparator(), newCsvFormat.getRecordSeparator());
        assertEquals(csvFormat.getNullString(), newCsvFormat.getNullString());
        assertArrayEquals(csvFormat.getHeader(), newCsvFormat.getHeader());
        assertEquals(csvFormat.getSkipHeaderRecord(), newCsvFormat.getSkipHeaderRecord());
        assertEquals(csvFormat.getAllowMissingColumnNames(), newCsvFormat.getAllowMissingColumnNames());
    }
}