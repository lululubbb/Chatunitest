package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class CSVFormat_8_3Test {

    @Test
    @Timeout(8000)
    public void testGetDelimiter() {
        // Given
        char expectedDelimiter = ',';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiterCustom() {
        // Given
        char expectedDelimiter = '\t';
        CSVFormat csvFormat = CSVFormat.newFormat('\t');

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiterUsingMock() {
        // Given
        char expectedDelimiter = ',';
        CSVFormat csvFormat = mock(CSVFormat.class);
        when(csvFormat.getDelimiter()).thenReturn(',');

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }
}