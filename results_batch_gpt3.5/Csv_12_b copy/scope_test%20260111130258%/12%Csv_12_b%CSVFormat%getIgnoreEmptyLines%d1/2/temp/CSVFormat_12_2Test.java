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

import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class CSVFormat_12_2Test {

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();

        // Then
        assertTrue(ignoreEmptyLines);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesCustom() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withIgnoreSurroundingSpaces(false)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n")
                .withAllowMissingColumnNames(false)
                .withSkipHeaderRecord(false);

        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();

        // Then
        assertTrue(ignoreEmptyLines);
    }

    // Additional tests for branch coverage
    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesRFC4180() {
        // Given
        CSVFormat csvFormat = CSVFormat.RFC4180;

        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();

        // Then
        assertFalse(ignoreEmptyLines);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesExcel() {
        // Given
        CSVFormat csvFormat = CSVFormat.EXCEL;

        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();

        // Then
        assertFalse(ignoreEmptyLines);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesTDF() {
        // Given
        CSVFormat csvFormat = CSVFormat.TDF;

        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();

        // Then
        assertTrue(ignoreEmptyLines);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesMYSQL() {
        // Given
        CSVFormat csvFormat = CSVFormat.MYSQL;

        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();

        // Then
        assertFalse(ignoreEmptyLines);
    }
}