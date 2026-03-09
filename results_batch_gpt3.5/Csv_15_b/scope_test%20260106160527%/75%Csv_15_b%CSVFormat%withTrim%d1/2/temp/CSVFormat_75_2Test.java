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

class CSVFormat_75_2Test {

    @Test
    @Timeout(8000)
    void testWithTrimTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat trimmedFormat = format.withTrim(true);

        assertNotNull(trimmedFormat);
        assertTrue(trimmedFormat.getTrim());
        // other properties remain unchanged
        assertEquals(format.getDelimiter(), trimmedFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), trimmedFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), trimmedFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), trimmedFormat.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), trimmedFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), trimmedFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), trimmedFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), trimmedFormat.getRecordSeparator());
        assertEquals(format.getNullString(), trimmedFormat.getNullString());
        assertArrayEquals(format.getHeaderComments(), trimmedFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), trimmedFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), trimmedFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), trimmedFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), trimmedFormat.getIgnoreHeaderCase());
        assertEquals(format.getTrailingDelimiter(), trimmedFormat.getTrailingDelimiter());
        assertEquals(format.getAutoFlush(), trimmedFormat.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithTrimFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true);
        CSVFormat untrimmedFormat = format.withTrim(false);

        assertNotNull(untrimmedFormat);
        assertFalse(untrimmedFormat.getTrim());
        // other properties remain unchanged
        assertEquals(format.getDelimiter(), untrimmedFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), untrimmedFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), untrimmedFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), untrimmedFormat.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), untrimmedFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), untrimmedFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), untrimmedFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), untrimmedFormat.getRecordSeparator());
        assertEquals(format.getNullString(), untrimmedFormat.getNullString());
        assertArrayEquals(format.getHeaderComments(), untrimmedFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), untrimmedFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), untrimmedFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), untrimmedFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), untrimmedFormat.getIgnoreHeaderCase());
        assertEquals(format.getTrailingDelimiter(), untrimmedFormat.getTrailingDelimiter());
        assertEquals(format.getAutoFlush(), untrimmedFormat.getAutoFlush());
    }
}