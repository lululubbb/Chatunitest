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

class CSVFormat_48_4Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_validCharacter() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char commentMarker = '#';

        CSVFormat newFormat = baseFormat.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentMarker), newFormat.getCommentMarker());
        // The original instance should remain unchanged
        assertNull(baseFormat.getCommentMarker());
        // Other properties should be equal
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
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
    void testWithCommentMarker_nullCharacter() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        CSVFormat newFormat = baseFormat.withCommentMarker((Character) null);

        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
        // The original instance should remain unchanged
        assertNull(baseFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakCharacter_throwsException() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        char[] lineBreaks = {'\n', '\r'};

        for (char lb : lineBreaks) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
                baseFormat.withCommentMarker(lb);
            });
            assertEquals("The comment start marker character cannot be a line break", ex.getMessage());
        }
    }
}