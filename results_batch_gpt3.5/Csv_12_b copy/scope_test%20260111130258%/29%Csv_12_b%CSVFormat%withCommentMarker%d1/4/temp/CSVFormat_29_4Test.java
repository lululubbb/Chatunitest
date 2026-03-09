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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CSVFormat_29_4Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker() {
        // Given
        char delimiter = ',';
        Character quoteCharacter = '"';
        QuoteMode quoteMode = null;
        Character commentMarker = '#';
        Character escapeCharacter = null;
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        CSVFormat csvFormat = CSVFormat.newFormat(delimiter)
                .withQuote(quoteCharacter)
                .withQuoteMode(quoteMode)
                .withEscape(escapeCharacter)
                .withIgnoreSurroundingSpaces(ignoreSurroundingSpaces)
                .withIgnoreEmptyLines(ignoreEmptyLines)
                .withRecordSeparator(recordSeparator)
                .withNullString(nullString)
                .withHeader(header)
                .withSkipHeaderRecord(skipHeaderRecord)
                .withAllowMissingColumnNames(allowMissingColumnNames);

        CSVFormat newCsvFormat = CSVFormat.newFormat(delimiter)
                .withQuote(quoteCharacter)
                .withQuoteMode(quoteMode)
                .withCommentMarker(commentMarker)
                .withEscape(escapeCharacter)
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
        assertEquals(newCsvFormat, result);
    }

    // Add more test cases for boundary conditions and edge cases if necessary
}