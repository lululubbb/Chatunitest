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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class CSVFormat_51_2Test {

    @Test
    @Timeout(8000)
    void testWithEscapeValidCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = Constants.BACKSLASH;
        CSVFormat result = format.withEscape(escapeChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), result.getQuoteMode());
        assertEquals(format.getCommentMarker(), result.getCommentMarker());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertArrayEquals(format.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(format.getTrim(), result.getTrim());
        assertEquals(format.getTrailingDelimiter(), result.getTrailingDelimiter());
        assertEquals(format.getAutoFlush(), result.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeNullCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to invoke withEscape(Character) with null
        Method method = CSVFormat.class.getMethod("withEscape", Character.class);
        CSVFormat result = (CSVFormat) method.invoke(format, (Character) null);
        assertNotNull(result);
        assertNull(result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeLineBreakCharacterCR() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = Constants.CR;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            format.withEscape(escapeChar);
        });
        assertEquals("The escape character cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeLineBreakCharacterLF() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = Constants.LF;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            format.withEscape(escapeChar);
        });
        assertEquals("The escape character cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeLineBreakCharacterCRLF() {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use the first char of CRLF which is CR, already tested above, but keep for coverage
        char escapeChar = Constants.CRLF.charAt(0);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            format.withEscape(escapeChar);
        });
        assertEquals("The escape character cannot be a line break", exception.getMessage());
    }
}