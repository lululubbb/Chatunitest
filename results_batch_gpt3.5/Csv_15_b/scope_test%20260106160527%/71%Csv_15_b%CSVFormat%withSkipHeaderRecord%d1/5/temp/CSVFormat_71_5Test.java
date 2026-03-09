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

class CSVFormat_71_5Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordTrue() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat modified = base.withSkipHeaderRecord(true);
        assertNotNull(modified);
        assertTrue(modified.getSkipHeaderRecord());
        // original instance remains unchanged
        assertFalse(base.getSkipHeaderRecord());
        // all other properties unchanged
        assertEquals(base.getDelimiter(), modified.getDelimiter());
        assertEquals(base.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), modified.getQuoteMode());
        assertEquals(base.getCommentMarker(), modified.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(base.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(base.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(base.getNullString(), modified.getNullString());
        assertArrayEquals(base.getHeaderComments(), modified.getHeaderComments());
        assertArrayEquals(base.getHeader(), modified.getHeader());
        assertEquals(base.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), modified.getTrim());
        assertEquals(base.getTrailingDelimiter(), modified.getTrailingDelimiter());
        assertEquals(base.getAutoFlush(), modified.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordFalse() {
        CSVFormat base = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat modified = base.withSkipHeaderRecord(false);
        assertNotNull(modified);
        assertFalse(modified.getSkipHeaderRecord());
        // original instance remains unchanged
        assertTrue(base.getSkipHeaderRecord());
        // all other properties unchanged
        assertEquals(base.getDelimiter(), modified.getDelimiter());
        assertEquals(base.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), modified.getQuoteMode());
        assertEquals(base.getCommentMarker(), modified.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(base.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(base.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(base.getNullString(), modified.getNullString());
        assertArrayEquals(base.getHeaderComments(), modified.getHeaderComments());
        assertArrayEquals(base.getHeader(), modified.getHeader());
        assertEquals(base.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), modified.getTrim());
        assertEquals(base.getTrailingDelimiter(), modified.getTrailingDelimiter());
        assertEquals(base.getAutoFlush(), modified.getAutoFlush());
    }

}