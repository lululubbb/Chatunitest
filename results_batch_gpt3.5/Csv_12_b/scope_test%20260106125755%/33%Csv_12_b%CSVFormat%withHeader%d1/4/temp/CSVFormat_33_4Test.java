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

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormatWithHeaderTest {

    @Test
    @Timeout(8000)
    void testWithHeaderCreatesNewCSVFormatWithGivenHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        String[] headers = new String[]{"col1", "col2", "col3"};

        CSVFormat newFormat = baseFormat.withHeader(headers);

        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        assertArrayEquals(headers, newFormat.getHeader());

        // Other properties remain same as baseFormat
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithNullHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        CSVFormat newFormat = baseFormat.withHeader((String[]) null);

        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        assertNull(newFormat.getHeader());

        // Other properties remain same as baseFormat
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithEmptyHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        String[] headers = new String[0];

        CSVFormat newFormat = baseFormat.withHeader(headers);

        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        assertArrayEquals(headers, newFormat.getHeader());

        // Other properties remain same as baseFormat
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderUsingReflectionToInvokePrivateConstructor() throws Exception {
        Class<?> clazz = CSVFormat.class;
        // Locate private constructor with all parameters
        Class<?>[] paramTypes = new Class[]{
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class
        };
        Constructor<?> constructor = clazz.getDeclaredConstructor(paramTypes);
        constructor.setAccessible(true);

        String[] header = new String[]{"h1", "h2"};
        Object instance = constructor.newInstance(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                header,
                CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames()
        );

        assertNotNull(instance);
        assertTrue(instance instanceof CSVFormat);
        CSVFormat formatInstance = (CSVFormat) instance;
        assertArrayEquals(header, formatInstance.getHeader());
    }
}