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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CSVFormat_39_1Test {

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
        boolean allowMissingColumnNames = true;

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(delimiter, quoteChar, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord,
                allowMissingColumnNames);

        // When
        CSVFormat result = csvFormat.withQuote('"');

        // Then
        assertEquals(delimiter, result.getDelimiter());
        assertEquals(Character.valueOf('"'), result.getQuoteCharacter());
        assertEquals(quoteMode, result.getQuoteMode());
        assertEquals(commentMarker, result.getCommentMarker());
        assertEquals(escapeCharacter, result.getEscapeCharacter());
        assertTrue(result.getIgnoreSurroundingSpaces());
        assertFalse(result.getIgnoreEmptyLines());
        assertEquals(recordSeparator, result.getRecordSeparator());
        assertNull(result.getNullString());
        assertArrayEquals(header, result.getHeader());
        assertFalse(result.getSkipHeaderRecord());
        assertTrue(result.getAllowMissingColumnNames());
    }
}