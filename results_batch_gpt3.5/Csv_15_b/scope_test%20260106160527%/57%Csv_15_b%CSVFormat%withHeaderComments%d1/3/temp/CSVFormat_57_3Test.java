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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_57_3Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_nullComments() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat result = base.withHeaderComments((Object[]) null);
        assertNotNull(result);
        assertNull(result.getHeaderComments());
        assertArrayEquals(base.getHeader(), result.getHeader());
        assertEquals(base.getDelimiter(), result.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_emptyComments() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat result = base.withHeaderComments();
        assertNotNull(result);
        assertNotNull(result.getHeaderComments());
        assertEquals(0, result.getHeaderComments().length);
        assertArrayEquals(base.getHeader(), result.getHeader());
        assertEquals(base.getDelimiter(), result.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_nonEmptyComments() {
        CSVFormat base = CSVFormat.DEFAULT.withDelimiter(';').withQuoteMode(QuoteMode.ALL);
        Object[] comments = new Object[]{"comment1", 123, null, "comment4"};
        CSVFormat result = base.withHeaderComments(comments);
        assertNotNull(result);
        assertArrayEquals(new String[]{"comment1", "123", null, "comment4"}, result.getHeaderComments());
        assertArrayEquals(base.getHeader(), result.getHeader());
        assertEquals(base.getDelimiter(), result.getDelimiter());
        assertEquals(base.getQuoteMode(), result.getQuoteMode());
        // Original base should remain unchanged
        assertNull(base.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_doesNotAffectOtherProperties() {
        CSVFormat base = CSVFormat.DEFAULT
                .withDelimiter('|')
                .withQuote('\'')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withNullString("NULL")
                .withHeader("a", "b")
                .withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(true)
                .withIgnoreHeaderCase(true)
                .withTrim(true)
                .withTrailingDelimiter(true)
                .withAutoFlush(true);
        Object[] comments = new Object[]{"header1", "header2"};
        CSVFormat result = base.withHeaderComments(comments);

        assertNotNull(result.getHeaderComments());
        assertArrayEquals(new String[]{"header1", "header2"}, result.getHeaderComments());

        assertEquals(base.getDelimiter(), result.getDelimiter());
        assertEquals(base.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), result.getQuoteMode());
        assertEquals(base.getCommentMarker(), result.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(base.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(base.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(base.getNullString(), result.getNullString());
        assertArrayEquals(base.getHeader(), result.getHeader());
        assertEquals(base.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(base.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), result.getTrim());
        assertEquals(base.getTrailingDelimiter(), result.getTrailingDelimiter());
        assertEquals(base.getAutoFlush(), result.getAutoFlush());

        // Original base remains unchanged
        assertNull(base.getHeaderComments());
    }
}