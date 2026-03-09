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

public class CSVFormat_43_4Test {

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord() {
        // Given
        boolean skipHeaderRecord = true;
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withRecordSeparator("\r\n");

        // When
        CSVFormat result = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordUsingMockito() {
        // Given
        boolean skipHeaderRecord = true;
        CSVFormat csvFormat = mock(CSVFormat.class);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');
        when(csvFormat.getQuoteMode()).thenReturn(null);
        when(csvFormat.getCommentMarker()).thenReturn(null);
        when(csvFormat.getEscapeCharacter()).thenReturn(null);
        when(csvFormat.getIgnoreSurroundingSpaces()).thenReturn(false);
        when(csvFormat.getIgnoreEmptyLines()).thenReturn(false);
        when(csvFormat.getRecordSeparator()).thenReturn("\r\n");
        when(csvFormat.getNullString()).thenReturn(null);
        when(csvFormat.getHeader()).thenReturn(null);
        when(csvFormat.getSkipHeaderRecord()).thenReturn(false);
        when(csvFormat.getAllowMissingColumnNames()).thenReturn(false);

        // When
        CSVFormat result = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
    }
}