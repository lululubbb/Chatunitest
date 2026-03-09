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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;

public class CSVFormat_3_1Test {

    @Test
    @Timeout(8000)
    public void testNewFormat() {
        // Given
        char delimiter = ',';

        // When
        CSVFormat csvFormat = CSVFormat.newFormat(delimiter);

        // Then
        assertNotNull(csvFormat);
        assertEquals(delimiter, csvFormat.getDelimiter());
        assertNull(csvFormat.getQuoteCharacter());
        assertNull(csvFormat.getQuoteMode());
        assertNull(csvFormat.getCommentMarker());
        assertNull(csvFormat.getEscapeCharacter());
        assertFalse(csvFormat.getIgnoreSurroundingSpaces());
        assertFalse(csvFormat.getAllowMissingColumnNames());
        assertFalse(csvFormat.getIgnoreEmptyLines());
        assertNull(csvFormat.getRecordSeparator());
        assertNull(csvFormat.getNullString());
        assertNull(csvFormat.getHeader());
        assertFalse(csvFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testNewFormat_CustomDelimiter() {
        // Given
        char delimiter = ';';

        // When
        CSVFormat csvFormat = CSVFormat.newFormat(delimiter);

        // Then
        assertNotNull(csvFormat);
        assertEquals(delimiter, csvFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testNewFormat_NullDelimiter() {
        // When
        CSVFormat csvFormat = CSVFormat.newFormat('\0');

        // Then
        assertNotNull(csvFormat);
        assertEquals('\0', csvFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testNewFormat_SpecialCharactersDelimiter() {
        // Given
        char delimiter = '\t';

        // When
        CSVFormat csvFormat = CSVFormat.newFormat(delimiter);

        // Then
        assertNotNull(csvFormat);
        assertEquals(delimiter, csvFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testNewFormat_EqualObjects() {
        // Given
        char delimiter = ',';

        // When
        CSVFormat csvFormat1 = CSVFormat.newFormat(delimiter);
        CSVFormat csvFormat2 = CSVFormat.newFormat(delimiter);

        // Then
        assertEquals(csvFormat1, csvFormat2);
    }

    @Test
    @Timeout(8000)
    public void testNewFormat_NotEqualObjects() {
        // When
        CSVFormat csvFormat1 = CSVFormat.newFormat(',');
        CSVFormat csvFormat2 = CSVFormat.newFormat(';');

        // Then
        assertNotEquals(csvFormat1, csvFormat2);
    }

}