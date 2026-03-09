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

class CSVFormatWithTrimTest {

    @Test
    @Timeout(8000)
    void testWithTrimTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat trimmed = original.withTrim(true);
        assertNotSame(original, trimmed);
        assertTrue(trimmed.getTrim());
        // Other properties remain unchanged
        assertEquals(original.getDelimiter(), trimmed.getDelimiter());
        assertEquals(original.getQuoteCharacter(), trimmed.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), trimmed.getQuoteMode());
        assertEquals(original.getCommentMarker(), trimmed.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), trimmed.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), trimmed.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), trimmed.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), trimmed.getRecordSeparator());
        assertEquals(original.getNullString(), trimmed.getNullString());
        assertArrayEquals(original.getHeaderComments(), trimmed.getHeaderComments());
        assertArrayEquals(original.getHeader(), trimmed.getHeader());
        assertEquals(original.getSkipHeaderRecord(), trimmed.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), trimmed.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), trimmed.getIgnoreHeaderCase());
        assertEquals(original.getTrailingDelimiter(), trimmed.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), trimmed.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithTrimFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withTrim(true);
        CSVFormat untrimmed = original.withTrim(false);
        assertNotSame(original, untrimmed);
        assertFalse(untrimmed.getTrim());
        // Other properties remain unchanged
        assertEquals(original.getDelimiter(), untrimmed.getDelimiter());
        assertEquals(original.getQuoteCharacter(), untrimmed.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), untrimmed.getQuoteMode());
        assertEquals(original.getCommentMarker(), untrimmed.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), untrimmed.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), untrimmed.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), untrimmed.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), untrimmed.getRecordSeparator());
        assertEquals(original.getNullString(), untrimmed.getNullString());
        assertArrayEquals(original.getHeaderComments(), untrimmed.getHeaderComments());
        assertArrayEquals(original.getHeader(), untrimmed.getHeader());
        assertEquals(original.getSkipHeaderRecord(), untrimmed.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), untrimmed.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), untrimmed.getIgnoreHeaderCase());
        assertEquals(original.getTrailingDelimiter(), untrimmed.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), untrimmed.getAutoFlush());
    }
}