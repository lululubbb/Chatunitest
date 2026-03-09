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

class CSVFormatWithDelimiterTest {

    @Test
    @Timeout(8000)
    void testWithDelimiter_validDelimiter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newDelimiter = ';';
        CSVFormat updated = original.withDelimiter(newDelimiter);
        assertNotNull(updated);
        assertEquals(newDelimiter, updated.getDelimiter());
        // Other properties should remain the same as original except delimiter
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());

        // headerComments and header can be null, so handle accordingly
        if (original.getHeaderComments() == null) {
            assertNull(updated.getHeaderComments());
        } else {
            assertArrayEquals(original.getHeaderComments(), updated.getHeaderComments());
        }

        if (original.getHeader() == null) {
            assertNull(updated.getHeader());
        } else {
            assertArrayEquals(original.getHeader(), updated.getHeader());
        }

        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), updated.getTrim());
        assertEquals(original.getTrailingDelimiter(), updated.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), updated.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_lineBreakDelimiterThrows() {
        CSVFormat original = CSVFormat.DEFAULT;
        char[] lineBreaks = {'\n', '\r'};
        for (char c : lineBreaks) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> original.withDelimiter(c));
            assertEquals("The delimiter cannot be a line break", ex.getMessage());
        }
    }
}