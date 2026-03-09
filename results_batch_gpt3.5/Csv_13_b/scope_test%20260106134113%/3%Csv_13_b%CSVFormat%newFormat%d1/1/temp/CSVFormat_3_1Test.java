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

import java.lang.reflect.Field;

class CSVFormat_3_1Test {

    @Test
    @Timeout(8000)
    void testNewFormatWithCommaDelimiter() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        assertNotNull(format);
        assertEquals(',', format.getDelimiter());
        assertNull(format.getQuoteCharacter());
        assertEquals(QuoteMode.MINIMAL, format.getQuoteMode());
        assertNull(format.getCommentMarker());
        assertNull(format.getEscapeCharacter());
        assertFalse(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertNull(format.getRecordSeparator());
        assertNull(format.getNullString());
        assertNull(format.getHeader());
        assertNull(format.getHeaderComments());
        assertFalse(format.getSkipHeaderRecord());
        assertFalse(format.getAllowMissingColumnNames());
        assertFalse(format.getIgnoreHeaderCase());

        // Reflection check for private field quoteMode
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        assertEquals(QuoteMode.MINIMAL, quoteModeField.get(format));
    }

    @Test
    @Timeout(8000)
    void testNewFormatWithTabDelimiter() throws Exception {
        CSVFormat format = CSVFormat.newFormat('\t');
        assertNotNull(format);
        assertEquals('\t', format.getDelimiter());
        assertNull(format.getQuoteCharacter());
        assertEquals(QuoteMode.MINIMAL, format.getQuoteMode());
        assertNull(format.getCommentMarker());
        assertNull(format.getEscapeCharacter());
        assertFalse(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertNull(format.getRecordSeparator());
        assertNull(format.getNullString());
        assertNull(format.getHeader());
        assertNull(format.getHeaderComments());
        assertFalse(format.getSkipHeaderRecord());
        assertFalse(format.getAllowMissingColumnNames());
        assertFalse(format.getIgnoreHeaderCase());

        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        assertEquals(QuoteMode.MINIMAL, quoteModeField.get(format));
    }

    @Test
    @Timeout(8000)
    void testNewFormatWithPipeDelimiter() throws Exception {
        CSVFormat format = CSVFormat.newFormat('|');
        assertNotNull(format);
        assertEquals('|', format.getDelimiter());
        assertNull(format.getQuoteCharacter());
        assertEquals(QuoteMode.MINIMAL, format.getQuoteMode());
        assertNull(format.getCommentMarker());
        assertNull(format.getEscapeCharacter());
        assertFalse(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertNull(format.getRecordSeparator());
        assertNull(format.getNullString());
        assertNull(format.getHeader());
        assertNull(format.getHeaderComments());
        assertFalse(format.getSkipHeaderRecord());
        assertFalse(format.getAllowMissingColumnNames());
        assertFalse(format.getIgnoreHeaderCase());

        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        assertEquals(QuoteMode.MINIMAL, quoteModeField.get(format));
    }
}