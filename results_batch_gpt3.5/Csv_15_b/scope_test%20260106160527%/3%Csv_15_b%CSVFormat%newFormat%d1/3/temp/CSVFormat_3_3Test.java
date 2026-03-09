package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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

class CSVFormat_3_3Test {

    @Test
    @Timeout(8000)
    void testNewFormat_withVariousDelimiters() throws Exception {
        // Test with comma delimiter
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertFalse(formatComma.getAllowMissingColumnNames());
        assertNull(formatComma.getQuoteCharacter());
        assertNull(formatComma.getEscapeCharacter());
        assertNull(formatComma.getCommentMarker());
        assertNull(formatComma.getHeader());
        assertNull(formatComma.getHeaderComments());
        assertFalse(formatComma.getIgnoreHeaderCase());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertFalse(formatComma.getSkipHeaderRecord());
        assertFalse(formatComma.getTrailingDelimiter());
        assertFalse(formatComma.getTrim());
        assertFalse(formatComma.getAutoFlush());
        assertNull(formatComma.getNullString());
        assertNull(formatComma.getQuoteMode());
        assertNull(formatComma.getRecordSeparator());

        // Test with tab delimiter
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());

        // Test with pipe delimiter
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());

        // Test with a special char delimiter
        CSVFormat formatSpecial = CSVFormat.newFormat('#');
        assertNotNull(formatSpecial);
        assertEquals('#', formatSpecial.getDelimiter());
    }
}