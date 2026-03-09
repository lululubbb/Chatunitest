package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_3_3Test {

    @Test
    @Timeout(8000)
    void testNewFormatWithVariousDelimiters() throws Exception {
        // Test with comma delimiter
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertFalse(formatComma.isQuoteCharacterSet());
        assertNull(formatComma.getQuoteMode());
        assertNull(formatComma.getCommentMarker());
        assertNull(formatComma.getEscapeCharacter());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertNull(formatComma.getRecordSeparator());
        assertNull(formatComma.getNullString());
        assertNull(formatComma.getHeader());
        assertFalse(formatComma.getSkipHeaderRecord());
        assertFalse(formatComma.getAllowMissingColumnNames());

        // Test with tab delimiter
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());
        assertFalse(formatTab.isQuoteCharacterSet());
        assertNull(formatTab.getQuoteMode());
        assertNull(formatTab.getCommentMarker());
        assertNull(formatTab.getEscapeCharacter());
        assertFalse(formatTab.getIgnoreSurroundingSpaces());
        assertFalse(formatTab.getIgnoreEmptyLines());
        assertNull(formatTab.getRecordSeparator());
        assertNull(formatTab.getNullString());
        assertNull(formatTab.getHeader());
        assertFalse(formatTab.getSkipHeaderRecord());
        assertFalse(formatTab.getAllowMissingColumnNames());

        // Test with semicolon delimiter
        CSVFormat formatSemi = CSVFormat.newFormat(';');
        assertNotNull(formatSemi);
        assertEquals(';', formatSemi.getDelimiter());
        assertFalse(formatSemi.isQuoteCharacterSet());
        assertNull(formatSemi.getQuoteMode());
        assertNull(formatSemi.getCommentMarker());
        assertNull(formatSemi.getEscapeCharacter());
        assertFalse(formatSemi.getIgnoreSurroundingSpaces());
        assertFalse(formatSemi.getIgnoreEmptyLines());
        assertNull(formatSemi.getRecordSeparator());
        assertNull(formatSemi.getNullString());
        assertNull(formatSemi.getHeader());
        assertFalse(formatSemi.getSkipHeaderRecord());
        assertFalse(formatSemi.getAllowMissingColumnNames());

        // Test with pipe delimiter
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());
        assertFalse(formatPipe.isQuoteCharacterSet());
        assertNull(formatPipe.getQuoteMode());
        assertNull(formatPipe.getCommentMarker());
        assertNull(formatPipe.getEscapeCharacter());
        assertFalse(formatPipe.getIgnoreSurroundingSpaces());
        assertFalse(formatPipe.getIgnoreEmptyLines());
        assertNull(formatPipe.getRecordSeparator());
        assertNull(formatPipe.getNullString());
        assertNull(formatPipe.getHeader());
        assertFalse(formatPipe.getSkipHeaderRecord());
        assertFalse(formatPipe.getAllowMissingColumnNames());
    }
}