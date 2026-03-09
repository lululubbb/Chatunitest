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
import java.lang.reflect.Method;

class CSVFormat_42_6Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Use reflection to invoke withAllowMissingColumnNames() (no-arg method)
        Method method = CSVFormat.class.getDeclaredMethod("withAllowMissingColumnNames");
        CSVFormat modified = (CSVFormat) method.invoke(original);

        assertNotSame(original, modified);
        assertTrue(modified.getAllowMissingColumnNames());
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeaderComments(), modified.getHeaderComments());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesFalse() throws Exception {
        CSVFormat original;

        // Use reflection to invoke withAllowMissingColumnNames() (no-arg method) first
        Method methodNoArg = CSVFormat.class.getDeclaredMethod("withAllowMissingColumnNames");
        original = (CSVFormat) methodNoArg.invoke(CSVFormat.DEFAULT);

        // Get constructor with all args
        Class<?>[] paramTypes = new Class[]{
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class
        };

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(paramTypes);
        constructor.setAccessible(true);

        CSVFormat modified = constructor.newInstance(
                original.getDelimiter(),
                original.getQuoteCharacter(),
                original.getQuoteMode(),
                original.getCommentMarker(),
                original.getEscapeCharacter(),
                original.getIgnoreSurroundingSpaces(),
                original.getIgnoreEmptyLines(),
                original.getRecordSeparator(),
                original.getNullString(),
                original.getHeaderComments(),
                original.getHeader(),
                original.getSkipHeaderRecord(),
                false, // allowMissingColumnNames = false
                original.getIgnoreHeaderCase()
        );

        assertNotSame(original, modified);
        assertFalse(modified.getAllowMissingColumnNames());
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeaderComments(), modified.getHeaderComments());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
    }
}