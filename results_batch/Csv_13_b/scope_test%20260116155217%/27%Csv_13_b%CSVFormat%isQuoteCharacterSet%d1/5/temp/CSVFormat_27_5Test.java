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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_27_5Test {

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSetWhenQuoteCharacterIsNotNull() throws Exception {
        // Use reflection to create CSVFormat instance with quoteCharacter set to '"'
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class,
                String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                ',',
                Character.valueOf('"'),
                null,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                new Object[0],  // Fix: pass empty Object[] instead of null
                null,
                false,
                true,
                false);
        assertTrue(format.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSetWhenQuoteCharacterIsNull() throws Exception {
        // Use withQuote(Character) with null to get instance with null quoteCharacter
        CSVFormat formatWithNullQuote = CSVFormat.DEFAULT.withQuote((Character) null);
        assertFalse(formatWithNullQuote.isQuoteCharacterSet());
    }
}