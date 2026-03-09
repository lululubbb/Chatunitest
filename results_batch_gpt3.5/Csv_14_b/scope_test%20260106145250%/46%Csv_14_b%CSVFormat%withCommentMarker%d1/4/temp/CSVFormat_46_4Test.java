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
import org.junit.jupiter.api.Test;

class CSVFormat_46_4Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarkerValidChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char commentMarker = '#';

        CSVFormat result = baseFormat.withCommentMarker(commentMarker);

        assertNotNull(result);
        assertEquals(Character.valueOf(commentMarker), result.getCommentMarker());
        assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(baseFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), result.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(baseFormat.getHeader(), result.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(baseFormat.getTrim(), result.getTrim());
        assertEquals(baseFormat.getTrailingDelimiter(), result.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarkerNull() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Character commentMarker = null;

        CSVFormat result = baseFormat.withCommentMarker(commentMarker);

        assertNotNull(result);
        assertNull(result.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarkerLineBreakThrows() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        char[] lineBreaks = {'\n', '\r'};

        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                baseFormat.withCommentMarker(lb);
            });
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }
}