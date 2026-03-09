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

public class CSVFormat_29_1Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_ValidCommentMarker_ReturnsNewCSVFormat() {
        // Given
        char delimiter = ',';
        Character quoteChar = '"';
        QuoteMode quoteMode = null;
        Character commentMarker = '#';
        Character escape = null;
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        CSVFormat csvFormat = CSVFormat.newFormat(delimiter)
                .withQuote(quoteChar)
                .withQuoteMode(quoteMode)
                .withEscape(escape)
                .withIgnoreSurroundingSpaces(ignoreSurroundingSpaces)
                .withIgnoreEmptyLines(ignoreEmptyLines)
                .withRecordSeparator(recordSeparator)
                .withNullString(nullString)
                .withHeader(header)
                .withSkipHeaderRecord(skipHeaderRecord)
                .withAllowMissingColumnNames(allowMissingColumnNames);

        // When
        CSVFormat newCSVFormat = csvFormat.withCommentMarker(commentMarker);

        // Then
        assertNotNull(newCSVFormat);
        assertEquals(delimiter, newCSVFormat.getDelimiter());
        assertEquals(quoteChar, newCSVFormat.getQuoteCharacter());
        assertEquals(quoteMode, newCSVFormat.getQuoteMode());
        assertEquals(commentMarker, newCSVFormat.getCommentMarker());
        assertEquals(escape, newCSVFormat.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, newCSVFormat.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, newCSVFormat.getIgnoreEmptyLines());
        assertEquals(recordSeparator, newCSVFormat.getRecordSeparator());
        assertEquals(nullString, newCSVFormat.getNullString());
        assertArrayEquals(header, newCSVFormat.getHeader());
        assertEquals(skipHeaderRecord, newCSVFormat.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, newCSVFormat.getAllowMissingColumnNames());
    }
}