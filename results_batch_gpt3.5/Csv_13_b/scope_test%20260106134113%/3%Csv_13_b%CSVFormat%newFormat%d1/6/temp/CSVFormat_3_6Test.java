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

public class CSVFormat_3_6Test {

    @Test
    @Timeout(8000)
    public void testNewFormatWithVariousDelimiters() {
        // Test with comma
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertNull(formatComma.getQuoteCharacter());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertFalse(formatComma.getAllowMissingColumnNames());
        assertNull(formatComma.getRecordSeparator());
        assertNull(formatComma.getNullString());
        assertNull(formatComma.getHeader());
        assertNull(formatComma.getHeaderComments());
        assertFalse(formatComma.getSkipHeaderRecord());
        assertFalse(formatComma.getIgnoreHeaderCase());
        assertNull(formatComma.getQuoteMode());

        // Test with tab
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());
        assertNull(formatTab.getQuoteCharacter());
        assertFalse(formatTab.getIgnoreEmptyLines());
        assertFalse(formatTab.getIgnoreSurroundingSpaces());
        assertFalse(formatTab.getAllowMissingColumnNames());
        assertNull(formatTab.getRecordSeparator());
        assertNull(formatTab.getNullString());
        assertNull(formatTab.getHeader());
        assertNull(formatTab.getHeaderComments());
        assertFalse(formatTab.getSkipHeaderRecord());
        assertFalse(formatTab.getIgnoreHeaderCase());
        assertNull(formatTab.getQuoteMode());

        // Test with pipe
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());
        assertNull(formatPipe.getQuoteCharacter());
        assertFalse(formatPipe.getIgnoreEmptyLines());
        assertFalse(formatPipe.getIgnoreSurroundingSpaces());
        assertFalse(formatPipe.getAllowMissingColumnNames());
        assertNull(formatPipe.getRecordSeparator());
        assertNull(formatPipe.getNullString());
        assertNull(formatPipe.getHeader());
        assertNull(formatPipe.getHeaderComments());
        assertFalse(formatPipe.getSkipHeaderRecord());
        assertFalse(formatPipe.getIgnoreHeaderCase());
        assertNull(formatPipe.getQuoteMode());

        // Test with a special character
        CSVFormat formatStar = CSVFormat.newFormat('*');
        assertNotNull(formatStar);
        assertEquals('*', formatStar.getDelimiter());
        assertNull(formatStar.getQuoteCharacter());
        assertFalse(formatStar.getIgnoreEmptyLines());
        assertFalse(formatStar.getIgnoreSurroundingSpaces());
        assertFalse(formatStar.getAllowMissingColumnNames());
        assertNull(formatStar.getRecordSeparator());
        assertNull(formatStar.getNullString());
        assertNull(formatStar.getHeader());
        assertNull(formatStar.getHeaderComments());
        assertFalse(formatStar.getSkipHeaderRecord());
        assertFalse(formatStar.getIgnoreHeaderCase());
        assertNull(formatStar.getQuoteMode());
    }
}