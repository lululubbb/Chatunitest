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

import org.junit.jupiter.api.Test;

public class CSVFormat_9_4Test {

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter() throws Exception {
        // Given
        char expectedEscapeCharacter = '\\';
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withEscape(expectedEscapeCharacter)
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withRecordSeparator("\r\n");

        // When
        Character actualEscapeCharacter = csvFormat.getEscapeCharacter();

        // Then
        assertEquals(expectedEscapeCharacter, actualEscapeCharacter);
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter_Null() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withRecordSeparator("\r\n");

        // When
        Character actualEscapeCharacter = csvFormat.getEscapeCharacter();

        // Then
        assertEquals(null, actualEscapeCharacter);
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter_PrivateMethod() throws Exception {
        // Given
        char expectedEscapeCharacter = '\\';
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withEscape(expectedEscapeCharacter)
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withRecordSeparator("\r\n");

        CSVFormat csvFormatSpy = mock(CSVFormat.class);
        when(csvFormatSpy.getEscapeCharacter()).thenCallRealMethod();

        // When
        Character actualEscapeCharacter = (Character) csvFormatSpy.getClass().getDeclaredMethod("getEscapeCharacter").invoke(csvFormatSpy);

        // Then
        assertEquals(expectedEscapeCharacter, actualEscapeCharacter.charValue());
    }
}