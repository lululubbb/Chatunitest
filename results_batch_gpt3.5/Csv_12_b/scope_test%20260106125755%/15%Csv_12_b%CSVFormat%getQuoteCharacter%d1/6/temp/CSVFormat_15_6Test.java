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
import org.junit.jupiter.api.DisplayName;
import java.lang.reflect.Constructor;

public class CSVFormat_15_6Test {

    @Test
    @Timeout(8000)
    @DisplayName("Test getQuoteCharacter returns correct Character when set")
    public void testGetQuoteCharacterWhenSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('\'');
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('\'', quoteChar.charValue());
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test getQuoteCharacter returns null when quoteCharacter is null")
    public void testGetQuoteCharacterWhenNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormatWithNullQuote = constructor.newInstance(
                format.getDelimiter(),
                null,
                format.getQuoteMode(),
                format.getCommentMarker(),
                format.getEscapeCharacter(),
                format.getIgnoreSurroundingSpaces(),
                format.getIgnoreEmptyLines(),
                format.getRecordSeparator(),
                format.getNullString(),
                format.getHeader(),
                format.getSkipHeaderRecord(),
                format.getAllowMissingColumnNames());

        assertNull(csvFormatWithNullQuote.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test getQuoteCharacter returns default quote character")
    public void testGetQuoteCharacterDefault() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('"', quoteChar.charValue());
    }
}