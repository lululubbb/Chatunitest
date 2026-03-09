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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_10_5Test {

    @Test
    @Timeout(8000)
    public void testGetHeader() throws Exception {
        // Given
        String[] expectedHeader = {"Header1", "Header2"};
        CSVFormat csvFormat = createCSVFormat(',', '"', null, null, null, false, true, "\r\n", null, expectedHeader, false, true);

        // When
        String[] actualHeader = csvFormat.getHeader();

        // Then
        assertArrayEquals(expectedHeader, actualHeader);
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode, Character commentStart,
            Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines, String recordSeparator,
            String nullString, String[] header, boolean skipHeaderRecord, boolean allowMissingColumnNames) throws Exception {
        Class<?> csvFormatClass = Class.forName("org.apache.commons.csv.CSVFormat");
        Constructor<?> constructor = csvFormatClass.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return (CSVFormat) constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces, ignoreEmptyLines,
                recordSeparator, nullString, header, skipHeaderRecord, allowMissingColumnNames);
    }
}