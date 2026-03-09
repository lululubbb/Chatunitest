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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_43_5Test {

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord() {
        // Given
        boolean skipHeaderRecord = true;
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withRecordSeparator("\r\n")
                .withSkipHeaderRecord(false)
                .withAllowMissingColumnNames(false);

        // When
        CSVFormat result = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordFalse() {
        // Given
        boolean skipHeaderRecord = false;
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withRecordSeparator("\r\n")
                .withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(false);

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
        CSVFormat csvFormat = Mockito.spy(CSVFormat.DEFAULT);

        // When
        Mockito.when(csvFormat.getSkipHeaderRecord()).thenReturn(skipHeaderRecord);
        CSVFormat result = csvFormat.withSkipHeaderRecord(skipHeaderRecord);

        // Then
        assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
    }
}