package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class CSVFormat_23_2Test {

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet() {
        // Given
        char delimiter = ',';
        char quoteChar = '"';
        CSVFormat csvFormat = createCSVFormat(delimiter, quoteChar, null, null, null,
                false, true, "\r\n", null, null, false, true);

        // When
        boolean result = csvFormat.isQuoteCharacterSet();

        // Then
        assertTrue(result);
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
            Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
            boolean ignoreEmptyLines, String recordSeparator, String nullString, String[] header,
            boolean skipHeaderRecord, boolean allowMissingColumnNames) {
        try {
            java.lang.reflect.Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class,
                    Character.class, QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class,
                    String.class, String.class, String[].class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                    ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord,
                    allowMissingColumnNames);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to set private fields using reflection
    private void setPrivateField(Object targetObject, String fieldName, Object newValue) {
        try {
            java.lang.reflect.Field field = targetObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(targetObject, newValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get private field value using reflection
    private Object getPrivateField(Object targetObject, String fieldName) {
        try {
            java.lang.reflect.Field field = targetObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(targetObject);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Additional test for coverage
    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSetWithNullQuoteCharacter() {
        // Given
        char delimiter = ',';
        CSVFormat csvFormat = createCSVFormat(delimiter, null, null, null, null,
                false, true, "\r\n", null, null, false, true);

        // When
        boolean result = csvFormat.isQuoteCharacterSet();

        // Then
        assertFalse(result);
    }
}