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
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_67_4Test {

    @Test
    @Timeout(8000)
    void testWithQuoteMode_nullQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withQuoteMode(null);
        assertNotNull(result);
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteCharacter(), result.getQuoteCharacter());
        assertNull(result.getQuoteMode());
        assertEquals(format.getCommentMarker(), result.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertArrayEquals(format.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(format.getTrim(), result.getTrim());
        assertEquals(format.getTrailingDelimiter(), result.getTrailingDelimiter());
        assertEquals(format.getAutoFlush(), result.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteMode_nonNullQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode quoteMode = QuoteMode.ALL;
        CSVFormat result = format.withQuoteMode(quoteMode);
        assertNotNull(result);
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(quoteMode, result.getQuoteMode());
        assertEquals(format.getCommentMarker(), result.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertArrayEquals(format.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(format.getTrim(), result.getTrim());
        assertEquals(format.getTrailingDelimiter(), result.getTrailingDelimiter());
        assertEquals(format.getAutoFlush(), result.getAutoFlush());
    }
}