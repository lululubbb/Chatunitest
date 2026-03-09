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

public class CSVFormat_48_2Test {

    private CSVFormat defaultFormat;

    @BeforeEach
    public void setUp() {
        defaultFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_validChar() {
        char commentMarker = '#';
        CSVFormat newFormat = defaultFormat.withCommentMarker(commentMarker);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentMarker), newFormat.getCommentMarker());
        // Ensure other properties are unchanged
        assertEquals(defaultFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(defaultFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(defaultFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(defaultFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(defaultFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(defaultFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(defaultFormat.getNullString(), newFormat.getNullString());
        assertArrayEquals(defaultFormat.getHeaderComments(), newFormat.getHeaderComments());
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
    public void testWithCommentMarker_null() {
        CSVFormat newFormat = defaultFormat.withCommentMarker((Character) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_lineBreakCharThrows() throws Exception {
        // Use reflection to access private static isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test with line break characters that should cause exception
        char[] lineBreaks = {'\n', '\r'};
        for (char lb : lineBreaks) {
            // Confirm isLineBreak returns true for these characters
            Boolean isLineBreak = (Boolean) isLineBreakMethod.invoke(null, Character.valueOf(lb));
            assertTrue(isLineBreak);

            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                defaultFormat.withCommentMarker(lb);
            });
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }

}