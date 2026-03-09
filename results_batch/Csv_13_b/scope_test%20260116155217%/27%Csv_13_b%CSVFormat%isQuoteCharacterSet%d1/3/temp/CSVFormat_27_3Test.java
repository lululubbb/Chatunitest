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

class CSVFormat_27_3Test {

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsNotNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsNull() throws Exception {
        // Get the private constructor with exact parameter types
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class,
                QuoteMode.class,
                Character.class, Character.class,
                boolean.class, boolean.class,
                String.class, String.class,
                Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Get quoteMode from DEFAULT instance via getter method
        QuoteMode quoteMode = CSVFormat.DEFAULT.getQuoteMode();

        // Prepare null arrays for headerComments and header
        Object[] headerComments = null;
        String[] header = null;

        CSVFormat format = constructor.newInstance(
                ',',       // delimiter
                null,      // quoteCharacter null
                quoteMode, // quoteMode
                null,      // commentMarker
                null,      // escapeCharacter
                false,     // ignoreSurroundingSpaces
                false,     // ignoreEmptyLines
                "\r\n",    // recordSeparator
                null,      // nullString
                headerComments, // headerComments (Object[])
                header,         // header (String[])
                false,     // skipHeaderRecord
                true,      // allowMissingColumnNames
                false      // ignoreHeaderCase
        );

        assertFalse(format.isQuoteCharacterSet());
    }
}