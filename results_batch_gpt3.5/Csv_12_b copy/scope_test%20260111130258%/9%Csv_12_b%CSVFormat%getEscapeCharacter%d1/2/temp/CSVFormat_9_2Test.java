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

import java.lang.reflect.Constructor;

public class CSVFormat_9_2Test {

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter() throws Exception {
        // Given
        char escapeChar = '\\';
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(',', '\"', null, null, escapeChar, false, true, "\r\n", null, null, false, true);

        // When
        Character actualEscapeCharacter = csvFormat.getEscapeCharacter();

        // Then
        assertEquals(escapeChar, actualEscapeCharacter.charValue());
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter_NullEscapeCharacter() throws Exception {
        // Given
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(',', '\"', null, null, null, false, true, "\r\n", null, null, false, true);

        // When
        Character actualEscapeCharacter = csvFormat.getEscapeCharacter();

        // Then
        assertEquals(null, actualEscapeCharacter);
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter_UsingMockito() throws Exception {
        // Given
        char escapeChar = '\\';
        CSVFormat csvFormat = mock(CSVFormat.class);
        when(csvFormat.getEscapeCharacter()).thenReturn(escapeChar);

        // When
        Character actualEscapeCharacter = csvFormat.getEscapeCharacter();

        // Then
        assertEquals(escapeChar, actualEscapeCharacter.charValue());
    }
}