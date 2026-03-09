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

class CSVFormat_27_1Test {

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsSet() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsNull() throws Exception {
        // Use reflection to create CSVFormat instance with quoteCharacter = null
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class,
                QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class,
                Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
                ',', // delimiter
                null, // quoteCharacter set to null
                null, // quoteMode
                null, // commentMarker
                null, // escapeCharacter
                false, // ignoreSurroundingSpaces
                true,  // ignoreEmptyLines
                "\r\n", // recordSeparator
                null, // headerComments (Object[])
                null, // header (String[])
                false, // skipHeaderRecord
                false, // allowMissingColumnNames
                false  // ignoreHeaderCase
        );

        assertFalse(format.isQuoteCharacterSet());
    }
}