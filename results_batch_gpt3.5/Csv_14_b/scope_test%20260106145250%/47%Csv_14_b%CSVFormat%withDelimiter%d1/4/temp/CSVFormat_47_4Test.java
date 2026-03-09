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

class CSVFormat_47_4Test {

    @Test
    @Timeout(8000)
    void testWithDelimiterValid() {
        CSVFormat base = CSVFormat.DEFAULT;

        char newDelimiter = ';';
        CSVFormat result = base.withDelimiter(newDelimiter);

        assertNotNull(result);
        assertEquals(newDelimiter, result.getDelimiter());

        // Other properties remain unchanged
        assertEquals(base.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), result.getQuoteMode());
        assertEquals(base.getCommentMarker(), result.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(base.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(base.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(base.getNullString(), result.getNullString());
        assertArrayEquals(base.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(base.getHeader(), result.getHeader());
        assertEquals(base.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(base.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), result.getTrim());
        assertEquals(base.getTrailingDelimiter(), result.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiterLineBreakCharCR() {
        CSVFormat base = CSVFormat.DEFAULT;
        char lineBreak = '\r';

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> base.withDelimiter(lineBreak));
        assertEquals("The delimiter cannot be a line break", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiterLineBreakCharLF() {
        CSVFormat base = CSVFormat.DEFAULT;
        char lineBreak = '\n';

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> base.withDelimiter(lineBreak));
        assertEquals("The delimiter cannot be a line break", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiterLineBreakCharUnicode() throws Exception {
        // Using reflection to invoke private static isLineBreak(char)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        char[] lineBreaks = {'\r', '\n'};
        for (char lb : lineBreaks) {
            boolean result = (boolean) isLineBreakMethod.invoke(null, lb);
            assertTrue(result);
        }

        char nonLineBreak = 'a';
        boolean result = (boolean) isLineBreakMethod.invoke(null, nonLineBreak);
        assertFalse(result);
    }
}