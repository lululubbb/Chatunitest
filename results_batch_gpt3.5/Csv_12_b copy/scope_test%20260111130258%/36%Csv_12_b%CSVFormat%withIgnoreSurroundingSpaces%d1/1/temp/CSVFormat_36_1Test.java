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
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class CSVFormat_36_1Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces() throws Exception {
        // Given
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(',', '\"', null, null, null,
                false, true, "\r\n", null, null, false, false);

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces(true);

        // Then
        assertEquals(',', result.getDelimiter());
        assertEquals('\"', result.getQuoteCharacter());
        assertNull(result.getQuoteMode());
        assertNull(result.getCommentMarker());
        assertNull(result.getEscapeCharacter());
        assertTrue(result.getIgnoreSurroundingSpaces());
        assertTrue(result.getIgnoreEmptyLines());
        assertEquals("\r\n", result.getRecordSeparator());
        assertNull(result.getNullString());
        assertNull(result.getHeader());
        assertFalse(result.getSkipHeaderRecord());
        assertFalse(result.getAllowMissingColumnNames());
    }
}