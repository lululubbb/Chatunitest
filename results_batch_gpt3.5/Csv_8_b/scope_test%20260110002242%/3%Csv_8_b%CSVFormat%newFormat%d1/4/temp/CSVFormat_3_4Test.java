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
import org.apache.commons.csv.Quote;
import org.junit.jupiter.api.Test;

class CSVFormat_3_4Test {

    @Test
    @Timeout(8000)
    void testNewFormatWithVariousDelimiters() {
        // Test with comma
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertEquals(Character.valueOf('\0'), formatComma.getQuoteChar() == null ? Character.valueOf('\0') : formatComma.getQuoteChar());
        assertNull(formatComma.getQuotePolicy());
        assertNull(formatComma.getCommentStart());
        assertNull(formatComma.getEscape());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertNull(formatComma.getRecordSeparator());
        assertNull(formatComma.getNullString());
        assertNull(formatComma.getHeader());
        assertFalse(formatComma.getSkipHeaderRecord());

        // Test with tab
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());
        assertEquals(Character.valueOf('\0'), formatTab.getQuoteChar() == null ? Character.valueOf('\0') : formatTab.getQuoteChar());
        assertNull(formatTab.getQuotePolicy());
        assertNull(formatTab.getCommentStart());
        assertNull(formatTab.getEscape());
        assertFalse(formatTab.getIgnoreSurroundingSpaces());
        assertFalse(formatTab.getIgnoreEmptyLines());
        assertNull(formatTab.getRecordSeparator());
        assertNull(formatTab.getNullString());
        assertNull(formatTab.getHeader());
        assertFalse(formatTab.getSkipHeaderRecord());

        // Test with custom delimiter
        CSVFormat formatCustom = CSVFormat.newFormat('|');
        assertNotNull(formatCustom);
        assertEquals('|', formatCustom.getDelimiter());
        assertEquals(Character.valueOf('\0'), formatCustom.getQuoteChar() == null ? Character.valueOf('\0') : formatCustom.getQuoteChar());
        assertNull(formatCustom.getQuotePolicy());
        assertNull(formatCustom.getCommentStart());
        assertNull(formatCustom.getEscape());
        assertFalse(formatCustom.getIgnoreSurroundingSpaces());
        assertFalse(formatCustom.getIgnoreEmptyLines());
        assertNull(formatCustom.getRecordSeparator());
        assertNull(formatCustom.getNullString());
        assertNull(formatCustom.getHeader());
        assertFalse(formatCustom.getSkipHeaderRecord());
    }

}