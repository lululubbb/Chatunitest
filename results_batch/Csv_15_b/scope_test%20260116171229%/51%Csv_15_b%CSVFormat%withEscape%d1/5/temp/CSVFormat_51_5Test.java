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

public class CSVFormat_51_5Test {

    @Test
    @Timeout(8000)
    public void testWithEscapeValidCharacter() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat newFormat = baseFormat.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscapeCharacter());
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
        assertEquals(baseFormat.getAutoFlush(), newFormat.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeNullCharacter() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        CSVFormat newFormat = baseFormat.withEscape((Character) null);

        assertNotNull(newFormat);
        assertNull(newFormat.getEscapeCharacter());
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeThrowsOnLineBreakChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        char[] lineBreaks = { '\n', '\r' };

        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                baseFormat.withEscape(lb);
            });
            assertEquals("The escape character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeThrowsOnLineBreakCharacterObject() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        Character[] lineBreaks = { '\n', '\r' };

        for (Character lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                baseFormat.withEscape(lb);
            });
            assertEquals("The escape character cannot be a line break", thrown.getMessage());
        }
    }
}