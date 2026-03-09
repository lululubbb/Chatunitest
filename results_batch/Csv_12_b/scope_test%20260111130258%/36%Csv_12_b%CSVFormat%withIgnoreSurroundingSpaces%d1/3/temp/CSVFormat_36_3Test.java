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

import org.junit.jupiter.api.Test;

public class CSVFormat_36_3Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces() {
        // Given
        char delimiter = ',';
        Character quoteCharacter = '"';
        CSVFormat.QuoteMode quoteMode = null;
        Character commentMarker = null;
        Character escapeCharacter = null;
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        CSVFormat csvFormat = new CSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord,
                allowMissingColumnNames);

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces(ignoreSurroundingSpaces);

        // Then
        assertEquals(ignoreSurroundingSpaces, result.getIgnoreSurroundingSpaces());
    }

    // Helper class for mocking
    private static class QuoteMode {}

    // Helper class for mocking
    private static class CSVParser {
        public CSVParser parse(Reader in) throws IOException {
            return mock(CSVParser.class);
        }
    }

    // Helper class for mocking
    private static class CSVPrinter {
        public CSVPrinter print(Appendable out) throws IOException {
            return mock(CSVPrinter.class);
        }
    }

}