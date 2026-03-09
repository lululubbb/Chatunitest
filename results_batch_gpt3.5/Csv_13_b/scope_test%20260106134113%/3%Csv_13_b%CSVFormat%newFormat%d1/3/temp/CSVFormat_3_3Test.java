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

class CSVFormat_3_3Test {

    @Test
    @Timeout(8000)
    void testNewFormatWithVariousDelimiters() {
        // Test with comma delimiter
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertEquals('\0', formatComma.getQuoteCharacter() == null ? '\0' : formatComma.getQuoteCharacter());
        assertFalse(formatComma.getAllowMissingColumnNames());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertNull(formatComma.getRecordSeparator());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertFalse(formatComma.getSkipHeaderRecord());

        // Test with tab delimiter
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());
        assertEquals('\0', formatTab.getQuoteCharacter() == null ? '\0' : formatTab.getQuoteCharacter());
        assertFalse(formatTab.getAllowMissingColumnNames());
        assertFalse(formatTab.getIgnoreEmptyLines());
        assertNull(formatTab.getRecordSeparator());
        assertFalse(formatTab.getIgnoreSurroundingSpaces());
        assertFalse(formatTab.getSkipHeaderRecord());

        // Test with pipe delimiter
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());
        assertEquals('\0', formatPipe.getQuoteCharacter() == null ? '\0' : formatPipe.getQuoteCharacter());
        assertFalse(formatPipe.getAllowMissingColumnNames());
        assertFalse(formatPipe.getIgnoreEmptyLines());
        assertNull(formatPipe.getRecordSeparator());
        assertFalse(formatPipe.getIgnoreSurroundingSpaces());
        assertFalse(formatPipe.getSkipHeaderRecord());

        // Test with special char delimiter
        CSVFormat formatSpecial = CSVFormat.newFormat('#');
        assertNotNull(formatSpecial);
        assertEquals('#', formatSpecial.getDelimiter());
        assertEquals('\0', formatSpecial.getQuoteCharacter() == null ? '\0' : formatSpecial.getQuoteCharacter());
        assertFalse(formatSpecial.getAllowMissingColumnNames());
        assertFalse(formatSpecial.getIgnoreEmptyLines());
        assertNull(formatSpecial.getRecordSeparator());
        assertFalse(formatSpecial.getIgnoreSurroundingSpaces());
        assertFalse(formatSpecial.getSkipHeaderRecord());
    }
}