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

public class CSVFormat_50_5Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_primitiveChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\'';

        CSVFormat result = original.withQuote(quoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
        assertNotSame(original, result);

        // Check that other properties remain unchanged
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(original.getNullString(), result.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_nullCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character quoteChar = null;

        CSVFormat result = original.withQuote(quoteChar);

        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        assertNotSame(original, result);

        // Check that other properties remain unchanged
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(original.getNullString(), result.getNullString());
    }
}