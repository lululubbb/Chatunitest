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

import java.lang.reflect.Method;

class CSVFormat_47_5Test {

    @Test
    @Timeout(8000)
    void testWithDelimiter_validDelimiter() {
        char delimiter = ';';
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withDelimiter(delimiter);

        assertNotNull(updated);
        assertEquals(delimiter, updated.getDelimiter());
        // Other fields remain same as original except delimiter
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeaderComments(), updated.getHeaderComments());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), updated.getTrim());
        assertEquals(original.getTrailingDelimiter(), updated.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_lineBreakDelimiterCR() {
        char delimiter = '\r'; // CR line break
        CSVFormat original = CSVFormat.DEFAULT;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> original.withDelimiter(delimiter));
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_lineBreakDelimiterLF() {
        char delimiter = '\n'; // LF line break
        CSVFormat original = CSVFormat.DEFAULT;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> original.withDelimiter(delimiter));
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_lineBreakDelimiterCRLF() throws Exception {
        // CRLF is a String line break, delimiter is char, so test with '\r' or '\n' only
        // But to cover private static isLineBreak(char), we use reflection to test it
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);
        assertTrue((Boolean) method.invoke(null, '\r'));
        assertTrue((Boolean) method.invoke(null, '\n'));
        assertFalse((Boolean) method.invoke(null, ';'));
        assertFalse((Boolean) method.invoke(null, 'a'));
    }
}