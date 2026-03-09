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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

class CSVFormat_3_6Test {

    @Test
    @Timeout(8000)
    void testNewFormatWithComma() {
        CSVFormat format = CSVFormat.newFormat(',');
        assertNotNull(format);
        assertEquals(',', format.getDelimiter());
        assertFalse(format.isQuoteCharacterSet());
        assertFalse(format.isCommentMarkerSet());
        assertFalse(format.isEscapeCharacterSet());
        assertFalse(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertNull(format.getRecordSeparator());
        assertFalse(format.isNullStringSet());
        assertNull(format.getHeader());
        assertFalse(format.getSkipHeaderRecord());
        assertFalse(format.getAllowMissingColumnNames());
        assertNull(format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testNewFormatWithTab() {
        CSVFormat format = CSVFormat.newFormat('\t');
        assertNotNull(format);
        assertEquals('\t', format.getDelimiter());
        assertFalse(format.isQuoteCharacterSet());
        assertFalse(format.isCommentMarkerSet());
        assertFalse(format.isEscapeCharacterSet());
        assertFalse(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertNull(format.getRecordSeparator());
        assertFalse(format.isNullStringSet());
        assertNull(format.getHeader());
        assertFalse(format.getSkipHeaderRecord());
        assertFalse(format.getAllowMissingColumnNames());
        assertNull(format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testNewFormatWithSpecialDelimiter() {
        char delimiter = '|';
        CSVFormat format = CSVFormat.newFormat(delimiter);
        assertNotNull(format);
        assertEquals(delimiter, format.getDelimiter());
        assertFalse(format.isQuoteCharacterSet());
        assertFalse(format.isCommentMarkerSet());
        assertFalse(format.isEscapeCharacterSet());
        assertFalse(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertNull(format.getRecordSeparator());
        assertFalse(format.isNullStringSet());
        assertNull(format.getHeader());
        assertFalse(format.getSkipHeaderRecord());
        assertFalse(format.getAllowMissingColumnNames());
        assertNull(format.getQuoteMode());
    }
}