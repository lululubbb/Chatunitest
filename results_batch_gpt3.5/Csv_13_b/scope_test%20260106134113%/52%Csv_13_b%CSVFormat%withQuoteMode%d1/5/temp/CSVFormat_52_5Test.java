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

public class CSVFormat_52_5Test {

    @Test
    @Timeout(8000)
    void testWithQuoteMode_NewInstanceHasSamePropertiesExceptQuoteMode() throws Exception {
        // Arrange
        CSVFormat original = CSVFormat.DEFAULT;
        QuoteMode newQuoteMode = QuoteMode.ALL;

        // Act
        CSVFormat result = original.withQuoteMode(newQuoteMode);

        // Assert
        assertNotNull(result);
        assertNotSame(original, result);
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(newQuoteMode, result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteMode_QuoteModeNull() {
        CSVFormat original = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        CSVFormat result = original.withQuoteMode(null);
        assertNotNull(result);
        assertNull(result.getQuoteMode());
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteMode_Immutability() {
        CSVFormat original = CSVFormat.DEFAULT;
        QuoteMode newQuoteMode = QuoteMode.MINIMAL;
        CSVFormat result = original.withQuoteMode(newQuoteMode);

        // Changing result should not affect original
        assertNotEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(QuoteMode.MINIMAL, result.getQuoteMode());
        assertEquals(original.getQuoteMode(), CSVFormat.DEFAULT.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteMode_PrivateConstructorInvocation() throws Exception {
        QuoteMode quoteMode = QuoteMode.NON_NUMERIC;
        CSVFormat original = CSVFormat.DEFAULT;

        // Use reflection to get private constructor and invoke it with new quoteMode
        Class<CSVFormat> clazz = CSVFormat.class;
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class,
                Character.class,
                QuoteMode.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                Object[].class,
                String[].class,
                boolean.class,
                boolean.class,
                boolean.class);
        constructor.setAccessible(true);

        CSVFormat instance = constructor.newInstance(
                original.getDelimiter(),
                original.getQuoteCharacter(),
                quoteMode,
                original.getCommentMarker(),
                original.getEscapeCharacter(),
                original.getIgnoreSurroundingSpaces(),
                original.getIgnoreEmptyLines(),
                original.getRecordSeparator(),
                original.getNullString(),
                original.getHeaderComments(),
                original.getHeader(),
                original.getSkipHeaderRecord(),
                original.getAllowMissingColumnNames(),
                original.getIgnoreHeaderCase()
        );

        assertNotNull(instance);
        assertEquals(quoteMode, instance.getQuoteMode());
        assertEquals(original.getDelimiter(), instance.getDelimiter());
    }
}