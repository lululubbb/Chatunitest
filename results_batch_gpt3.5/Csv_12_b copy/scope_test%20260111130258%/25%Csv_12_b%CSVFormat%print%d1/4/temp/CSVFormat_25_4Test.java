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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

public class CSVFormat_25_4Test {

    @Test
    @Timeout(8000)
    public void testPrint() throws IOException {
        // Given
        StringWriter out = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVPrinter csvPrinter = csvFormat.print(out);

        // Then
        assertNotNull(csvPrinter);
    }
}