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

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.Quote;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_15_2Test {

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_Default() throws Exception {
        // CSVFormat is immutable, so create a new instance via reflection to test modifying quotePolicy
        // Use reflection to create a new CSVFormat instance with quotePolicy = null

        // Get the constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        // Create a CSVFormat instance with quotePolicy = null
        CSVFormat format = constructor.newInstance(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteChar(),
                (Quote) null,
                CSVFormat.DEFAULT.getCommentStart(),
                CSVFormat.DEFAULT.getEscape(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord()
        );

        Quote quotePolicy = format.getQuotePolicy();
        assertNull(quotePolicy, "CSVFormat quotePolicy should be null");
    }

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_CustomQuotePolicy() {
        Quote customQuotePolicy = Quote.ALL;
        CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(customQuotePolicy);
        assertEquals(customQuotePolicy, format.getQuotePolicy());
    }

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_NullQuotePolicy() {
        CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(null);
        assertNull(format.getQuotePolicy());
    }
}