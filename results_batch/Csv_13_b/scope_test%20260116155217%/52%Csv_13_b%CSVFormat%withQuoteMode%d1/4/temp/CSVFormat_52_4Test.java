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
import java.lang.reflect.Method;

public class CSVFormat_52_4Test {

    @Test
    @Timeout(8000)
    public void testWithQuoteModeCreatesNewInstanceWithGivenQuoteMode() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        QuoteMode newQuoteMode = QuoteMode.ALL;

        CSVFormat newFormat = original.withQuoteMode(newQuoteMode);

        assertNotNull(newFormat);
        assertNotSame(original, newFormat);
        assertEquals(newQuoteMode, newFormat.getQuoteMode());

        // Check other properties remain unchanged
        assertEquals(original.getDelimiter(), newFormat.getDelimiter());
        assertEquals(original.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(original.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(original.getNullString(), newFormat.getNullString());
        assertArrayEquals(original.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(original.getHeader(), newFormat.getHeader());
        assertEquals(original.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteModeWithNullQuoteMode() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat newFormat = original.withQuoteMode(null);

        assertNotNull(newFormat);
        assertNotSame(original, newFormat);
        assertNull(newFormat.getQuoteMode());

        // Other properties unchanged
        assertEquals(original.getDelimiter(), newFormat.getDelimiter());
        assertEquals(original.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(original.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(original.getNullString(), newFormat.getNullString());
        assertArrayEquals(original.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(original.getHeader(), newFormat.getHeader());
        assertEquals(original.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteModeReflectiveInvocation() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        QuoteMode quoteMode = QuoteMode.MINIMAL;

        Method method = CSVFormat.class.getMethod("withQuoteMode", QuoteMode.class);
        CSVFormat result = (CSVFormat) method.invoke(original, quoteMode);

        assertNotNull(result);
        assertEquals(quoteMode, result.getQuoteMode());
        assertNotSame(original, result);
    }
}