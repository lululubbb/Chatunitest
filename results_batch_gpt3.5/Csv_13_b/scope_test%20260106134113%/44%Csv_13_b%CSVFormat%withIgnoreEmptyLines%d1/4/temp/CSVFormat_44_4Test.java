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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_44_4Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesTrue() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat result = csvFormat.withIgnoreEmptyLines(true);

        assertNotNull(result);
        assertEquals(csvFormat.getDelimiter(), result.getDelimiter());
        assertEquals(csvFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(csvFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(csvFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(csvFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(csvFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertTrue(result.getIgnoreEmptyLines());
        assertEquals(csvFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(csvFormat.getNullString(), result.getNullString());
        assertArrayEquals(csvFormat.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(csvFormat.getHeader(), result.getHeader());
        assertEquals(csvFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(csvFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(csvFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesFalse() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat result = csvFormat.withIgnoreEmptyLines(false);

        assertNotNull(result);
        assertEquals(csvFormat.getDelimiter(), result.getDelimiter());
        assertEquals(csvFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(csvFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(csvFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(csvFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(csvFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertFalse(result.getIgnoreEmptyLines());
        assertEquals(csvFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(csvFormat.getNullString(), result.getNullString());
        assertArrayEquals(csvFormat.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(csvFormat.getHeader(), result.getHeader());
        assertEquals(csvFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(csvFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(csvFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesDoesNotModifyOriginal() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreEmptyLines(!original.getIgnoreEmptyLines());

        assertNotSame(original, modified);
        assertEquals(original.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesPrivateConstructorInvocation() throws Exception {
        // Using reflection to verify the private constructor is callable with ignoreEmptyLines parameter

        Class<CSVFormat> clazz = CSVFormat.class;
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        // Fix: ensure headerComments is Object[] and not null
        Object[] headerComments = defaultFormat.getHeaderComments();
        if (headerComments == null) {
            headerComments = new Object[0];
        }

        // Fix: If headerComments is String[], convert to Object[]
        else if (headerComments instanceof String[]) {
            String[] strArr = (String[]) headerComments;
            headerComments = new Object[strArr.length];
            System.arraycopy(strArr, 0, headerComments, 0, strArr.length);
        }

        CSVFormat instance = constructor.newInstance(
                defaultFormat.getDelimiter(),
                defaultFormat.getQuoteCharacter(),
                defaultFormat.getQuoteMode(),
                defaultFormat.getCommentMarker(),
                defaultFormat.getEscapeCharacter(),
                defaultFormat.getIgnoreSurroundingSpaces(),
                true, // ignoreEmptyLines
                defaultFormat.getRecordSeparator(),
                defaultFormat.getNullString(),
                headerComments,
                defaultFormat.getHeader(),
                defaultFormat.getSkipHeaderRecord(),
                defaultFormat.getAllowMissingColumnNames(),
                defaultFormat.getIgnoreHeaderCase()
        );

        assertTrue(instance.getIgnoreEmptyLines());
    }

}