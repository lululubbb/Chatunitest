package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_57_1Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreEmptyLines(true);

        assertNotNull(modified);
        assertTrue(modified.getIgnoreEmptyLines());
        // Original should remain unchanged
        assertTrue(original.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesFalse() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreEmptyLines(false);

        assertNotNull(modified);
        assertFalse(modified.getIgnoreEmptyLines());
        // Original should remain unchanged
        assertTrue(original.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesCreatesNewInstance() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreEmptyLines(!original.getIgnoreEmptyLines());

        assertNotSame(original, modified);
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesPreservesOtherProperties() {
        CSVFormat original = CSVFormat.DEFAULT.withAllowMissingColumnNames(true)
                .withCommentMarker('#')
                .withDelimiter(';')
                .withEscape('\\')
                .withQuote('\'')
                .withQuoteMode(QuoteMode.ALL)
                .withRecordSeparator("\n")
                .withNullString("NULL")
                .withHeader("h1", "h2")
                .withHeaderComments("comment1")
                .withSkipHeaderRecord(true)
                .withIgnoreHeaderCase(true)
                .withIgnoreSurroundingSpaces(true)
                .withTrim(true)
                .withTrailingDelimiter(true);

        CSVFormat modified = original.withIgnoreEmptyLines(!original.getIgnoreEmptyLines());

        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertArrayEquals(original.getHeaderComments(), modified.getHeaderComments());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getTrim(), modified.getTrim());
        assertEquals(original.getTrailingDelimiter(), modified.getTrailingDelimiter());

        // The ignoreEmptyLines should be inverted
        assertNotEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
    }
}