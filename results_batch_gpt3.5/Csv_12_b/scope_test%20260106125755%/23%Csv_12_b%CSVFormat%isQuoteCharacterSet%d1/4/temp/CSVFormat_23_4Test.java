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

class CSVFormat_23_4Test {

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSetWhenQuoteCharacterIsSet() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSetWhenQuoteCharacterIsNull() throws Exception {
        // Use reflection to create CSVFormat instance with quoteCharacter null
        Class<?> quoteModeClass = Class.forName("org.apache.commons.csv.QuoteMode");
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class,
                quoteModeClass,
                Character.class, Character.class,
                boolean.class, boolean.class,
                String.class, String.class,
                String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // For the quoteMode parameter, pass null explicitly as the QuoteMode type
        CSVFormat format = constructor.newInstance(
                ',', null, null, null, null,
                false, false,
                "\n", null,
                null, false, false);

        assertFalse(format.isQuoteCharacterSet());
    }
}