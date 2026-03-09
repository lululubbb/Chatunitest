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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class CSVFormat_44_5Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat result = original.withIgnoreEmptyLines(true);

        assertNotNull(result);
        assertEquals(true, result.getIgnoreEmptyLines());
        // All other fields should be equal to original's respective fields
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());

        assertNotSame(original, result);
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesFalse() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat result = original.withIgnoreEmptyLines(false);

        assertNotNull(result);
        assertEquals(false, result.getIgnoreEmptyLines());
        // All other fields should be equal to original's respective fields
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());

        assertNotSame(original, result);
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesDoesNotModifyOriginal() {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat result = original.withIgnoreEmptyLines(!original.getIgnoreEmptyLines());

        assertNotEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getIgnoreEmptyLines(), original.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesReflectionInvocation() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        Method method = CSVFormat.class.getMethod("withIgnoreEmptyLines", boolean.class);

        CSVFormat resultTrue = (CSVFormat) method.invoke(original, true);
        assertTrue(resultTrue.getIgnoreEmptyLines());

        CSVFormat resultFalse = (CSVFormat) method.invoke(original, false);
        assertFalse(resultFalse.getIgnoreEmptyLines());
    }
}