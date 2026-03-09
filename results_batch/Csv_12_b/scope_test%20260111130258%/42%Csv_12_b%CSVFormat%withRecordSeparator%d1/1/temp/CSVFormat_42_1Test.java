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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class CSVFormat_42_1Test {

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator() {
        // Given
        char delimiter = ',';
        Character quoteCharacter = '"';
        QuoteMode quoteMode = null;
        Character commentMarker = null;
        Character escapeCharacter = null;
        boolean ignoreSurroundingSpaces = false;
        boolean allowMissingColumnNames = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;

        CSVFormat csvFormat = CSVFormat.newFormat(delimiter)
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
        CSVFormat newCsvFormat = csvFormat.withRecordSeparator("\n");

        // Then
        assertEquals("\n", newCsvFormat.getRecordSeparator());
    }
}