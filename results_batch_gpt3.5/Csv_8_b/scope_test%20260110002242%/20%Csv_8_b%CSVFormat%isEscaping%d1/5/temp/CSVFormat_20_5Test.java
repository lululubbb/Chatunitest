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

class CSVFormat_20_5Test {

    @Test
    @Timeout(8000)
    void testIsEscaping_whenEscapeIsNull_shouldReturnFalse() throws Exception {
        // Use reflection to create CSVFormat instance with escape = null
        CSVFormat csvFormat = createCSVFormatWithEscape(null);
        assertFalse(csvFormat.isEscaping());
    }

    @Test
    @Timeout(8000)
    void testIsEscaping_whenEscapeIsNonNull_shouldReturnTrue() throws Exception {
        Character escapeChar = '\\';
        CSVFormat csvFormat = createCSVFormatWithEscape(escapeChar);
        assertTrue(csvFormat.isEscaping());
    }

    private CSVFormat createCSVFormatWithEscape(Character escape) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(
                ',',                 // delimiter
                '"',                 // quoteChar
                null,                // quotePolicy
                null,                // commentStart
                escape,              // escape
                false,               // ignoreSurroundingSpaces
                true,                // ignoreEmptyLines
                "\r\n",              // recordSeparator
                null,                // nullString
                (Object) null,       // header (cast to Object to match varargs)
                false                // skipHeaderRecord
        );
    }
}