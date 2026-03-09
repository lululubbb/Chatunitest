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
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class CSVFormat_4_6Test {

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor() {
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
        String[] header = {"Header1", "Header2"};
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;

        // When
        CSVFormat csvFormat = invokeCSVFormatConstructor(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header,
                skipHeaderRecord, allowMissingColumnNames);

        // Then
        assertEquals(delimiter, csvFormat.getDelimiter());
        assertEquals(quoteChar, csvFormat.getQuoteCharacter());
        assertEquals(quoteMode, csvFormat.getQuoteMode());
        assertEquals(commentStart, csvFormat.getCommentMarker());
        assertEquals(escape, csvFormat.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, csvFormat.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, csvFormat.getIgnoreEmptyLines());
        assertEquals(recordSeparator, csvFormat.getRecordSeparator());
        assertEquals(nullString, csvFormat.getNullString());
        assertArrayEquals(header, csvFormat.getHeader());
        assertEquals(skipHeaderRecord, csvFormat.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, csvFormat.getAllowMissingColumnNames());
    }

    private CSVFormat invokeCSVFormatConstructor(char delimiter, Character quoteChar, QuoteMode quoteMode,
                                                  Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
                                                  boolean ignoreEmptyLines, String recordSeparator, String nullString,
                                                  String[] header, boolean skipHeaderRecord, boolean allowMissingColumnNames) {
        try {
            return (CSVFormat) CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                    Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                    String[].class, boolean.class, boolean.class)
                    .newInstance(delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces,
                            ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord, allowMissingColumnNames);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar() {
        // Given
        char lineBreakChar = '\n';

        // When
        boolean result = invokeIsLineBreakChar(lineBreakChar);

        // Then
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter() {
        // Given
        Character lineBreakCharacter = '\r';

        // When
        boolean result = invokeIsLineBreakCharacter(lineBreakCharacter);

        // Then
        assertTrue(result);
    }

    private boolean invokeIsLineBreakChar(char c) {
        try {
            return (boolean) CSVFormat.class.getDeclaredMethod("isLineBreak", char.class)
                    .invoke(null, c);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean invokeIsLineBreakCharacter(Character c) {
        try {
            return (boolean) CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class)
                    .invoke(null, c);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}