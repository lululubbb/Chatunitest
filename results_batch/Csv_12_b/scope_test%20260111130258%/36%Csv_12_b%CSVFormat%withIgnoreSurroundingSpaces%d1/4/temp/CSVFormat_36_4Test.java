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

public class CSVFormat_36_4Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',').withQuote('"');
        boolean ignoreSurroundingSpaces = true;

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces(ignoreSurroundingSpaces);

        // Then
        assertEquals(ignoreSurroundingSpaces, result.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_DefaultValue() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean ignoreSurroundingSpaces = true;

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces(ignoreSurroundingSpaces);

        // Then
        assertEquals(ignoreSurroundingSpaces, result.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_RFC4180() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.RFC4180;
        boolean ignoreSurroundingSpaces = true;

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces(ignoreSurroundingSpaces);

        // Then
        assertEquals(ignoreSurroundingSpaces, result.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_EXCEL() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.EXCEL;
        boolean ignoreSurroundingSpaces = true;

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces(ignoreSurroundingSpaces);

        // Then
        assertEquals(ignoreSurroundingSpaces, result.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_TDF() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.TDF;
        boolean ignoreSurroundingSpaces = true;

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces(ignoreSurroundingSpaces);

        // Then
        assertEquals(ignoreSurroundingSpaces, result.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_MYSQL() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.MYSQL;
        boolean ignoreSurroundingSpaces = true;

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces(ignoreSurroundingSpaces);

        // Then
        assertEquals(ignoreSurroundingSpaces, result.getIgnoreSurroundingSpaces());
    }
}