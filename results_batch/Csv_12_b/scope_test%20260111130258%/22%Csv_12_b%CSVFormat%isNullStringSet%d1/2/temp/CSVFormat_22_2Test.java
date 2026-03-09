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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVFormat_22_2Test {

    @Test
    @Timeout(8000)
    public void testIsNullStringSetNotNull() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withNullString("null")
                .withIgnoreEmptyLines(true)
                .withAllowMissingColumnNames(false);

        // When
        boolean result = csvFormat.isNullStringSet();

        // Then
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSetNull() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withIgnoreEmptyLines(true)
                .withAllowMissingColumnNames(false);

        // When
        boolean result = csvFormat.isNullStringSet();

        // Then
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withNullString("null")
                .withIgnoreEmptyLines(true)
                .withAllowMissingColumnNames(false);

        // When
        csvFormat.withCommentMarker('#');

        // Then
        assertEquals('#', csvFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withNullString("null")
                .withIgnoreEmptyLines(true)
                .withAllowMissingColumnNames(false);

        // When
        csvFormat.withDelimiter(';');

        // Then
        assertEquals(';', csvFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeCharacter() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withNullString("null")
                .withIgnoreEmptyLines(true)
                .withAllowMissingColumnNames(false);

        // When
        csvFormat.withEscape('\\');

        // Then
        assertEquals('\\', csvFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withNullString("null")
                .withIgnoreEmptyLines(true)
                .withAllowMissingColumnNames(false);

        // When
        csvFormat.withHeader("Header1", "Header2");

        // Then
        assertArrayEquals(new String[]{"Header1", "Header2"}, csvFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withNullString("null")
                .withIgnoreEmptyLines(true)
                .withAllowMissingColumnNames(false);

        // When
        csvFormat.withAllowMissingColumnNames(true);

        // Then
        assertTrue(csvFormat.getAllowMissingColumnNames());
    }

    // Add more test cases as needed to achieve full branch coverage

}