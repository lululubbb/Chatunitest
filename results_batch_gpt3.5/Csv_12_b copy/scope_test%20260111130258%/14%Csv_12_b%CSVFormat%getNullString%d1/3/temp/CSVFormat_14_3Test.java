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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class CSVFormat_14_3Test {

    @Test
    @Timeout(8000)
    public void testGetNullString() {
        // Given
        CSVFormat csvFormat = createCSVFormat(',', '\"', null, null, null, false, true, "\r\n", "NULL", null, false, false);

        // When
        String nullString = csvFormat.getNullString();

        // Then
        assertEquals("NULL", nullString);
    }

    @Test
    @Timeout(8000)
    public void testGetNullStringWithDefaultFormat() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        String nullString = csvFormat.getNullString();

        // Then
        assertEquals(null, nullString);
    }

    @Test
    @Timeout(8000)
    public void testGetNullStringWithMockedFormat() {
        // Given
        CSVFormat csvFormat = mock(CSVFormat.class);
        when(csvFormat.getNullString()).thenReturn("MockedNull");

        // When
        String nullString = csvFormat.getNullString();

        // Then
        assertEquals("MockedNull", nullString);
    }

    private CSVFormat createCSVFormat(char delimiter, char quoteChar, QuoteMode quoteMode, Character commentStart,
            Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines, String recordSeparator,
            String nullString, String[] header, boolean skipHeaderRecord, boolean allowMissingColumnNames) {
        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class,
                    QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class,
                    String.class, String[].class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces,
                    ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord, allowMissingColumnNames);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}