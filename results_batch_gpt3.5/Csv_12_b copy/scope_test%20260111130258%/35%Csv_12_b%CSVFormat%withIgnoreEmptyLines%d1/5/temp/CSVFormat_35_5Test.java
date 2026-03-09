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
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class CSVFormat_35_5Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean ignoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertNotSame(csvFormat, newCsvFormat);
        assertEquals(ignoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_ReturnsNewInstance() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean ignoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertNotSame(csvFormat, newCsvFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_False() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean ignoreEmptyLines = false;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertNotSame(csvFormat, newCsvFormat);
        assertEquals(ignoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_DefaultInstance() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(true);

        // Then
        assertNotSame(csvFormat, newCsvFormat);
        assertFalse(newCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_StaticInstances() {
        // Given
        boolean ignoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat1 = CSVFormat.RFC4180.withIgnoreEmptyLines(ignoreEmptyLines);
        CSVFormat newCsvFormat2 = CSVFormat.EXCEL.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertTrue(newCsvFormat1.getIgnoreEmptyLines());
        assertTrue(newCsvFormat2.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_StaticInstances_False() {
        // Given
        boolean ignoreEmptyLines = false;

        // When
        CSVFormat newCsvFormat1 = CSVFormat.RFC4180.withIgnoreEmptyLines(ignoreEmptyLines);
        CSVFormat newCsvFormat2 = CSVFormat.EXCEL.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertFalse(newCsvFormat1.getIgnoreEmptyLines());
        assertFalse(newCsvFormat2.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_TDF() {
        // Given
        CSVFormat csvFormat = CSVFormat.TDF;
        boolean ignoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertNotSame(csvFormat, newCsvFormat);
        assertEquals(ignoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_MYSQL() {
        // Given
        CSVFormat csvFormat = CSVFormat.MYSQL;
        boolean ignoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertNotSame(csvFormat, newCsvFormat);
        assertEquals(ignoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }
}