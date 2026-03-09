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

public class CSVFormat_34_4Test {

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean allowMissingColumnNames = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withAllowMissingColumnNames(allowMissingColumnNames);

        // Then
        assertEquals(allowMissingColumnNames, newCsvFormat.getAllowMissingColumnNames());
        assertEquals(csvFormat.getDelimiter(), newCsvFormat.getDelimiter());
        assertEquals(csvFormat.getQuoteCharacter(), newCsvFormat.getQuoteCharacter());
        assertEquals(csvFormat.getQuoteMode(), newCsvFormat.getQuoteMode());
        assertEquals(csvFormat.getCommentMarker(), newCsvFormat.getCommentMarker());
        assertEquals(csvFormat.getEscapeCharacter(), newCsvFormat.getEscapeCharacter());
        assertEquals(csvFormat.getIgnoreSurroundingSpaces(), newCsvFormat.getIgnoreSurroundingSpaces());
        assertEquals(csvFormat.getIgnoreEmptyLines(), newCsvFormat.getIgnoreEmptyLines());
        assertEquals(csvFormat.getRecordSeparator(), newCsvFormat.getRecordSeparator());
        assertEquals(csvFormat.getNullString(), newCsvFormat.getNullString());
        assertArrayEquals(csvFormat.getHeader(), newCsvFormat.getHeader());
        assertEquals(csvFormat.getSkipHeaderRecord(), newCsvFormat.getSkipHeaderRecord());
    }
}