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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormatIsQuotingTest {

    @Test
    @Timeout(8000)
    void testIsQuotingWhenQuoteCharIsNotNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        // DEFAULT has quoteChar = DOUBLE_QUOTE_CHAR (not null)
        assertTrue(format.isQuoting());
    }

    @Test
    @Timeout(8000)
    void testIsQuotingWhenQuoteCharIsNull() throws Exception {
        // Create a CSVFormat with quoteChar set to null using reflection to bypass immutability
        CSVFormat format = CSVFormat.DEFAULT;

        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);

        // Remove final modifier from the field (works in Java 8 and earlier)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(quoteCharField, quoteCharField.getModifiers() & ~Modifier.FINAL);

        // Since quoteChar is Character, set to null is allowed
        quoteCharField.set(format, null);

        assertFalse(format.isQuoting());
    }
}