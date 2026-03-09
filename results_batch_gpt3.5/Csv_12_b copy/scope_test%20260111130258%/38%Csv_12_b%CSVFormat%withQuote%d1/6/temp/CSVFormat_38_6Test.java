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

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class CSVFormat_38_6Test {

    @Test
    @Timeout(8000)
    public void testWithQuote() {
        // Given
        char quoteChar = '"';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withQuote(quoteChar);

        // Then
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_Null() {
        // Given
        char quoteChar = '\0'; // null character
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withQuote(quoteChar);

        // Then
        assertEquals(null, result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_Mockito() {
        // Given
        char quoteChar = '"';
        CSVFormat csvFormat = mock(CSVFormat.class);
        when(csvFormat.withQuote(quoteChar)).thenReturn(csvFormat);

        // When
        CSVFormat result = csvFormat.withQuote(quoteChar);

        // Then
        assertEquals(result, csvFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_Reflection() throws Exception {
        // Given
        char quoteChar = '"';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withQuote(quoteChar);

        // Then
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        Character quoteCharacter = (Character) quoteCharacterField.get(result);
        assertEquals(Character.valueOf(quoteChar), quoteCharacter);
    }
}