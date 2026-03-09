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

import java.lang.reflect.Field;

class CSVFormat_23_6Test {

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsNotNull() throws Exception {
        // Use the existing CSVFormat.DEFAULT instance
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to get the private field 'quoteCharacter'
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);

        // Verify quoteCharacter is not null initially
        Character quoteChar = (Character) quoteCharacterField.get(format);
        assertNotNull(quoteChar);

        // Call the method and assert true
        assertTrue(format.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsNull() throws Exception {
        // Create CSVFormat instance with quoteCharacter set to null using withQuote(null)
        CSVFormat format = CSVFormat.DEFAULT.withQuote((Character) null);

        // Use reflection to get the private field 'quoteCharacter'
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);

        // Verify quoteCharacter is null
        Character quoteChar = (Character) quoteCharacterField.get(format);
        assertNull(quoteChar);

        // Call the method and assert false
        assertFalse(format.isQuoteCharacterSet());
    }
}