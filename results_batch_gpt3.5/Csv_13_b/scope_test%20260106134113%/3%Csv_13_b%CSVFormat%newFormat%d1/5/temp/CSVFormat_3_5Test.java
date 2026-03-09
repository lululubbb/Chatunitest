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
    void testNewFormatWithVariousDelimiters() {
        // Test with comma delimiter
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertNull(formatComma.getQuoteCharacter());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertFalse(formatComma.getSkipHeaderRecord());
        assertFalse(formatComma.getAllowMissingColumnNames());
        assertNull(formatComma.getRecordSeparator());
        assertNull(formatComma.getNullString());
        assertNull(formatComma.getHeader());
        assertNull(formatComma.getHeaderComments());
        assertFalse(formatComma.isCommentMarkerSet());
        assertFalse(formatComma.isEscapeCharacterSet());
        assertFalse(formatComma.isNullStringSet());
        assertFalse(formatComma.isQuoteCharacterSet());

        // Test with tab delimiter
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());

        // Test with pipe delimiter
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());

        // Test with newline character as delimiter (edge case)
        CSVFormat formatNewline = CSVFormat.newFormat('\n');
        assertNotNull(formatNewline);
        assertEquals('\n', formatNewline.getDelimiter());

        // Test with special char delimiter
        CSVFormat formatSpecial = CSVFormat.newFormat('\u2603'); // Unicode snowman
        assertNotNull(formatSpecial);
        assertEquals('\u2603', formatSpecial.getDelimiter());
    }

}