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

import static org.junit.jupiter.api.Assertions.*;
import static org.apache.commons.csv.Constants.*;

public class CSVFormat_39_6Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_validChar() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote(DOUBLE_QUOTE_CHAR)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator(CRLF);
        Character quoteChar = '\'';

        // When
        CSVFormat newCsvFormat = csvFormat.withQuote(quoteChar);

        // Then
        assertEquals(quoteChar, newCsvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_lineBreakChar() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote(DOUBLE_QUOTE_CHAR)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator(CRLF);
        Character lineBreakChar = '\n';

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> csvFormat.withQuote(lineBreakChar));

        // Then
        assertEquals("The quoteChar cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_nullChar() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote(DOUBLE_QUOTE_CHAR)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator(CRLF);
        Character nullChar = null;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> csvFormat.withQuote(nullChar));

        // Then
        assertEquals("The quoteChar cannot be null", exception.getMessage());
    }

}