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

public class CSVFormat_29_6Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker() {
        // Given
        char delimiter = ',';
        char quoteChar = '"';
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
        CSVFormat result = csvFormat.withCommentMarker(commentMarker);

        // Then
        assertNotNull(result);
        assertEquals(delimiter, result.getDelimiter());
        assertEquals(quoteChar, result.getQuoteCharacter());
        assertEquals(quoteMode, result.getQuoteMode());
        assertEquals(commentMarker, result.getCommentMarker());
        assertEquals(escape, result.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, result.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, result.getIgnoreEmptyLines());
        assertEquals(recordSeparator, result.getRecordSeparator());
        assertEquals(nullString, result.getNullString());
        assertArrayEquals(header, result.getHeader());
        assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, result.getAllowMissingColumnNames());
    }
}