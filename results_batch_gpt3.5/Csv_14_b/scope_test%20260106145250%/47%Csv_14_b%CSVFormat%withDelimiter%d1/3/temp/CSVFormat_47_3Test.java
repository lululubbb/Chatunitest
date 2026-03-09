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

import java.lang.reflect.Method;

class CSVFormat_47_3Test {

    @Test
    @Timeout(8000)
    void testWithDelimiterValid() {
        char delimiter = ';';
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat newFormat = original.withDelimiter(delimiter);

        assertNotNull(newFormat);
        assertEquals(delimiter, newFormat.getDelimiter());
        // Other properties should remain the same as original except delimiter
        assertEquals(original.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(original.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(original.getNullString(), newFormat.getNullString());
        assertArrayEquals(original.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(original.getHeader(), newFormat.getHeader());
        assertEquals(original.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), newFormat.getTrim());
        assertEquals(original.getTrailingDelimiter(), newFormat.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiterLineBreakCR() {
        char delimiter = '\r';
        CSVFormat original = CSVFormat.DEFAULT;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            original.withDelimiter(delimiter);
        });
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiterLineBreakLF() {
        char delimiter = '\n';
        CSVFormat original = CSVFormat.DEFAULT;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            original.withDelimiter(delimiter);
        });
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiterLineBreakCRLF() throws Exception {
        // Use reflection to test private static isLineBreak(char)
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(null, '\r'));
        assertTrue((Boolean) method.invoke(null, '\n'));
        assertFalse((Boolean) method.invoke(null, 'a'));
        assertFalse((Boolean) method.invoke(null, ','));
    }
}