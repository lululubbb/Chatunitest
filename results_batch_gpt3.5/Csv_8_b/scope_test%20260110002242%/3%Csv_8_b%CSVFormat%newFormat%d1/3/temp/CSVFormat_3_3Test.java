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
    void testNewFormat_withVariousDelimiters() {
        // Test with comma
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertEquals(Character.valueOf('\0'), formatComma.getQuoteChar() == null ? Character.valueOf('\0') : formatComma.getQuoteChar());
        assertNull(formatComma.getQuotePolicy());
        assertEquals(Character.valueOf('\0'), formatComma.getCommentStart() == null ? Character.valueOf('\0') : formatComma.getCommentStart());
        assertEquals(Character.valueOf('\0'), formatComma.getEscape() == null ? Character.valueOf('\0') : formatComma.getEscape());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertNull(formatComma.getRecordSeparator());
        assertNull(formatComma.getNullString());
        assertNull(formatComma.getHeader());
        assertFalse(formatComma.getSkipHeaderRecord());

        // Test with semicolon
        CSVFormat formatSemi = CSVFormat.newFormat(';');
        assertNotNull(formatSemi);
        assertEquals(';', formatSemi.getDelimiter());

        // Test with tab
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());

        // Test with pipe
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());
    }
}