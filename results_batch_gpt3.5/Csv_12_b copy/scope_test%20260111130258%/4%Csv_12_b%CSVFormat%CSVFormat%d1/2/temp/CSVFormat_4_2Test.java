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

public class CSVFormat_4_2Test {

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
        CSVFormat csvFormat = CSVFormat.DEFAULT;

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

    @Test
    @Timeout(8000)
    public void testIsLineBreak() {
        assertTrue(isLineBreak('\n'));
        assertFalse(isLineBreak('a'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter() {
        assertTrue(isLineBreak('\r'));
        assertFalse(isLineBreak('b'));
    }

    @Test
    @Timeout(8000)
    public void testNewFormat() {
        // Given
        char delimiter = ',';

        // When
        CSVFormat csvFormat = CSVFormat.newFormat(delimiter);

        // Then
        assertEquals(delimiter, csvFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testFormat() {
        // Given
        String expected = "value1,value2";
        Object[] values = {"value1", "value2"};

        // When
        String result = CSVFormat.DEFAULT.format(values);

        // Then
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    public void testGettersAndSetters() {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        assertEquals(Character.valueOf('"'), csvFormat.getQuoteCharacter());
        assertEquals(true, csvFormat.getIgnoreEmptyLines());

        csvFormat = csvFormat.withQuote(null).withIgnoreEmptyLines(false);

        assertNull(csvFormat.getQuoteCharacter());
        assertEquals(false, csvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testParse() throws Exception {
        // Given
        String data = "John,Doe\nJane,Smith";
        Reader reader = new StringReader(data);

        // When
        CSVParser csvParser = CSVFormat.DEFAULT.parse(reader);

        // Then
        assertNotNull(csvParser);
    }

    @Test
    @Timeout(8000)
    public void testPrint() throws Exception {
        // Given
        StringWriter writer = new StringWriter();

        // When
        CSVPrinter csvPrinter = CSVFormat.DEFAULT.print(writer);

        // Then
        assertNotNull(csvPrinter);
    }

    private static boolean isLineBreak(char c) {
        return CSVFormat.isLineBreak(c);
    }

    private static boolean isLineBreak(Character c) {
        return CSVFormat.isLineBreak(c.charValue());
    }
}