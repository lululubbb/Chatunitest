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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_27_1Test {

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsNonNull() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                QuoteMode.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                Object[].class,
                String[].class,
                boolean.class,
                boolean.class,
                boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(
                ',',        // delimiter
                '\"',       // quoteCharacter non-null (use char, not Character.valueOf)
                null,       // quoteMode
                null,       // commentMarker
                null,       // escapeCharacter
                false,      // ignoreSurroundingSpaces
                false,      // ignoreEmptyLines
                "\n",       // recordSeparator
                null,       // nullString
                null,       // headerComments
                null,       // header
                false,      // skipHeaderRecord
                false,      // allowMissingColumnNames
                false       // ignoreHeaderCase
        );

        assertTrue(csvFormat.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsNull() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                QuoteMode.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                Object[].class,
                String[].class,
                boolean.class,
                boolean.class,
                boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(
                ',',        // delimiter
                null,       // quoteCharacter null
                null,       // quoteMode
                null,       // commentMarker
                null,       // escapeCharacter
                false,      // ignoreSurroundingSpaces
                false,      // ignoreEmptyLines
                "\n",       // recordSeparator
                null,       // nullString
                null,       // headerComments
                null,       // header
                false,      // skipHeaderRecord
                false,      // allowMissingColumnNames
                false       // ignoreHeaderCase
        );

        assertFalse(csvFormat.isQuoteCharacterSet());
    }
}