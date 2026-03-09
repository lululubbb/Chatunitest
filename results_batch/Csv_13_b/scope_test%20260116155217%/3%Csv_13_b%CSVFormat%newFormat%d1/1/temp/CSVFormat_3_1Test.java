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
        // Test with comma
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertNull(formatComma.getQuoteCharacter());
        assertFalse(formatComma.getAllowMissingColumnNames());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertNull(formatComma.getCommentMarker());
        assertNull(formatComma.getEscapeCharacter());
        assertNull(formatComma.getNullString());
        assertNull(formatComma.getRecordSeparator());
        assertFalse(formatComma.getSkipHeaderRecord());
        assertFalse(formatComma.getIgnoreHeaderCase());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertNull(formatComma.getHeader());
        assertNull(formatComma.getHeaderComments());
        assertNull(formatComma.getQuoteMode());

        // Test with tab
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());

        // Test with semicolon
        CSVFormat formatSemi = CSVFormat.newFormat(';');
        assertNotNull(formatSemi);
        assertEquals(';', formatSemi.getDelimiter());

        // Test with pipe
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());

        // Test with space
        CSVFormat formatSpace = CSVFormat.newFormat(' ');
        assertNotNull(formatSpace);
        assertEquals(' ', formatSpace.getDelimiter());
    }
}