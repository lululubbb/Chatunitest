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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CSVFormat_40_5Test {

    @Test
    @Timeout(8000)
    public void testWithQuoteMode() {
        // Create a mock CSVFormat object
        CSVFormat csvFormatMock = mock(CSVFormat.class);

        // Define the expected QuoteMode
        QuoteMode expectedQuoteMode = QuoteMode.ALL;

        // Mock the behavior of the private constructor
        when(csvFormatMock.withQuoteMode(expectedQuoteMode)).thenCallRealMethod();

        // Call the method under test
        CSVFormat result = csvFormatMock.withQuoteMode(expectedQuoteMode);

        // Validate the result
        assertEquals(expectedQuoteMode, result.getQuoteMode());
    }
}