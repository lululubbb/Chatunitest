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

class CSVFormatIsQuotingTest {

    @Test
    @Timeout(8000)
    void testIsQuoting_whenQuoteCharIsNonNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.isQuoting());
    }

    @Test
    @Timeout(8000)
    void testIsQuoting_whenQuoteCharIsNull() throws Exception {
        // Use reflection to create a CSVFormat instance with quoteChar = null
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                Quote.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                String[].class,
                boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                CSVFormat.DEFAULT.getDelimiter(),
                null,
                null,
                null,
                null,
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(format.isQuoting());
    }
}