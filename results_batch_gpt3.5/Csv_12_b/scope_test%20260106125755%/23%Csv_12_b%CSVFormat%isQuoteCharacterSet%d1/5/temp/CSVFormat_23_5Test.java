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
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_23_5Test {

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // DEFAULT has DOUBLE_QUOTE_CHAR as quoteCharacter, so isQuoteCharacterSet() should be true
        assertTrue(format.isQuoteCharacterSet());

        // Use reflection to forcibly set quoteCharacter to a non-null Character
        CSVFormat customFormat = createCSVFormatWithQuoteCharacter('\'');
        assertTrue(customFormat.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsNull() throws Exception {
        // Use reflection to create CSVFormat with quoteCharacter null
        CSVFormat format = createCSVFormatWithQuoteCharacter(null);
        assertFalse(format.isQuoteCharacterSet());
    }

    private CSVFormat createCSVFormatWithQuoteCharacter(Character quoteChar) throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class,
                String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        QuoteMode defaultQuoteMode = null;

        return constructor.newInstance(
                ',', quoteChar, defaultQuoteMode,
                null, null,
                false, false,
                "\r\n", null, (Object) null,
                false, false);
    }
}