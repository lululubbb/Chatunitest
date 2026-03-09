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

import org.junit.jupiter.api.Test;

public class CSVFormat_43_3Test {

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord() {
        // Given
        char delimiter = ',';
        Character quoteCharacter = '"';
        QuoteMode quoteMode = null;
        Character commentMarker = null;
        Character escapeCharacter = null;
        boolean ignoreSurroundingSpaces = false;
        boolean allowMissingColumnNames = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = true;

        CSVFormat csvFormat = createCSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, false, false);

        // When
        CSVFormat result = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
    }

    // Helper method to create CSVFormat instance using reflection for testing private constructor
    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode, Character commentStart,
            Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines, String recordSeparator,
            String nullString, String[] header, boolean skipHeaderRecord, boolean allowMissingColumnNames) {

        try {
            return CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                    Character.class, boolean.class, boolean.class, String.class, String.class, String[].class,
                    boolean.class, boolean.class).newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                            ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header,
                            skipHeaderRecord, allowMissingColumnNames);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}