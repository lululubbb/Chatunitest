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
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_16_3Test {

    @Test
    @Timeout(8000)
    public void testGetQuoteMode() throws Exception {
        // Given
        CSVFormat csvFormat = createCSVFormat(',', '\"', QuoteMode.ALL, null, null, false, true, "\r\n", null, null, false, true);

        // When
        QuoteMode quoteMode = csvFormat.getQuoteMode();

        // Then
        assertEquals(QuoteMode.ALL, quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_Default() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        QuoteMode quoteMode = csvFormat.getQuoteMode();

        // Then
        assertEquals(null, quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_RFC4180() {
        // Given
        CSVFormat csvFormat = CSVFormat.RFC4180;

        // When
        QuoteMode quoteMode = csvFormat.getQuoteMode();

        // Then
        assertEquals(null, quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_Excel() {
        // Given
        CSVFormat csvFormat = CSVFormat.EXCEL;

        // When
        QuoteMode quoteMode = csvFormat.getQuoteMode();

        // Then
        assertEquals(null, quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_TDF() {
        // Given
        CSVFormat csvFormat = CSVFormat.TDF;

        // When
        QuoteMode quoteMode = csvFormat.getQuoteMode();

        // Then
        assertEquals(null, quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_MYSQL() {
        // Given
        CSVFormat csvFormat = CSVFormat.MYSQL;

        // When
        QuoteMode quoteMode = csvFormat.getQuoteMode();

        // Then
        assertEquals(null, quoteMode);
    }

    private CSVFormat createCSVFormat(char delimiter, char quoteChar, QuoteMode quoteMode, Character commentStart,
                                       Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                       String recordSeparator, String nullString, String[] header, boolean skipHeaderRecord,
                                       boolean allowMissingColumnNames) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord, allowMissingColumnNames);
    }
}