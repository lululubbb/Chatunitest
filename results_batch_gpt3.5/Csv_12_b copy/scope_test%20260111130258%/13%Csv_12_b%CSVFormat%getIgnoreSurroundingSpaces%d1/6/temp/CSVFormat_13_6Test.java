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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;

public class CSVFormat_13_6Test {

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpaces() throws Exception {
        // Given
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(',', '"', null, null, null,
                true, true, "\r\n", null, null, false, false);

        // When
        boolean result = csvFormat.getIgnoreSurroundingSpaces();

        // Then
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpacesFalse() throws Exception {
        // Given
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(',', '"', null, null, null,
                false, true, "\r\n", null, null, false, false);

        // When
        boolean result = csvFormat.getIgnoreSurroundingSpaces();

        // Then
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpacesWithMock() throws Exception {
        // Given
        CSVFormat csvFormat = Mockito.spy(CSVFormat.DEFAULT);
        Mockito.when(csvFormat.getIgnoreSurroundingSpaces()).thenReturn(true);

        // When
        boolean result = csvFormat.getIgnoreSurroundingSpaces();

        // Then
        assertTrue(result);
    }
}