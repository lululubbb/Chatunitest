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

public class CSVFormat_31_5Test {

    @Test
    @Timeout(8000)
    void testWithHeaderCreatesNewInstanceWithCorrectHeader() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        String[] headers = new String[] {"col1", "col2", "col3"};
        CSVFormat newFormat = original.withHeader(headers);

        // Check that the new instance is not the same as original
        assertNotSame(original, newFormat);

        // Check that header field is set correctly in new instance using reflection
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] actualHeader = (String[]) headerField.get(newFormat);
        assertArrayEquals(headers, actualHeader);

        // Check that other fields are copied correctly from original to newFormat
        assertEquals(original.getDelimiter(), newFormat.getDelimiter());
        assertEquals(original.getQuoteChar(), newFormat.getQuoteChar());
        assertEquals(original.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(original.getCommentStart(), newFormat.getCommentStart());
        assertEquals(original.getEscape(), newFormat.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(original.getNullString(), newFormat.getNullString());
        assertEquals(original.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderNullHeaderCreatesNewInstanceWithNullHeader() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat newFormat = original.withHeader((String[]) null);

        assertNotSame(original, newFormat);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] actualHeader = (String[]) headerField.get(newFormat);
        assertNull(actualHeader);

        // Other fields remain same
        assertEquals(original.getDelimiter(), newFormat.getDelimiter());
        assertEquals(original.getQuoteChar(), newFormat.getQuoteChar());
        assertEquals(original.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(original.getCommentStart(), newFormat.getCommentStart());
        assertEquals(original.getEscape(), newFormat.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(original.getNullString(), newFormat.getNullString());
        assertEquals(original.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderEmptyHeaderCreatesNewInstanceWithEmptyHeader() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        String[] emptyHeader = new String[0];
        CSVFormat newFormat = original.withHeader(emptyHeader);

        assertNotSame(original, newFormat);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] actualHeader = (String[]) headerField.get(newFormat);
        assertArrayEquals(emptyHeader, actualHeader);

        // Other fields remain same
        assertEquals(original.getDelimiter(), newFormat.getDelimiter());
        assertEquals(original.getQuoteChar(), newFormat.getQuoteChar());
        assertEquals(original.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(original.getCommentStart(), newFormat.getCommentStart());
        assertEquals(original.getEscape(), newFormat.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(original.getNullString(), newFormat.getNullString());
        assertEquals(original.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }
}