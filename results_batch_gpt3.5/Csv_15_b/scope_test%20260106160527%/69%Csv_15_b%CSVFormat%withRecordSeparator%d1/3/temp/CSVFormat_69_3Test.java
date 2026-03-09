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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_69_3Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_NewInstanceWithGivenRecordSeparator() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String newRecordSeparator = "\n";

        CSVFormat updated = original.withRecordSeparator(newRecordSeparator);

        // Verify that a new instance is created
        assertNotSame(original, updated);

        // Verify that the recordSeparator is updated
        assertEquals(newRecordSeparator, updated.getRecordSeparator());

        // Verify that other properties are unchanged
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getNullString(), updated.getNullString());

        // headerComments and header can be null, handle null safely
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
    void testWithRecordSeparator_NullRecordSeparator() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String newRecordSeparator = null;

        CSVFormat updated = original.withRecordSeparator(newRecordSeparator);

        assertNotSame(original, updated);
        assertNull(updated.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_EmptyRecordSeparator() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String newRecordSeparator = "";

        CSVFormat updated = original.withRecordSeparator(newRecordSeparator);

        assertNotSame(original, updated);
        assertEquals("", updated.getRecordSeparator());
    }
}