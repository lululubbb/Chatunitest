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

class CSVFormat_3_6Test {

    @Test
    @Timeout(8000)
    void testNewFormat_withVariousDelimiters() throws Exception {
        // Test with comma
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertNull(formatComma.getQuoteChar());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertNull(formatComma.getCommentStart());
        assertNull(formatComma.getEscape());
        assertNull(formatComma.getRecordSeparator());
        assertNull(formatComma.getNullString());
        assertNull(formatComma.getHeader());
        assertFalse(formatComma.getSkipHeaderRecord());

        // Test with tab
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());
        assertNull(formatTab.getQuoteChar());
        assertFalse(formatTab.getIgnoreEmptyLines());
        assertFalse(formatTab.getIgnoreSurroundingSpaces());
        assertNull(formatTab.getCommentStart());
        assertNull(formatTab.getEscape());
        assertNull(formatTab.getRecordSeparator());
        assertNull(formatTab.getNullString());
        assertNull(formatTab.getHeader());
        assertFalse(formatTab.getSkipHeaderRecord());

        // Test with pipe
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());
        assertNull(formatPipe.getQuoteChar());
        assertFalse(formatPipe.getIgnoreEmptyLines());
        assertFalse(formatPipe.getIgnoreSurroundingSpaces());
        assertNull(formatPipe.getCommentStart());
        assertNull(formatPipe.getEscape());
        assertNull(formatPipe.getRecordSeparator());
        assertNull(formatPipe.getNullString());
        assertNull(formatPipe.getHeader());
        assertFalse(formatPipe.getSkipHeaderRecord());

        // Test with special char
        CSVFormat formatSpecial = CSVFormat.newFormat('\u2603'); // snowman char
        assertNotNull(formatSpecial);
        assertEquals('\u2603', formatSpecial.getDelimiter());
        assertNull(formatSpecial.getQuoteChar());
        assertFalse(formatSpecial.getIgnoreEmptyLines());
        assertFalse(formatSpecial.getIgnoreSurroundingSpaces());
        assertNull(formatSpecial.getCommentStart());
        assertNull(formatSpecial.getEscape());
        assertNull(formatSpecial.getRecordSeparator());
        assertNull(formatSpecial.getNullString());
        assertNull(formatSpecial.getHeader());
        assertFalse(formatSpecial.getSkipHeaderRecord());
    }
}