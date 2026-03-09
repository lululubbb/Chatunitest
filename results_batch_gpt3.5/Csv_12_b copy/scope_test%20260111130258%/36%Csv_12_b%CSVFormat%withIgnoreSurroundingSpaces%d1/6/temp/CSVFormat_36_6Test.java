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

import org.junit.jupiter.api.Test;

public class CSVFormat_36_6Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces() throws Exception {
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

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces(true);

        // Then
        assertEquals(delimiter, result.getDelimiter());
        assertEquals(quoteChar, result.getQuoteCharacter());
        assertEquals(quoteMode, result.getQuoteMode());
        assertEquals(commentStart, result.getCommentMarker());
        assertEquals(escape, result.getEscapeCharacter());
        assertEquals(true, result.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, result.getIgnoreEmptyLines());
        assertEquals(recordSeparator, result.getRecordSeparator());
        assertEquals(nullString, result.getNullString());
        assertEquals(header, result.getHeader());
        assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, result.getAllowMissingColumnNames());
    }
}