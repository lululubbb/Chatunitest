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

public class CSVFormat_46_6Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_validChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat result = original.withCommentMarker(commentChar);

        assertNotNull(result);
        assertNotSame(original, result);
        assertEquals(Character.valueOf(commentChar), result.getCommentMarker());
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
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
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_null() {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat result = original.withCommentMarker((Character) null);

        assertNotNull(result);
        assertNotSame(original, result);
        assertNull(result.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_lineBreakChar_throwsException() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Access private static method isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Verify that '\n' is a line break
        assertTrue((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('\r')));

        // Test with '\n' character - expect IllegalArgumentException
        char lineBreakChar = '\n';
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            original.withCommentMarker(lineBreakChar);
        });
        assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());

        // Test with '\r' character - expect IllegalArgumentException
        char carriageReturnChar = '\r';
        IllegalArgumentException thrown2 = assertThrows(IllegalArgumentException.class, () -> {
            original.withCommentMarker(carriageReturnChar);
        });
        assertEquals("The comment start marker character cannot be a line break", thrown2.getMessage());
    }
}