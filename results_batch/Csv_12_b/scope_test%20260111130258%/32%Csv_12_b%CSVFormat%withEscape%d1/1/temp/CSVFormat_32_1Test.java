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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class CSVFormat_32_1Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_validEscapeCharacter() {
        // Given
        char delimiter = ',';
        Character quoteCharacter = '"';
        QuoteMode quoteMode = null;
        Character commentMarker = null;
        Character escapeCharacter = '\\';
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        CSVFormat csvFormat = createCSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord,
                allowMissingColumnNames);

        // When
        CSVFormat result = csvFormat.withEscape('|');

        // Then
        assertNotNull(result);
        assertEquals('|', result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_invalidEscapeCharacter() {
        // Given
        char delimiter = ',';
        Character quoteCharacter = '"';
        QuoteMode quoteMode = null;
        Character commentMarker = null;
        Character escapeCharacter = '\\';
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        CSVFormat csvFormat = createCSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord,
                allowMissingColumnNames);

        // When/Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            csvFormat.withEscape('\n');
        });
        assertEquals("The escape character cannot be a line break", exception.getMessage());
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter, boolean ignoreSurroundingSpaces,
                                      boolean ignoreEmptyLines, String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord, boolean allowMissingColumnNames) {
        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class,
                    QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class,
                    String.class, String[].class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            return constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                    ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord,
                    allowMissingColumnNames);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}