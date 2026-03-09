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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

class CSVFormat_3_2Test {

    @Test
    @Timeout(8000)
    void testNewFormatWithCommaDelimiter() {
        CSVFormat format = CSVFormat.newFormat(',');
        assertNotNull(format);
        assertEquals(',', format.getDelimiter());
        assertEquals('\0', format.getQuoteCharacter() == null ? '\0' : format.getQuoteCharacter());
        assertNull(format.getQuoteMode());
        assertNull(format.getCommentMarker());
        assertNull(format.getEscapeCharacter());
        assertFalse(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getAllowMissingColumnNames());
        assertFalse(format.getIgnoreEmptyLines());
        assertNull(format.getRecordSeparator());
        assertNull(format.getNullString());
        assertNull(format.getHeader());
        assertNull(format.getHeaderComments());
        assertFalse(format.getSkipHeaderRecord());
        assertFalse(format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testNewFormatWithTabDelimiter() {
        CSVFormat format = CSVFormat.newFormat('\t');
        assertNotNull(format);
        assertEquals('\t', format.getDelimiter());
        assertEquals('\0', format.getQuoteCharacter() == null ? '\0' : format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testNewFormatWithBackslashDelimiter() {
        CSVFormat format = CSVFormat.newFormat('\\');
        assertNotNull(format);
        assertEquals('\\', format.getDelimiter());
        assertEquals('\0', format.getQuoteCharacter() == null ? '\0' : format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testNewFormatWithSpecialCharDelimiter() {
        CSVFormat format = CSVFormat.newFormat('\n');
        assertNotNull(format);
        assertEquals('\n', format.getDelimiter());
        assertEquals('\0', format.getQuoteCharacter() == null ? '\0' : format.getQuoteCharacter());
    }
}