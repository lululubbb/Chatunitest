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

class CSVFormat_3_6Test {

    @Test
    @Timeout(8000)
    void testNewFormat_withVariousDelimiters() {
        // Test with comma
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertNull(formatComma.getQuoteCharacter());
        assertFalse(formatComma.getAllowMissingColumnNames());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertNull(formatComma.getRecordSeparator());
        assertFalse(formatComma.getSkipHeaderRecord());

        // Test with tab
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());
        assertNull(formatTab.getQuoteCharacter());
        assertFalse(formatTab.getAllowMissingColumnNames());
        assertFalse(formatTab.getIgnoreEmptyLines());
        assertNull(formatTab.getRecordSeparator());
        assertFalse(formatTab.getSkipHeaderRecord());

        // Test with semicolon
        CSVFormat formatSemicolon = CSVFormat.newFormat(';');
        assertNotNull(formatSemicolon);
        assertEquals(';', formatSemicolon.getDelimiter());
        assertNull(formatSemicolon.getQuoteCharacter());
        assertFalse(formatSemicolon.getAllowMissingColumnNames());
        assertFalse(formatSemicolon.getIgnoreEmptyLines());
        assertNull(formatSemicolon.getRecordSeparator());
        assertFalse(formatSemicolon.getSkipHeaderRecord());

        // Test with pipe
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());
        assertNull(formatPipe.getQuoteCharacter());
        assertFalse(formatPipe.getAllowMissingColumnNames());
        assertFalse(formatPipe.getIgnoreEmptyLines());
        assertNull(formatPipe.getRecordSeparator());
        assertFalse(formatPipe.getSkipHeaderRecord());

        // Test with space
        CSVFormat formatSpace = CSVFormat.newFormat(' ');
        assertNotNull(formatSpace);
        assertEquals(' ', formatSpace.getDelimiter());
        assertNull(formatSpace.getQuoteCharacter());
        assertFalse(formatSpace.getAllowMissingColumnNames());
        assertFalse(formatSpace.getIgnoreEmptyLines());
        assertNull(formatSpace.getRecordSeparator());
        assertFalse(formatSpace.getSkipHeaderRecord());
    }
}