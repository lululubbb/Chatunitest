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

class CSVFormat_43_1Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesNoArg() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = baseFormat.withAllowMissingColumnNames();

        // Verify that a new CSVFormat instance is returned (immutability)
        assertNotSame(baseFormat, newFormat);

        // Verify that allowMissingColumnNames is set to true
        assertTrue(newFormat.getAllowMissingColumnNames());

        // Verify that other properties remain unchanged
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());

        // header and headerComments can be null, so handle null safely
        assertArrayEquals(baseFormat.getHeader() == null ? new String[0] : baseFormat.getHeader(),
                newFormat.getHeader() == null ? new String[0] : newFormat.getHeader());
        assertArrayEquals(baseFormat.getHeaderComments() == null ? new String[0] : baseFormat.getHeaderComments(),
                newFormat.getHeaderComments() == null ? new String[0] : newFormat.getHeaderComments());

        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getTrailingDelimiter(), newFormat.getTrailingDelimiter());
        assertEquals(baseFormat.getTrim(), newFormat.getTrim());
    }
}