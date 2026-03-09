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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

class CSVFormatWithEscapeTest {

    @Test
    @Timeout(8000)
    void testWithEscape_validEscapeCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat newFormat = format.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscapeCharacter());
        // Other properties remain the same
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(format.getTrim(), newFormat.getTrim());
        assertEquals(format.getTrailingDelimiter(), newFormat.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_nullEscapeCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat newFormat = format.withEscape((Character) null);

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertNull(newFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_lineBreakEscapeCharacter_CR() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\r';

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            format.withEscape(escapeChar);
        });
        assertEquals("The escape character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_lineBreakEscapeCharacter_LF() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\n';

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            format.withEscape(escapeChar);
        });
        assertEquals("The escape character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_lineBreakEscapeCharacter_CRLF() {
        // CRLF is a String, so not a Character, but testing with '\r' or '\n' covers line breaks
        // No direct Character for CRLF, so skip this case
    }
}