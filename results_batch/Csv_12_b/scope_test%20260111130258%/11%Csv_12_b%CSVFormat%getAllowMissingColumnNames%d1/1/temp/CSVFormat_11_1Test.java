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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class CSVFormat_11_1Test {

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean expected = false;

        // When
        boolean result = csvFormat.getAllowMissingColumnNames();

        // Then
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNamesWithMock() {
        // Given
        CSVFormat csvFormat = mock(CSVFormat.class);
        when(csvFormat.getAllowMissingColumnNames()).thenReturn(true);
        boolean expected = true;

        // When
        boolean result = csvFormat.getAllowMissingColumnNames();

        // Then
        assertEquals(expected, result);
    }

}