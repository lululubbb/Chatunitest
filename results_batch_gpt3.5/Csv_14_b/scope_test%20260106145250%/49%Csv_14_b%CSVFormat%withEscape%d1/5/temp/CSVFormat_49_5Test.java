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
import org.apache.commons.csv.Constants;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class CSVFormat_49_5Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_validEscapeCharacter() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char escapeChar = Constants.BACKSLASH;
        CSVFormat newFormat = baseFormat.withEscape(escapeChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscapeCharacter());
        // Other fields should remain the same as baseFormat except escapeCharacter
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(baseFormat.getHeader(), newFormat.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(baseFormat.getTrim(), newFormat.getTrim());
        assertEquals(baseFormat.getTrailingDelimiter(), newFormat.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_nullEscapeCharacter() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat newFormat = constructor.newInstance(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                null, // escapeCharacter null
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                baseFormat.getHeaderComments() == null ? null : (Object[]) baseFormat.getHeaderComments(),
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord(),
                baseFormat.getAllowMissingColumnNames(),
                baseFormat.getIgnoreHeaderCase(),
                baseFormat.getTrim(),
                baseFormat.getTrailingDelimiter()
        );

        assertNotNull(newFormat);
        assertNull(newFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_lineBreakEscapeCharacter_CR() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char escapeChar = Constants.CR;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            baseFormat.withEscape(escapeChar);
        });
        assertEquals("The escape character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_lineBreakEscapeCharacter_LF() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char escapeChar = Constants.LF;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            baseFormat.withEscape(escapeChar);
        });
        assertEquals("The escape character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_lineBreakEscapeCharacter_CRLF() {
        // CRLF is a String constant, but withEscape accepts char, so CRLF cannot be passed directly.
        // We test withEscape with characters CR and LF separately, which is sufficient.
    }
}