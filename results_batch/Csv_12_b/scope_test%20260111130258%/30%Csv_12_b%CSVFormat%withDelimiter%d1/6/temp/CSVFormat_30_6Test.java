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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CSVFormat_30_6Test {

    @Test
    @Timeout(8000)
    public void testWithDelimiter_validDelimiter() {
        // Given
        char delimiter = ',';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withDelimiter(delimiter);

        // Then
        assertEquals(delimiter, result.getDelimiter());
        assertEquals(csvFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(csvFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(csvFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(csvFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(csvFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(csvFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(csvFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(csvFormat.getNullString(), result.getNullString());
        assertArrayEquals(csvFormat.getHeader(), result.getHeader());
        assertEquals(csvFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(csvFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_invalidDelimiter() {
        // Given
        char lineBreakDelimiter = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> csvFormat.withDelimiter(lineBreakDelimiter));

        // Then
        assertEquals("The delimiter cannot be a line break", exception.getMessage());
    }
}