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

class CSVFormat_3_1Test {

    @Test
    @Timeout(8000)
    void testNewFormat_withVariousDelimiters() {
        // Test with comma delimiter
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertEquals(Character.valueOf(null), formatComma.getQuoteChar());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertNull(formatComma.getCommentStart());
        assertNull(formatComma.getEscape());
        assertNull(formatComma.getNullString());
        assertNull(formatComma.getHeader());
        assertFalse(formatComma.getSkipHeaderRecord());
        assertNull(formatComma.getQuotePolicy());
        assertNull(formatComma.getRecordSeparator());

        // Test with tab delimiter
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());
        assertEquals(Character.valueOf(null), formatTab.getQuoteChar());
        assertFalse(formatTab.getIgnoreEmptyLines());
        assertFalse(formatTab.getIgnoreSurroundingSpaces());
        assertNull(formatTab.getCommentStart());
        assertNull(formatTab.getEscape());
        assertNull(formatTab.getNullString());
        assertNull(formatTab.getHeader());
        assertFalse(formatTab.getSkipHeaderRecord());
        assertNull(formatTab.getQuotePolicy());
        assertNull(formatTab.getRecordSeparator());

        // Test with pipe delimiter
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());
        assertEquals(Character.valueOf(null), formatPipe.getQuoteChar());
        assertFalse(formatPipe.getIgnoreEmptyLines());
        assertFalse(formatPipe.getIgnoreSurroundingSpaces());
        assertNull(formatPipe.getCommentStart());
        assertNull(formatPipe.getEscape());
        assertNull(formatPipe.getNullString());
        assertNull(formatPipe.getHeader());
        assertFalse(formatPipe.getSkipHeaderRecord());
        assertNull(formatPipe.getQuotePolicy());
        assertNull(formatPipe.getRecordSeparator());

        // Test with special char delimiter
        CSVFormat formatSpecial = CSVFormat.newFormat('\u2603'); // Unicode snowman
        assertNotNull(formatSpecial);
        assertEquals('\u2603', formatSpecial.getDelimiter());
        assertEquals(Character.valueOf(null), formatSpecial.getQuoteChar());
        assertFalse(formatSpecial.getIgnoreEmptyLines());
        assertFalse(formatSpecial.getIgnoreSurroundingSpaces());
        assertNull(formatSpecial.getCommentStart());
        assertNull(formatSpecial.getEscape());
        assertNull(formatSpecial.getNullString());
        assertNull(formatSpecial.getHeader());
        assertFalse(formatSpecial.getSkipHeaderRecord());
        assertNull(formatSpecial.getQuotePolicy());
        assertNull(formatSpecial.getRecordSeparator());
    }
}