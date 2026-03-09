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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

public class CSVFormat_36_2Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces() {
        // Given
        char delimiter = ',';
        Character quoteChar = '"';
        QuoteMode quoteMode = null;
        Character commentStart = null;
        Character escape = null;
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        CSVFormat csvFormat = CSVFormat.newFormat(delimiter)
                .withQuote(quoteChar)
                .withQuoteMode(quoteMode)
                .withCommentMarker(commentStart)
                .withEscape(escape)
                .withIgnoreSurroundingSpaces(ignoreSurroundingSpaces)
                .withIgnoreEmptyLines(ignoreEmptyLines)
                .withRecordSeparator(recordSeparator)
                .withNullString(nullString)
                .withHeader(header)
                .withSkipHeaderRecord(skipHeaderRecord)
                .withAllowMissingColumnNames(allowMissingColumnNames);

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces(false);

        // Then
        assertEquals(delimiter, result.getDelimiter());
        assertEquals(quoteChar, result.getQuoteCharacter());
        assertEquals(quoteMode, result.getQuoteMode());
        assertEquals(commentStart, result.getCommentMarker());
        assertEquals(escape, result.getEscapeCharacter());
        assertEquals(false, result.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, result.getIgnoreEmptyLines());
        assertEquals(recordSeparator, result.getRecordSeparator());
        assertEquals(nullString, result.getNullString());
        assertEquals(header, result.getHeader());
        assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, result.getAllowMissingColumnNames());
    }
}