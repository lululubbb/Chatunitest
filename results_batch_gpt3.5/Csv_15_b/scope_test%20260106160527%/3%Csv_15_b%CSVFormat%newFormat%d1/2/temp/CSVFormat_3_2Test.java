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
import org.junit.jupiter.api.Test;

class CSVFormat_3_2Test {

    @Test
    @Timeout(8000)
    void testNewFormatWithVariousDelimiters() throws Exception {
        // Test with comma
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertFalse(formatComma.isQuoteCharacterSet());
        assertFalse(formatComma.isEscapeCharacterSet());
        assertNull(formatComma.getRecordSeparator());
        assertFalse(formatComma.getSkipHeaderRecord());
        assertFalse(formatComma.getTrailingDelimiter());
        assertFalse(formatComma.getAllowMissingColumnNames());
        assertFalse(formatComma.getIgnoreHeaderCase());
        assertFalse(formatComma.getTrim());
        assertFalse(formatComma.getAutoFlush());

        // Test with tab
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());
        assertFalse(formatTab.getIgnoreEmptyLines());
        assertFalse(formatTab.getIgnoreSurroundingSpaces());
        assertFalse(formatTab.isQuoteCharacterSet());
        assertFalse(formatTab.isEscapeCharacterSet());
        assertNull(formatTab.getRecordSeparator());
        assertFalse(formatTab.getSkipHeaderRecord());
        assertFalse(formatTab.getTrailingDelimiter());
        assertFalse(formatTab.getAllowMissingColumnNames());
        assertFalse(formatTab.getIgnoreHeaderCase());
        assertFalse(formatTab.getTrim());
        assertFalse(formatTab.getAutoFlush());

        // Test with pipe
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());
        assertFalse(formatPipe.getIgnoreEmptyLines());
        assertFalse(formatPipe.getIgnoreSurroundingSpaces());
        assertFalse(formatPipe.isQuoteCharacterSet());
        assertFalse(formatPipe.isEscapeCharacterSet());
        assertNull(formatPipe.getRecordSeparator());
        assertFalse(formatPipe.getSkipHeaderRecord());
        assertFalse(formatPipe.getTrailingDelimiter());
        assertFalse(formatPipe.getAllowMissingColumnNames());
        assertFalse(formatPipe.getIgnoreHeaderCase());
        assertFalse(formatPipe.getTrim());
        assertFalse(formatPipe.getAutoFlush());

        // Test with special char
        CSVFormat formatHash = CSVFormat.newFormat('#');
        assertNotNull(formatHash);
        assertEquals('#', formatHash.getDelimiter());
        assertFalse(formatHash.getIgnoreEmptyLines());
        assertFalse(formatHash.getIgnoreSurroundingSpaces());
        assertFalse(formatHash.isQuoteCharacterSet());
        assertFalse(formatHash.isEscapeCharacterSet());
        assertNull(formatHash.getRecordSeparator());
        assertFalse(formatHash.getSkipHeaderRecord());
        assertFalse(formatHash.getTrailingDelimiter());
        assertFalse(formatHash.getAllowMissingColumnNames());
        assertFalse(formatHash.getIgnoreHeaderCase());
        assertFalse(formatHash.getTrim());
        assertFalse(formatHash.getAutoFlush());
    }
}