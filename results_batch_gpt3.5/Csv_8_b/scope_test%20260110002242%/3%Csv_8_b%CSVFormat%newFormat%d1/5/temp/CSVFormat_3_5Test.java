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

class CSVFormat_3_5Test {

    @Test
    @Timeout(8000)
    void testNewFormat_withVariousDelimiters() {
        // Test with comma
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertEquals('\0', formatComma.getQuoteChar() == null ? '\0' : formatComma.getQuoteChar().charValue());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertNull(formatComma.getCommentStart());
        assertNull(formatComma.getEscape());
        assertNull(formatComma.getRecordSeparator());
        assertNull(formatComma.getNullString());
        assertNull(formatComma.getHeader());
        assertFalse(formatComma.getSkipHeaderRecord());

        // Test with tab
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());
        assertEquals('\0', formatTab.getQuoteChar() == null ? '\0' : formatTab.getQuoteChar().charValue());

        // Test with special char
        CSVFormat formatSpecial = CSVFormat.newFormat('|');
        assertNotNull(formatSpecial);
        assertEquals('|', formatSpecial.getDelimiter());
        assertEquals('\0', formatSpecial.getQuoteChar() == null ? '\0' : formatSpecial.getQuoteChar().charValue());
    }
}