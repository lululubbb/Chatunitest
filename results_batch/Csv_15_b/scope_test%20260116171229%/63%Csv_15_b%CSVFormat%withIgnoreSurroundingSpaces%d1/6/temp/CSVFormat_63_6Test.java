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

public class CSVFormat_63_6Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpacesTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat changed = original.withIgnoreSurroundingSpaces(true);

        assertNotSame(original, changed);
        assertTrue(changed.getIgnoreSurroundingSpaces());
        // Original remains unchanged
        assertFalse(original.getIgnoreSurroundingSpaces());

        // Other properties remain the same
        assertEquals(original.getDelimiter(), changed.getDelimiter());
        assertEquals(original.getQuoteCharacter(), changed.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), changed.getQuoteMode());
        assertEquals(original.getCommentMarker(), changed.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), changed.getEscapeCharacter());
        assertEquals(original.getIgnoreEmptyLines(), changed.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), changed.getRecordSeparator());
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
    void testWithIgnoreSurroundingSpacesFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        CSVFormat changed = original.withIgnoreSurroundingSpaces(false);

        assertNotSame(original, changed);
        assertFalse(changed.getIgnoreSurroundingSpaces());
        // Original remains unchanged
        assertTrue(original.getIgnoreSurroundingSpaces());

        // Other properties remain the same
        assertEquals(original.getDelimiter(), changed.getDelimiter());
        assertEquals(original.getQuoteCharacter(), changed.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), changed.getQuoteMode());
        assertEquals(original.getCommentMarker(), changed.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), changed.getEscapeCharacter());
        assertEquals(original.getIgnoreEmptyLines(), changed.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), changed.getRecordSeparator());
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