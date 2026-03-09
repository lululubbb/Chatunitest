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

public class CSVFormat_16_4Test {

    @Test
    @Timeout(8000)
    public void testGetQuoteMode() throws Exception {
        // Given
        CSVFormat csvFormat = (CSVFormat) CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class)
                .newInstance(',', '"', QuoteMode.ALL, '#', '\\', true, false, "\r\n",
                        "NA", new String[]{"A", "B", "C"}, true, true);

        // When
        QuoteMode quoteMode = csvFormat.getQuoteMode();

        // Then
        assertEquals(QuoteMode.ALL, quoteMode);
    }

}