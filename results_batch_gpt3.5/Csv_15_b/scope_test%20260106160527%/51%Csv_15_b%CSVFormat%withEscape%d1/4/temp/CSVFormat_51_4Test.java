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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_51_4Test {

    @Test
    @Timeout(8000)
    public void testWithEscapeValidCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escapeChar = '\\';

        // Use reflection to call withEscape(Character) because it expects Character, not char
        Method withEscapeMethod = CSVFormat.class.getMethod("withEscape", Character.class);
        CSVFormat result = (CSVFormat) withEscapeMethod.invoke(format, escapeChar);

        assertNotNull(result);
        assertEquals(escapeChar, result.getEscapeCharacter());
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
    public void testWithEscapeNullCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to call withEscape(null)
        Method withEscapeMethod = CSVFormat.class.getMethod("withEscape", Character.class);
        CSVFormat result = (CSVFormat) withEscapeMethod.invoke(format, new Object[] { null });

        assertNotNull(result);
        assertNull(result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeLineBreakCharacterCR() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escapeChar = '\r';

        Method withEscapeMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            withEscapeMethod.invoke(format, escapeChar);
        });
        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof IllegalArgumentException);
        assertEquals("The escape character cannot be a line break", cause.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeLineBreakCharacterLF() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escapeChar = '\n';

        Method withEscapeMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            withEscapeMethod.invoke(format, escapeChar);
        });
        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof IllegalArgumentException);
        assertEquals("The escape character cannot be a line break", cause.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeLineBreakCharacterCRLF() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // CRLF is two chars, test CR and LF separately
        Character escapeChar = '\r';

        Method withEscapeMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            withEscapeMethod.invoke(format, escapeChar);
        });
        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof IllegalArgumentException);
        assertEquals("The escape character cannot be a line break", cause.getMessage());
    }
}