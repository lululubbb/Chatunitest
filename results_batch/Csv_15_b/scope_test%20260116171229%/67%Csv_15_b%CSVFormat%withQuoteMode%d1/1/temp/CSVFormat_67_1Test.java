package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormatWithQuoteModeTest {

    @Test
    @Timeout(8000)
    void testWithQuoteMode() {
        CSVFormat original = CSVFormat.DEFAULT;

        // Test with null QuoteMode
        CSVFormat resultNull = original.withQuoteMode(null);
        assertNotNull(resultNull);
        assertNull(resultNull.getQuoteMode());
        assertEquals(original.getDelimiter(), resultNull.getDelimiter());
        assertEquals(original.getQuoteCharacter(), resultNull.getQuoteCharacter());
        assertEquals(original.getCommentMarker(), resultNull.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), resultNull.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), resultNull.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), resultNull.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), resultNull.getRecordSeparator());
        assertEquals(original.getNullString(), resultNull.getNullString());
        assertArrayEquals(original.getHeaderComments(), resultNull.getHeaderComments());
        assertArrayEquals(original.getHeader(), resultNull.getHeader());
        assertEquals(original.getSkipHeaderRecord(), resultNull.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), resultNull.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), resultNull.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), resultNull.getTrim());
        assertEquals(original.getTrailingDelimiter(), resultNull.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), resultNull.getAutoFlush());

        // Test with a non-null QuoteMode
        QuoteMode quoteMode = QuoteMode.ALL;
        CSVFormat result = original.withQuoteMode(quoteMode);
        assertNotNull(result);
        assertEquals(quoteMode, result.getQuoteMode());
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
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
        assertEquals(original.getTrim(), result.getTrim());
        assertEquals(original.getTrailingDelimiter(), result.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), result.getAutoFlush());

        // Test chaining: original -> withQuoteMode -> withQuoteMode with different QuoteMode
        CSVFormat changedOnce = original.withQuoteMode(QuoteMode.MINIMAL);
        CSVFormat changedTwice = changedOnce.withQuoteMode(QuoteMode.NON_NUMERIC);
        assertEquals(QuoteMode.MINIMAL, changedOnce.getQuoteMode());
        assertEquals(QuoteMode.NON_NUMERIC, changedTwice.getQuoteMode());
        assertNotSame(changedOnce, changedTwice);
        assertNotSame(original, changedOnce);
        assertNotSame(original, changedTwice);
    }
}