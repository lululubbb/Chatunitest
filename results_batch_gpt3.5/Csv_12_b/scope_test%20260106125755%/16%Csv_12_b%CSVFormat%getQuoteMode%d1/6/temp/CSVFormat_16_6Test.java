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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_16_6Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Default() throws Exception {
        CSVFormat format = createCSVFormatWithQuoteMode(null);
        QuoteMode mode = format.getQuoteMode();
        assertNull(mode, "Default CSVFormat quoteMode should be null");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_CustomQuoteMode() throws Exception {
        QuoteMode customMode = QuoteMode.ALL;
        CSVFormat format = createCSVFormatWithQuoteMode(customMode);
        QuoteMode mode = format.getQuoteMode();
        assertSame(customMode, mode, "QuoteMode should be the custom mode set");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_NullQuoteMode() throws Exception {
        CSVFormat format = createCSVFormatWithQuoteMode(null);
        QuoteMode mode = format.getQuoteMode();
        assertNull(mode, "QuoteMode should be null when set to null");
    }

    private CSVFormat createCSVFormatWithQuoteMode(QuoteMode quoteMode) throws Exception {
        // Create a new CSVFormat instance based on DEFAULT but with the specified quoteMode set via reflection
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to create a new CSVFormat instance by calling the private constructor
        java.lang.reflect.Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        ctor.setAccessible(true);

        CSVFormat newFormat = ctor.newInstance(
                format.getDelimiter(),
                format.getQuoteCharacter(),
                quoteMode,
                format.getCommentMarker(),
                format.getEscapeCharacter(),
                format.getIgnoreSurroundingSpaces(),
                format.getIgnoreEmptyLines(),
                format.getRecordSeparator(),
                format.getNullString(),
                format.getHeader(),
                format.getSkipHeaderRecord(),
                format.getAllowMissingColumnNames()
        );

        return newFormat;
    }
}