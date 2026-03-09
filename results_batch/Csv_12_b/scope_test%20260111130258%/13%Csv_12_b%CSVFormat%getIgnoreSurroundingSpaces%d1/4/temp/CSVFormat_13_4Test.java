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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CSVFormat_13_4Test {

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpaces() throws Exception {
        // Given
        CSVFormat csvFormat = createCSVFormat(',', '\"', null, null, null,
                true, true, "\r\n", null, null, false, false);

        // When
        boolean result = csvFormat.getIgnoreSurroundingSpaces();

        // Then
        assertEquals(true, result);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpaces_Default() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        boolean result = csvFormat.getIgnoreSurroundingSpaces();

        // Then
        assertEquals(true, result);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpaces_RFC4180() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.RFC4180;

        // When
        boolean result = csvFormat.getIgnoreSurroundingSpaces();

        // Then
        assertEquals(false, result);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpaces_TDF() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.TDF;

        // When
        boolean result = csvFormat.getIgnoreSurroundingSpaces();

        // Then
        assertEquals(true, result);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpaces_MYSQL() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.MYSQL;

        // When
        boolean result = csvFormat.getIgnoreSurroundingSpaces();

        // Then
        assertEquals(false, result);
    }

    private CSVFormat createCSVFormat(char delimiter, char quoteChar, QuoteMode quoteMode, Character commentStart,
                                      Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header, boolean skipHeaderRecord,
                                      boolean allowMissingColumnNames) throws NoSuchMethodException, IllegalAccessException,
                                      InvocationTargetException, InstantiationException {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class,
                QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord, allowMissingColumnNames);
    }
}