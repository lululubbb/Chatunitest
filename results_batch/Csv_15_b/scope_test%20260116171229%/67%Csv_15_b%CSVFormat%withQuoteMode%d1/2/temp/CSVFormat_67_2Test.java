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
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

public class CSVFormat_67_2Test {

    @Test
    @Timeout(8000)
    public void testWithQuoteMode_NullQuoteMode() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat result = base.withQuoteMode(null);
        assertNotNull(result);
        assertNull(result.getQuoteMode());
        // Other properties remain unchanged
        assertEquals(base.getDelimiter(), result.getDelimiter());
        assertEquals(base.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(base.getCommentMarker(), result.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(base.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(base.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(base.getNullString(), result.getNullString());

        // headerComments and header may be null, handle null safely
        if (base.getHeaderComments() == null) {
            assertNull(result.getHeaderComments());
        } else {
            assertArrayEquals(base.getHeaderComments(), result.getHeaderComments());
        }
        if (base.getHeader() == null) {
            assertNull(result.getHeader());
        } else {
            assertArrayEquals(base.getHeader(), result.getHeader());
        }

        assertEquals(base.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(base.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), result.getTrim());
        assertEquals(base.getTrailingDelimiter(), result.getTrailingDelimiter());
        assertEquals(base.getAutoFlush(), result.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteMode_NonNullQuoteMode() {
        CSVFormat base = CSVFormat.DEFAULT;
        QuoteMode qm = QuoteMode.ALL;
        CSVFormat result = base.withQuoteMode(qm);
        assertNotNull(result);
        assertEquals(qm, result.getQuoteMode());
        // Other properties remain unchanged
        assertEquals(base.getDelimiter(), result.getDelimiter());
        assertEquals(base.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(base.getCommentMarker(), result.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(base.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(base.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(base.getNullString(), result.getNullString());

        // headerComments and header may be null, handle null safely
        if (base.getHeaderComments() == null) {
            assertNull(result.getHeaderComments());
        } else {
            assertArrayEquals(base.getHeaderComments(), result.getHeaderComments());
        }
        if (base.getHeader() == null) {
            assertNull(result.getHeader());
        } else {
            assertArrayEquals(base.getHeader(), result.getHeader());
        }

        assertEquals(base.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(base.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), result.getTrim());
        assertEquals(base.getTrailingDelimiter(), result.getTrailingDelimiter());
        assertEquals(base.getAutoFlush(), result.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteMode_Immutability() {
        CSVFormat base = CSVFormat.DEFAULT;
        QuoteMode qm = QuoteMode.MINIMAL;
        CSVFormat result = base.withQuoteMode(qm);
        // Ensure original instance is not modified
        assertNotSame(base, result);
        assertNull(base.getQuoteMode());
        assertEquals(qm, result.getQuoteMode());
    }
}