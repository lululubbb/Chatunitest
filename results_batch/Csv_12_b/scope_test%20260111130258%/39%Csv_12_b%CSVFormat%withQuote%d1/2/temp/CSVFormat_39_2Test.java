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
import java.lang.reflect.InvocationTargetException;

public class CSVFormat_39_2Test {

    @Test
    @Timeout(8000)
    public void testWithQuote() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Given
        char delimiter = ',';
        Character quoteChar = '"';
        QuoteMode quoteMode = QuoteMode.ALL;
        Character commentMarker = '#';
        Character escapeCharacter = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = {"Header1", "Header2"};
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                    Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            CSVFormat csvFormat = constructor.newInstance(delimiter, quoteChar, quoteMode, commentMarker, escapeCharacter,
                    ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord,
                    allowMissingColumnNames);

            char newQuoteChar = '\'';

            // When
            CSVFormat newCsvFormat = csvFormat.withQuote(newQuoteChar);

            // Then
            assertNotEquals(csvFormat, newCsvFormat);
            assertEquals(delimiter, newCsvFormat.getDelimiter());
            assertEquals(newQuoteChar, newCsvFormat.getQuoteCharacter());
            assertEquals(quoteMode, newCsvFormat.getQuoteMode());
            assertEquals(commentMarker, newCsvFormat.getCommentMarker());
            assertEquals(escapeCharacter, newCsvFormat.getEscapeCharacter());
            assertEquals(ignoreSurroundingSpaces, newCsvFormat.getIgnoreSurroundingSpaces());
            assertEquals(ignoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
            assertEquals(recordSeparator, newCsvFormat.getRecordSeparator());
            assertEquals(nullString, newCsvFormat.getNullString());
            assertArrayEquals(header, newCsvFormat.getHeader());
            assertEquals(skipHeaderRecord, newCsvFormat.getSkipHeaderRecord());
            assertEquals(allowMissingColumnNames, newCsvFormat.getAllowMissingColumnNames());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}