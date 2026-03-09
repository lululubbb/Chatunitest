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

public class CSVFormat_48_4Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarkerValidChar() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use a valid comment marker (not a line break)
        Character commentMarker = '#';

        CSVFormat newFormat = format.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertEquals(commentMarker, newFormat.getCommentMarker());

        // Ensure other properties remain the same
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
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
        assertEquals(format.getAutoFlush(), newFormat.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarkerNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Setting comment marker to null should be allowed and set commentMarker to null
        CSVFormat newFormat = format.withCommentMarker((Character) null);

        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarkerLineBreakCharThrows() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to access private static method isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test with line break characters that should cause exception
        char[] lineBreakChars = new char[]{'\n', '\r'};

        for (char c : lineBreakChars) {
            // Confirm isLineBreak returns true for this char wrapped as Character
            Boolean result = (Boolean) isLineBreakMethod.invoke(null, Character.valueOf(c));
            assertTrue(result, "Expected isLineBreak to return true for char: " + Integer.toHexString(c));

            // Expect IllegalArgumentException when passing this char to withCommentMarker
            final Character charObj = Character.valueOf(c);
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withCommentMarker(charObj);
            });
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarkerLineBreakCharacterObjectThrows() {
        CSVFormat format = CSVFormat.DEFAULT;

        Character lineBreakChar = Character.valueOf('\n');

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            format.withCommentMarker(lineBreakChar);
        });

        assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
    }
}