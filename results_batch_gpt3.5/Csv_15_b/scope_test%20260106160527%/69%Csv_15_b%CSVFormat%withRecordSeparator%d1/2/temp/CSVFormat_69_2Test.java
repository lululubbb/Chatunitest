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

class CSVFormat_69_2Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_NewInstanceWithGivenRecordSeparator() {
        CSVFormat original = CSVFormat.DEFAULT;
        String newRecordSeparator = "\n";
        CSVFormat changed = original.withRecordSeparator(newRecordSeparator);

        // Verify that a new instance is returned
        assertNotSame(original, changed);

        // Verify that the record separator is updated
        assertEquals(newRecordSeparator, changed.getRecordSeparator());

        // Verify that other properties remain the same
        assertEquals(original.getDelimiter(), changed.getDelimiter());
        assertEquals(original.getQuoteCharacter(), changed.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), changed.getQuoteMode());
        assertEquals(original.getCommentMarker(), changed.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), changed.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), changed.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), changed.getIgnoreEmptyLines());
        assertEquals(original.getNullString(), changed.getNullString());
        assertArrayEquals(original.getHeaderComments(), changed.getHeaderComments());
        assertArrayEquals(original.getHeader(), changed.getHeader());
        assertEquals(original.getSkipHeaderRecord(), changed.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), changed.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), changed.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), changed.getTrim());
        assertEquals(original.getTrailingDelimiter(), changed.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), changed.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_NullRecordSeparator() {
        CSVFormat original = CSVFormat.DEFAULT;
        String newRecordSeparator = null;
        CSVFormat changed = original.withRecordSeparator(newRecordSeparator);

        // Verify that a new instance is returned
        assertNotSame(original, changed);

        // Verify that the record separator is set to null
        assertNull(changed.getRecordSeparator());

        // Verify that other properties remain the same
        assertEquals(original.getDelimiter(), changed.getDelimiter());
        assertEquals(original.getQuoteCharacter(), changed.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), changed.getQuoteMode());
        assertEquals(original.getCommentMarker(), changed.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), changed.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), changed.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), changed.getIgnoreEmptyLines());
        assertEquals(original.getNullString(), changed.getNullString());
        assertArrayEquals(original.getHeaderComments(), changed.getHeaderComments());
        assertArrayEquals(original.getHeader(), changed.getHeader());
        assertEquals(original.getSkipHeaderRecord(), changed.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), changed.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), changed.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), changed.getTrim());
        assertEquals(original.getTrailingDelimiter(), changed.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), changed.getAutoFlush());
    }
}