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

import org.junit.jupiter.api.Test;

public class CSVFormat_37_3Test {

    @Test
    @Timeout(8000)
    public void testWithNullString() {
        // Given
        String nullString = "NULL";
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withNullString(nullString);

        // Then
        assertEquals(nullString, newCsvFormat.getNullString());
        assertEquals(',', newCsvFormat.getDelimiter());
        assertEquals('\"', newCsvFormat.getQuoteCharacter());
        assertEquals(null, newCsvFormat.getCommentMarker());
        assertEquals(null, newCsvFormat.getEscapeCharacter());
        assertEquals(true, newCsvFormat.getIgnoreSurroundingSpaces());
        assertEquals(true, newCsvFormat.getIgnoreEmptyLines());
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), newCsvFormat.getRecordSeparator());
        assertEquals(null, newCsvFormat.getHeader());
        assertEquals(false, newCsvFormat.getSkipHeaderRecord());
        assertEquals(false, newCsvFormat.getAllowMissingColumnNames());
    }
}