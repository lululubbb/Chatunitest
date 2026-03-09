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

class CSVFormat_3_1Test {

    @Test
    @Timeout(8000)
    void testNewFormatWithVariousDelimiters() {
        // Test with comma delimiter
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertNull(formatComma.getQuoteCharacter());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertFalse(formatComma.getSkipHeaderRecord());
        assertFalse(formatComma.getTrailingDelimiter());
        assertFalse(formatComma.getAutoFlush());
        assertFalse(formatComma.getAllowMissingColumnNames());
        assertFalse(formatComma.getIgnoreHeaderCase());
        assertFalse(formatComma.getTrim());
        assertNull(formatComma.getCommentMarker());
        assertNull(formatComma.getEscapeCharacter());
        assertNull(formatComma.getHeader());
        assertNull(formatComma.getHeaderComments());
        assertNull(formatComma.getNullString());
        assertNull(formatComma.getQuoteMode());
        assertNull(formatComma.getRecordSeparator());

        // Test with tab delimiter
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());
        assertNull(formatTab.getQuoteCharacter());
        assertFalse(formatTab.getIgnoreEmptyLines());
        assertFalse(formatTab.getIgnoreSurroundingSpaces());
        assertFalse(formatTab.getSkipHeaderRecord());
        assertFalse(formatTab.getTrailingDelimiter());
        assertFalse(formatTab.getAutoFlush());
        assertFalse(formatTab.getAllowMissingColumnNames());
        assertFalse(formatTab.getIgnoreHeaderCase());
        assertFalse(formatTab.getTrim());
        assertNull(formatTab.getCommentMarker());
        assertNull(formatTab.getEscapeCharacter());
        assertNull(formatTab.getHeader());
        assertNull(formatTab.getHeaderComments());
        assertNull(formatTab.getNullString());
        assertNull(formatTab.getQuoteMode());
        assertNull(formatTab.getRecordSeparator());

        // Test with pipe delimiter
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());
        assertNull(formatPipe.getQuoteCharacter());
        assertFalse(formatPipe.getIgnoreEmptyLines());
        assertFalse(formatPipe.getIgnoreSurroundingSpaces());
        assertFalse(formatPipe.getSkipHeaderRecord());
        assertFalse(formatPipe.getTrailingDelimiter());
        assertFalse(formatPipe.getAutoFlush());
        assertFalse(formatPipe.getAllowMissingColumnNames());
        assertFalse(formatPipe.getIgnoreHeaderCase());
        assertFalse(formatPipe.getTrim());
        assertNull(formatPipe.getCommentMarker());
        assertNull(formatPipe.getEscapeCharacter());
        assertNull(formatPipe.getHeader());
        assertNull(formatPipe.getHeaderComments());
        assertNull(formatPipe.getNullString());
        assertNull(formatPipe.getQuoteMode());
        assertNull(formatPipe.getRecordSeparator());

        // Test with special character delimiter
        CSVFormat formatDollar = CSVFormat.newFormat('$');
        assertNotNull(formatDollar);
        assertEquals('$', formatDollar.getDelimiter());
        assertNull(formatDollar.getQuoteCharacter());
        assertFalse(formatDollar.getIgnoreEmptyLines());
        assertFalse(formatDollar.getIgnoreSurroundingSpaces());
        assertFalse(formatDollar.getSkipHeaderRecord());
        assertFalse(formatDollar.getTrailingDelimiter());
        assertFalse(formatDollar.getAutoFlush());
        assertFalse(formatDollar.getAllowMissingColumnNames());
        assertFalse(formatDollar.getIgnoreHeaderCase());
        assertFalse(formatDollar.getTrim());
        assertNull(formatDollar.getCommentMarker());
        assertNull(formatDollar.getEscapeCharacter());
        assertNull(formatDollar.getHeader());
        assertNull(formatDollar.getHeaderComments());
        assertNull(formatDollar.getNullString());
        assertNull(formatDollar.getQuoteMode());
        assertNull(formatDollar.getRecordSeparator());
    }
}