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
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

public class CSVFormat_39_4Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_WhenQuoteCharIsNotLineBreak_ExpectNewCSVFormatInstance() {
        // Given
        char delimiter = ',';
        char quoteChar = '"';
        QuoteMode quoteMode = null;
        Character commentMarker = null;
        Character escapeCharacter = null;
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        CSVFormat csvFormat = CSVFormat.newFormat(delimiter);

        // When
        CSVFormat result = csvFormat.withQuote(quoteChar);

        // Then
        assertNotNull(result);
        assertNotSame(csvFormat, result);
        assertEquals(delimiter, result.getDelimiter());
        assertEquals(quoteChar, result.getQuoteCharacter());
        assertEquals(quoteMode, result.getQuoteMode());
        assertEquals(commentMarker, result.getCommentMarker());
        assertEquals(escapeCharacter, result.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, result.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, result.getIgnoreEmptyLines());
        assertEquals(recordSeparator, result.getRecordSeparator());
        assertEquals(nullString, result.getNullString());
        assertArrayEquals(header, result.getHeader());
        assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_WhenQuoteCharIsLineBreak_ExpectIllegalArgumentException() {
        // Given
        char delimiter = ',';
        char quoteChar = '\n'; // Line break character
        CSVFormat csvFormat = CSVFormat.newFormat(delimiter);

        // When
        assertThrows(IllegalArgumentException.class, () -> {
            csvFormat.withQuote(quoteChar);
        });
    }
}