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

class CSVFormat_3_1Test {

    @Test
    @Timeout(8000)
    void testNewFormatWithVariousDelimiters() {
        // Test with comma
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertNotNull(formatComma);
        assertEquals(',', formatComma.getDelimiter());
        assertFalse(formatComma.getIgnoreEmptyLines());
        assertFalse(formatComma.getIgnoreSurroundingSpaces());
        assertFalse(formatComma.isQuoteCharacterSet());
        assertFalse(formatComma.getAllowMissingColumnNames());

        // Test with tab
        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertNotNull(formatTab);
        assertEquals('\t', formatTab.getDelimiter());
        assertFalse(formatTab.getIgnoreEmptyLines());
        assertFalse(formatTab.getIgnoreSurroundingSpaces());
        assertFalse(formatTab.isQuoteCharacterSet());
        assertFalse(formatTab.getAllowMissingColumnNames());

        // Test with pipe
        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertNotNull(formatPipe);
        assertEquals('|', formatPipe.getDelimiter());
        assertFalse(formatPipe.getIgnoreEmptyLines());
        assertFalse(formatPipe.getIgnoreSurroundingSpaces());
        assertFalse(formatPipe.isQuoteCharacterSet());
        assertFalse(formatPipe.getAllowMissingColumnNames());

        // Test with special char, e.g. backslash
        CSVFormat formatBackslash = CSVFormat.newFormat('\\');
        assertNotNull(formatBackslash);
        assertEquals('\\', formatBackslash.getDelimiter());
        assertFalse(formatBackslash.getIgnoreEmptyLines());
        assertFalse(formatBackslash.getIgnoreSurroundingSpaces());
        assertFalse(formatBackslash.isQuoteCharacterSet());
        assertFalse(formatBackslash.getAllowMissingColumnNames());
    }
}