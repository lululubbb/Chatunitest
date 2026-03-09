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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CSVFormat_42_3Test {

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Given
        char delimiter = ',';
        Character quoteChar = '"';
        QuoteMode quoteMode = null;
        Character commentStart = null;
        Character escape = null;
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class,
                QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header,
                skipHeaderRecord, allowMissingColumnNames);

        // When
        CSVFormat newCsvFormat = csvFormat.withRecordSeparator("\n");

        // Then
        assertEquals("\n", newCsvFormat.getRecordSeparator());
    }

    // Helper method to create a CSVFormat instance for testing private methods
    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
                                       Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
                                       boolean ignoreEmptyLines, String recordSeparator, String nullString, String[] header,
                                       boolean skipHeaderRecord, boolean allowMissingColumnNames) {
        return new CSVFormat(delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord, allowMissingColumnNames);
    }

}