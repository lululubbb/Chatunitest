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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_18_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setup() {
        csvFormat = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
    }

    @Test
    @Timeout(8000)
    public void testGetSkipHeaderRecord() {
        // Given
        boolean expectedSkipHeaderRecord = true;
        csvFormat = csvFormat.withSkipHeaderRecord(expectedSkipHeaderRecord);

        // When
        boolean actualSkipHeaderRecord = csvFormat.getSkipHeaderRecord();

        // Then
        assertEquals(expectedSkipHeaderRecord, actualSkipHeaderRecord);
    }

    @Test
    @Timeout(8000)
    public void testGetSkipHeaderRecordDefault() {
        // Given
        boolean expectedSkipHeaderRecord = false;

        // When
        boolean actualSkipHeaderRecord = csvFormat.getSkipHeaderRecord();

        // Then
        assertEquals(expectedSkipHeaderRecord, actualSkipHeaderRecord);
    }

    @Test
    @Timeout(8000)
    public void testGetSkipHeaderRecordUsingMockito() {
        // Given
        boolean expectedSkipHeaderRecord = true;
        CSVFormat csvFormatMock = mock(CSVFormat.class);
        when(csvFormatMock.getSkipHeaderRecord()).thenReturn(expectedSkipHeaderRecord);

        // When
        boolean actualSkipHeaderRecord = csvFormatMock.getSkipHeaderRecord();

        // Then
        assertEquals(expectedSkipHeaderRecord, actualSkipHeaderRecord);
    }
}