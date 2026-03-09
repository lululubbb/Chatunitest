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

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_48_5Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_validChar() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentMarker = '#';

        CSVFormat result = format.withCommentMarker(commentMarker);

        assertNotNull(result);
        assertEquals(Character.valueOf(commentMarker), result.getCommentMarker());
        // original instance remains unchanged
        assertNull(format.getCommentMarker());
        // other properties remain the same
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), result.getQuoteMode());
        assertEquals(format.getEscapeCharacter(), result.getEscapeCharacter());
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
    public void testWithCommentMarker_null() {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat result = format.withCommentMarker((Character) null);

        assertNotNull(result);
        assertNull(result.getCommentMarker());
        // original instance remains unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_lineBreakChar_throws() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to access private static method isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test with line break characters that should throw
        Character[] lineBreakChars = { '\n', '\r' };

        for (Character lbChar : lineBreakChars) {
            // Confirm isLineBreak returns true for these characters
            Boolean isLineBreak = (Boolean) isLineBreakMethod.invoke(null, lbChar);
            assertTrue(isLineBreak);

            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withCommentMarker(lbChar.charValue());
            });
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }
}