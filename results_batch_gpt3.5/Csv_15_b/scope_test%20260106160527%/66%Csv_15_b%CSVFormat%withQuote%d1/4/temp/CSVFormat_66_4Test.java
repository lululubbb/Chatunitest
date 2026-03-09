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

public class CSVFormat_66_4Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_ValidQuoteChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\'';
        CSVFormat result = original.withQuote(quoteChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
        // Ensure other properties remain unchanged
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), result.getTrim());
        assertEquals(original.getTrailingDelimiter(), result.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), result.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_NullQuoteChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withQuote((Character) null);
        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_LineBreakQuoteChar_ThrowsException() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Use reflection to invoke private static isLineBreak(Character) method
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test with '\n' line break character
        Character lf = '\n';
        assertTrue((Boolean) isLineBreakMethod.invoke(null, lf));
        IllegalArgumentException thrownLf = assertThrows(IllegalArgumentException.class, () -> {
            original.withQuote(lf);
        });
        assertEquals("The quoteChar cannot be a line break", thrownLf.getMessage());

        // Test with '\r' line break character
        Character cr = '\r';
        assertTrue((Boolean) isLineBreakMethod.invoke(null, cr));
        IllegalArgumentException thrownCr = assertThrows(IllegalArgumentException.class, () -> {
            original.withQuote(cr);
        });
        assertEquals("The quoteChar cannot be a line break", thrownCr.getMessage());
    }
}