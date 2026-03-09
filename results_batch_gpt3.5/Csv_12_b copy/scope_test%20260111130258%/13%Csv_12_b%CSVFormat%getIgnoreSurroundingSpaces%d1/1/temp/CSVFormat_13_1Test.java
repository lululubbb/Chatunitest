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

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_13_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setup() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpaces() throws Exception {
        // Given
        boolean expected = false;

        // When
        boolean result = csvFormat.getIgnoreSurroundingSpaces();

        // Then
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpacesAfterSetting() throws Exception {
        // Given
        boolean expected = true;
        csvFormat = csvFormat.withIgnoreSurroundingSpaces(true);

        // When
        boolean result = csvFormat.getIgnoreSurroundingSpaces();

        // Then
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpacesWithMock() throws Exception {
        // Given
        boolean expected = true;
        CSVFormat csvFormatMock = mock(CSVFormat.class);
        when(csvFormatMock.getIgnoreSurroundingSpaces()).thenReturn(true);

        // When
        boolean result = csvFormatMock.getIgnoreSurroundingSpaces();

        // Then
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpacesUsingReflection() throws Exception {
        // Given
        boolean expected = true;

        // When
        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        ignoreSurroundingSpacesField.set(csvFormat, true);
        boolean result = csvFormat.getIgnoreSurroundingSpaces();

        // Then
        assertEquals(expected, result);
    }
}