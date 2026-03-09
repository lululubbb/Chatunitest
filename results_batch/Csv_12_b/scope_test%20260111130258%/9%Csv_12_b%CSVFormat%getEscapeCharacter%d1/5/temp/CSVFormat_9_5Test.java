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

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_9_5Test {

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter() {
        // Given
        char escapeChar = '\\';
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withEscape(escapeChar)
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withRecordSeparator("\r\n");

        // When
        Character result = csvFormat.getEscapeCharacter();

        // Then
        assertEquals(Character.valueOf(escapeChar), result);
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacterWithNull() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withRecordSeparator("\r\n");

        // When
        Character result = csvFormat.getEscapeCharacter();

        // Then
        assertEquals(null, result);
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacterWithMockito() throws Exception {
        // Given
        char escapeChar = '\\';
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withEscape(escapeChar)
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withRecordSeparator("\r\n");
        CSVFormat spyFormat = Mockito.spy(csvFormat);

        // When
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        escapeCharacterField.set(spyFormat, escapeChar);

        // Then
        Character result = spyFormat.getEscapeCharacter();
        assertEquals(Character.valueOf(escapeChar), result);
    }
}