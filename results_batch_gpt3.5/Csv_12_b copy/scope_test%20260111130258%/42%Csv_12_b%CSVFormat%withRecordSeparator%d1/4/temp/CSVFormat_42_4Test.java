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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class CSVFormat_42_4Test {

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

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header,
                skipHeaderRecord, allowMissingColumnNames);

        CSVFormat expected = constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header,
                skipHeaderRecord, allowMissingColumnNames);

        // When
        CSVFormat result = csvFormat.withRecordSeparator("\n");

        // Then
        assertEquals(expected.getDelimiter(), result.getDelimiter());
        assertEquals(expected.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(expected.getQuoteMode(), result.getQuoteMode());
        assertEquals(expected.getCommentMarker(), result.getCommentMarker());
        assertEquals(expected.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(expected.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(expected.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals("\n", result.getRecordSeparator());
        assertEquals(expected.getNullString(), result.getNullString());
        assertEquals(Arrays.toString(expected.getHeader()), Arrays.toString(result.getHeader()));
        assertEquals(expected.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(expected.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
    }
}