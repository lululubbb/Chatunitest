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
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class CSVFormat_25_3Test {

    @Test
    @Timeout(8000)
    public void testPrint() throws IOException {
        // Given
        Appendable out = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVPrinter csvPrinter = csvFormat.print(out);

        // Then
        assertEquals(CSVPrinter.class, csvPrinter.getClass());
    }

    @Test
    @Timeout(8000)
    public void testPrint_WithCustomFormat() throws IOException {
        // Given
        Appendable out = new StringWriter();
        char customDelimiter = '|';
        CSVFormat csvFormat = CSVFormat.newFormat(customDelimiter);

        // When
        CSVPrinter csvPrinter = csvFormat.print(out);

        // Then
        assertEquals(CSVPrinter.class, csvPrinter.getClass());
    }

    @Test
    @Timeout(8000)
    public void testPrint_IOException() {
        // Given
        Appendable out = mock(Appendable.class);
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        try {
            // When
            csvFormat.print(out);
        } catch (IOException e) {
            // Then
            assertEquals(IOException.class, e.getClass());
        }
    }

    @Test
    @Timeout(8000)
    public void testPrint_WithCustomFormatDelimiter() throws IOException {
        // Given
        Appendable out = new StringWriter();
        char customDelimiter = '|';
        CSVFormat csvFormat = CSVFormat.newFormat(customDelimiter);

        // When
        CSVPrinter csvPrinter = csvFormat.print(out);

        // Then
        assertEquals(CSVPrinter.class, csvPrinter.getClass());
    }

}
