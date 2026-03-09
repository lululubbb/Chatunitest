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

import org.junit.jupiter.api.Test;

public class CSVFormat_32_6Test {

    @Test
    @Timeout(8000)
    public void testWithEscapeValidCharacter() {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withEscape(escape);

        // Then
        assertEquals(escape, newCsvFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeLineBreakCharacter() {
        // Given
        char lineBreak = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            csvFormat.withEscape(lineBreak);
        });

        // Then
        assertEquals("The escape character cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeNullCharacter() {
        // Given
        char escape = '\0';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            csvFormat.withEscape(escape);
        });

        // Then
        assertEquals("The escape character cannot be null", exception.getMessage());
    }

}