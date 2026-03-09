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

public class CSVFormat_30_3Test {

    @Test
    @Timeout(8000)
    public void testWithDelimiter() {
        // Given
        char delimiter = ',';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newFormat = csvFormat.withDelimiter(delimiter);

        // Then
        assertNotSame(csvFormat, newFormat);
        assertEquals(delimiter, newFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_LineBreakDelimiter() {
        // Given
        char lineBreakDelimiter = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> csvFormat.withDelimiter(lineBreakDelimiter));
        assertEquals("The delimiter cannot be a line break", exception.getMessage());
    }

}