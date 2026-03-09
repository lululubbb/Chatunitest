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
    void testNewFormat_withVariousDelimiters() {
        // Test with comma delimiter
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertFalse(formatComma.isQuoteCharacterSet());
        assertFalse(formatComma.getAllowMissingColumnNames());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertNull(formatComma.getCommentMarker());
        assertNull(formatComma.getEscapeCharacter());
        assertNull(formatComma.getNullString());
        assertNull(formatComma.getHeader());
        assertNull(formatComma.getHeaderComments());
        assertFalse(formatComma.getSkipHeaderRecord());
        assertFalse(formatComma.getIgnoreHeaderCase());

        // Test with tab delimiter
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());

        // Test with special character delimiter
        CSVFormat formatStar = CSVFormat.newFormat('*');
        assertNotNull(formatStar);
        assertEquals('*', formatStar.getDelimiter());

        // Test with line break character (should be allowed as delimiter)
        CSVFormat formatLF = CSVFormat.newFormat('\n');
        assertNotNull(formatLF);
        assertEquals('\n', formatLF.getDelimiter());

        // Test with quote character delimiter (unusual but allowed)
        CSVFormat formatQuote = CSVFormat.newFormat('"');
        assertNotNull(formatQuote);
        assertEquals('"', formatQuote.getDelimiter());
    }
}