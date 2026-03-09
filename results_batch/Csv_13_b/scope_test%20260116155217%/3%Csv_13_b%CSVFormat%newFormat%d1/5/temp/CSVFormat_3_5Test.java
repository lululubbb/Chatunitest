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

class CSVFormat_3_5Test {

    @Test
    @Timeout(8000)
    void testNewFormat_withVariousDelimiters() {
        // Test with comma
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertNull(formatComma.getQuoteCharacter());
        assertNull(formatComma.getQuoteMode());
        assertNull(formatComma.getCommentMarker());
        assertNull(formatComma.getEscapeCharacter());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertNull(formatComma.getRecordSeparator());
        assertNull(formatComma.getNullString());
        assertNull(formatComma.getHeader());
        assertNull(formatComma.getHeaderComments());
        assertFalse(formatComma.getSkipHeaderRecord());
        assertFalse(formatComma.getAllowMissingColumnNames());
        assertFalse(formatComma.getIgnoreHeaderCase());

        // Test with tab
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());
        assertNull(formatTab.getQuoteCharacter());
        assertNull(formatTab.getQuoteMode());
        assertNull(formatTab.getCommentMarker());
        assertNull(formatTab.getEscapeCharacter());
        assertFalse(formatTab.getIgnoreSurroundingSpaces());
        assertFalse(formatTab.getIgnoreEmptyLines());
        assertNull(formatTab.getRecordSeparator());
        assertNull(formatTab.getNullString());
        assertNull(formatTab.getHeader());
        assertNull(formatTab.getHeaderComments());
        assertFalse(formatTab.getSkipHeaderRecord());
        assertFalse(formatTab.getAllowMissingColumnNames());
        assertFalse(formatTab.getIgnoreHeaderCase());

        // Test with pipe
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());
        assertNull(formatPipe.getQuoteCharacter());
        assertNull(formatPipe.getQuoteMode());
        assertNull(formatPipe.getCommentMarker());
        assertNull(formatPipe.getEscapeCharacter());
        assertFalse(formatPipe.getIgnoreSurroundingSpaces());
        assertFalse(formatPipe.getIgnoreEmptyLines());
        assertNull(formatPipe.getRecordSeparator());
        assertNull(formatPipe.getNullString());
        assertNull(formatPipe.getHeader());
        assertNull(formatPipe.getHeaderComments());
        assertFalse(formatPipe.getSkipHeaderRecord());
        assertFalse(formatPipe.getAllowMissingColumnNames());
        assertFalse(formatPipe.getIgnoreHeaderCase());
    }
}