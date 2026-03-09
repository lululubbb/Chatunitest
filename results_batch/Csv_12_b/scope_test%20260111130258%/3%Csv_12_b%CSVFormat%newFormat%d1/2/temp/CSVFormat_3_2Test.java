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
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class CSVFormat_3_2Test {

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
    }

    @Test
    @Timeout(8000)
    public void testNewFormatWithDelimiterTab() {
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
    public void testNewFormatWithDelimiterPipe() {
        // Given
        char delimiter = '|';

        // When
        CSVFormat csvFormat = CSVFormat.newFormat(delimiter);

        // Then
        assertNotNull(csvFormat);
        assertEquals(delimiter, csvFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testNewFormatWithDelimiterOther() {
        // Given
        char delimiter = ';';

        // When
        CSVFormat csvFormat = CSVFormat.newFormat(delimiter);

        // Then
        assertNotNull(csvFormat);
        assertEquals(delimiter, csvFormat.getDelimiter());
    }
}