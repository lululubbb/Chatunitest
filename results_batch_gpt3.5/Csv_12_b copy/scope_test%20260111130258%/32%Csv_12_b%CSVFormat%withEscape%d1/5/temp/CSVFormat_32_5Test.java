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

import java.lang.reflect.Constructor;

public class CSVFormat_32_5Test {

    @Test
    @Timeout(8000)
    public void testWithEscapeValid() {
        // Given
        char delimiter = ',';
        char quoteChar = '"';
        QuoteMode quoteMode = null;
        char commentStart = '#';
        char escape = '\\';
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withEscape('|');

        // Then
        assertEquals('|', result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeLineBreak() {
        // Given
        char delimiter = ',';
        char quoteChar = '"';
        QuoteMode quoteMode = null;
        char commentStart = '#';
        char escape = '\\';
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> {
            csvFormat.withEscape('\n');
        });
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeLineBreakValid() {
        // Given
        char delimiter = ',';
        char quoteChar = '"';
        QuoteMode quoteMode = null;
        char commentStart = '#';
        char escape = '\\';
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withEscape('|');

        // Then
        assertEquals('|', result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeLineBreakException() {
        // Given
        char delimiter = ',';
        char quoteChar = '"';
        QuoteMode quoteMode = null;
        char commentStart = '#';
        char escape = '\\';
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> {
            csvFormat.withEscape('\r');
        });
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeLineBreakValid2() {
        // Given
        char delimiter = ',';
        char quoteChar = '"';
        QuoteMode quoteMode = null;
        char commentStart = '#';
        char escape = '\\';
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withEscape('|');

        // Then
        assertEquals('|', result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeLineBreakException2() {
        // Given
        char delimiter = ',';
        char quoteChar = '"';
        QuoteMode quoteMode = null;
        char commentStart = '#';
        char escape = '\\';
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> {
            csvFormat.withEscape('\r');
        });
    }

    // Additional tests can be added for better coverage

}