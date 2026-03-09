package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
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
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertFalse(formatComma.isQuoteCharacterSet());
        assertFalse(formatComma.isCommentMarkerSet());
        assertFalse(formatComma.getSkipHeaderRecord());
        assertFalse(formatComma.getTrailingDelimiter());
        assertFalse(formatComma.getTrim());
        assertFalse(formatComma.getAllowMissingColumnNames());

        // Test with tab delimiter
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());
        assertFalse(formatTab.getIgnoreEmptyLines());
        assertFalse(formatTab.getIgnoreSurroundingSpaces());
        assertFalse(formatTab.isQuoteCharacterSet());
        assertFalse(formatTab.isCommentMarkerSet());
        assertFalse(formatTab.getSkipHeaderRecord());
        assertFalse(formatTab.getTrailingDelimiter());
        assertFalse(formatTab.getTrim());
        assertFalse(formatTab.getAllowMissingColumnNames());

        // Test with pipe delimiter
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());
        assertFalse(formatPipe.getIgnoreEmptyLines());
        assertFalse(formatPipe.getIgnoreSurroundingSpaces());
        assertFalse(formatPipe.isQuoteCharacterSet());
        assertFalse(formatPipe.isCommentMarkerSet());
        assertFalse(formatPipe.getSkipHeaderRecord());
        assertFalse(formatPipe.getTrailingDelimiter());
        assertFalse(formatPipe.getTrim());
        assertFalse(formatPipe.getAllowMissingColumnNames());

        // Test with special character delimiter
        CSVFormat formatStar = CSVFormat.newFormat('*');
        assertNotNull(formatStar);
        assertEquals('*', formatStar.getDelimiter());
        assertFalse(formatStar.getIgnoreEmptyLines());
        assertFalse(formatStar.getIgnoreSurroundingSpaces());
        assertFalse(formatStar.isQuoteCharacterSet());
        assertFalse(formatStar.isCommentMarkerSet());
        assertFalse(formatStar.getSkipHeaderRecord());
        assertFalse(formatStar.getTrailingDelimiter());
        assertFalse(formatStar.getTrim());
        assertFalse(formatStar.getAllowMissingColumnNames());
    }
}