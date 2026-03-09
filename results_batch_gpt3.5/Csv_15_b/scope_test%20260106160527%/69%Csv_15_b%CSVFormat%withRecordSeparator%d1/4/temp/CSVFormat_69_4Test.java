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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_69_4Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_NewValue() {
        CSVFormat base = CSVFormat.DEFAULT;
        String newSeparator = "\n";
        CSVFormat updated = base.withRecordSeparator(newSeparator);

        assertNotSame(base, updated);
        assertEquals(newSeparator, updated.getRecordSeparator());

        // Verify other fields are unchanged
        assertEquals(base.getDelimiter(), updated.getDelimiter());
        assertEquals(base.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), updated.getQuoteMode());
        assertEquals(base.getCommentMarker(), updated.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(base.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(base.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(base.getNullString(), updated.getNullString());
        assertArrayEquals(base.getHeaderComments(), updated.getHeaderComments());
        assertArrayEquals(base.getHeader(), updated.getHeader());
        assertEquals(base.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(base.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), updated.getTrim());
        assertEquals(base.getTrailingDelimiter(), updated.getTrailingDelimiter());
        assertEquals(base.getAutoFlush(), updated.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_NullValue() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat updated = base.withRecordSeparator((String) null);

        assertNotSame(base, updated);
        assertNull(updated.getRecordSeparator());

        // Verify other fields are unchanged
        assertEquals(base.getDelimiter(), updated.getDelimiter());
        assertEquals(base.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), updated.getQuoteMode());
        assertEquals(base.getCommentMarker(), updated.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(base.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(base.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(base.getNullString(), updated.getNullString());
        assertArrayEquals(base.getHeaderComments(), updated.getHeaderComments());
        assertArrayEquals(base.getHeader(), updated.getHeader());
        assertEquals(base.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(base.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), updated.getTrim());
        assertEquals(base.getTrailingDelimiter(), updated.getTrailingDelimiter());
        assertEquals(base.getAutoFlush(), updated.getAutoFlush());
    }

}