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
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.Reader;

public class CSVFormat_23_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setup() {
        csvFormat = CSVFormat.newFormat(',').withQuote('"').build();
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSetWithQuoteCharacterSet() {
        assertTrue(csvFormat.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSetWithoutQuoteCharacterSet() {
        CSVFormat csvFormatWithoutQuote = CSVFormat.newFormat(',').build();
        assertTrue(!csvFormatWithoutQuote.isQuoteCharacterSet());
    }

    // Add more test cases for different scenarios if needed

}