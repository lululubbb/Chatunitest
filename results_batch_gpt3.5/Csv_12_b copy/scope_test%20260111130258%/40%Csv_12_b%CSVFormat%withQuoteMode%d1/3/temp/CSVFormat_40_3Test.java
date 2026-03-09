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

class CSVFormat_40_3Test {

    @Test
    @Timeout(8000)
    void testWithQuoteMode() {
        // Given
        QuoteMode quoteMode = QuoteMode.ALL;
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withQuoteMode(quoteMode);

        // Then
        assertEquals(quoteMode, result.getQuoteMode());
    }

    // Add more test cases as needed for different scenarios

}