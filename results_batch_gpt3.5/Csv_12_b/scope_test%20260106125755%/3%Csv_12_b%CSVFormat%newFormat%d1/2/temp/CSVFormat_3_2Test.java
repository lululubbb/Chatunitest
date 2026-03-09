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

class CSVFormat_3_2Test {

    @Test
    @Timeout(8000)
    void testNewFormatWithVariousDelimiters() {
        // Test with comma delimiter
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertFalse(formatComma.getQuoteCharacter() != null);
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertNull(formatComma.getRecordSeparator());
        assertFalse(formatComma.getAllowMissingColumnNames());
        assertFalse(formatComma.getSkipHeaderRecord());
        assertNull(formatComma.getNullString());
        assertNull(formatComma.getHeader());
        assertNull(formatComma.getQuoteMode());
        assertNull(formatComma.getCommentMarker());
        assertNull(formatComma.getEscapeCharacter());

        // Test with tab delimiter
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());

        // Test with pipe delimiter
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());

        // Test with space delimiter
        CSVFormat formatSpace = CSVFormat.newFormat(' ');
        assertNotNull(formatSpace);
        assertEquals(' ', formatSpace.getDelimiter());
    }
}