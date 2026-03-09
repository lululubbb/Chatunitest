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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class CSVFormat_22_6Test {

    @Test
    @Timeout(8000)
    public void testIsNullStringSetNotNull() {
        // Given
        CSVFormat csvFormat = createCSVFormat(',', '\"', null, null, null, false, true, "\r\n", "NULL", null, false, false);

        // When
        boolean result = csvFormat.isNullStringSet();

        // Then
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSetNull() {
        // Given
        CSVFormat csvFormat = createCSVFormat(',', '\"', null, null, null, false, true, "\r\n", null, null, false, false);

        // When
        boolean result = csvFormat.isNullStringSet();

        // Then
        assertFalse(result);
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode, Character commentStart,
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