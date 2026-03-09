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

class CSVFormat_21_5Test {

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_whenEscapeCharacterIsNull() throws Exception {
        // Use DEFAULT which has escapeCharacter null
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_whenEscapeCharacterIsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        assertTrue(format.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_withReflection_escapeCharacterNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Create a new instance with escapeCharacter null using reflection since fields are final
        CSVFormat newFormat = createCSVFormatWithEscapeCharacter(null);
        assertFalse(newFormat.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_withReflection_escapeCharacterSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Create a new instance with escapeCharacter set to '\\' using reflection since fields are final
        CSVFormat newFormat = createCSVFormatWithEscapeCharacter('\\');
        assertTrue(newFormat.isEscapeCharacterSet());
    }

    private CSVFormat createCSVFormatWithEscapeCharacter(Character escapeCharacter) throws Exception {
        // Get constructor with all parameters
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Use DEFAULT's other field values
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        return constructor.newInstance(
                defaultFormat.getDelimiter(),
                defaultFormat.getQuoteCharacter(),
                defaultFormat.getQuoteMode(),
                defaultFormat.getCommentMarker(),
                escapeCharacter,
                defaultFormat.getIgnoreSurroundingSpaces(),
                defaultFormat.getIgnoreEmptyLines(),
                defaultFormat.getRecordSeparator(),
                defaultFormat.getNullString(),
                defaultFormat.getHeader(),
                defaultFormat.getSkipHeaderRecord(),
                defaultFormat.getAllowMissingColumnNames()
        );
    }
}