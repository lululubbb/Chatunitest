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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class CSVFormat_14_6Test {

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, CSVFormat.Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteChar, null, null, null,
                false, true, "\r\n", null, null, false);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharWhenQuoteCharIsNonNull() throws Exception {
        Character quoteChar = '"';
        CSVFormat format = createCSVFormat(',', quoteChar);
        assertEquals(quoteChar, format.getQuoteChar());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharWhenQuoteCharIsNull() throws Exception {
        CSVFormat format = createCSVFormat(',', null);
        assertNull(format.getQuoteChar());
    }
}