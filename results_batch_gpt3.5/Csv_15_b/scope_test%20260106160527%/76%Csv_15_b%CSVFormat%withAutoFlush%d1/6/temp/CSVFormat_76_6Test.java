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

class CSVFormat_76_6Test {

    @Test
    @Timeout(8000)
    void testWithAutoFlushTrue() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat resultFormat = baseFormat.withAutoFlush(true);

        assertNotSame(baseFormat, resultFormat);
        assertEquals(baseFormat.getDelimiter(), resultFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), resultFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), resultFormat.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), resultFormat.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), resultFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), resultFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), resultFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), resultFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), resultFormat.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), resultFormat.getHeaderComments());
        assertArrayEquals(baseFormat.getHeader(), resultFormat.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), resultFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), resultFormat.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), resultFormat.getIgnoreHeaderCase());
        assertEquals(baseFormat.getTrim(), resultFormat.getTrim());
        assertEquals(baseFormat.getTrailingDelimiter(), resultFormat.getTrailingDelimiter());
        assertTrue(resultFormat.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithAutoFlushFalse() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withAutoFlush(true);
        assertTrue(baseFormat.getAutoFlush());

        CSVFormat resultFormat = baseFormat.withAutoFlush(false);

        assertNotSame(baseFormat, resultFormat);
        assertFalse(resultFormat.getAutoFlush());
        // Other properties unchanged
        assertEquals(baseFormat.getDelimiter(), resultFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), resultFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), resultFormat.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), resultFormat.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), resultFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), resultFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), resultFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), resultFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), resultFormat.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), resultFormat.getHeaderComments());
        assertArrayEquals(baseFormat.getHeader(), resultFormat.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), resultFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), resultFormat.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), resultFormat.getIgnoreHeaderCase());
        assertEquals(baseFormat.getTrim(), resultFormat.getTrim());
        assertEquals(baseFormat.getTrailingDelimiter(), resultFormat.getTrailingDelimiter());
    }
}