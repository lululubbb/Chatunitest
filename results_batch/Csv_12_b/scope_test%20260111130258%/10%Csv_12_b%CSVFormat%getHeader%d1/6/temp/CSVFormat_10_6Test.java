package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Reader;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;

public class CSVFormat_10_6Test {

    @Test
    @Timeout(8000)
    public void testGetHeader() {
        // Given
        String[] header = {"Name", "Age", "City"};
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withHeader(header)
                .build();

        // When
        String[] result = csvFormat.getHeader();

        // Then
        assertArrayEquals(header, result);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderWhenNull() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .build();

        // When
        String[] result = csvFormat.getHeader();

        // Then
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderClone() {
        // Given
        String[] header = {"Name", "Age", "City"};
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withHeader(header)
                .build();

        // When
        String[] result = csvFormat.getHeader();
        result = result.clone();
        result[0] = "Modified";

        // Then
        assertNotEquals(header[0], result[0]);
    }
}