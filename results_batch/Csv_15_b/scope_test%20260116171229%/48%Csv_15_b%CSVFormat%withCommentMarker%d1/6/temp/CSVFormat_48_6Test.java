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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_48_6Test {

    private CSVFormat defaultFormat;

    @BeforeEach
    public void setUp() {
        defaultFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_ValidCharacter() {
        char commentChar = '#';
        CSVFormat newFormat = defaultFormat.withCommentMarker(commentChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentMarker());
        // Other properties remain unchanged
        assertEquals(defaultFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(defaultFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(defaultFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(defaultFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(defaultFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(defaultFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(defaultFormat.getNullString(), newFormat.getNullString());
        assertEquals(defaultFormat.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(defaultFormat.getHeader(), newFormat.getHeader());
        assertEquals(defaultFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(defaultFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(defaultFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(defaultFormat.getTrim(), newFormat.getTrim());
        assertEquals(defaultFormat.getTrailingDelimiter(), newFormat.getTrailingDelimiter());
        assertEquals(defaultFormat.getAutoFlush(), newFormat.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_NullCharacter() {
        CSVFormat newFormat = defaultFormat.withCommentMarker((Character) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_LineBreakCharacters() throws Exception {
        // Use reflection to access private static method isLineBreak(char)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Verify that isLineBreak returns true for line break characters
        assertTrue((Boolean) isLineBreakMethod.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakMethod.invoke(null, '\r'));

        // Test that withCommentMarker throws IllegalArgumentException for line break chars
        char[] lineBreakChars = new char[] { '\n', '\r' };
        for (char c : lineBreakChars) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                defaultFormat.withCommentMarker(Character.valueOf(c));
            });
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }
}