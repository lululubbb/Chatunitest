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

class CSVFormat_48_3Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_validCharacter_setsCommentMarker() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat formatWithComment = baseFormat.withCommentMarker(commentChar);

        assertNotNull(formatWithComment);
        assertEquals(Character.valueOf(commentChar), formatWithComment.getCommentMarker());
        // All other properties should remain the same as baseFormat except commentMarker
        assertEquals(baseFormat.getDelimiter(), formatWithComment.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), formatWithComment.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), formatWithComment.getQuoteMode());
        assertEquals(baseFormat.getEscapeCharacter(), formatWithComment.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), formatWithComment.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), formatWithComment.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), formatWithComment.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), formatWithComment.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), formatWithComment.getHeaderComments());
        assertArrayEquals(baseFormat.getHeader(), formatWithComment.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), formatWithComment.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), formatWithComment.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), formatWithComment.getIgnoreHeaderCase());
        assertEquals(baseFormat.getTrim(), formatWithComment.getTrim());
        assertEquals(baseFormat.getTrailingDelimiter(), formatWithComment.getTrailingDelimiter());
        assertEquals(baseFormat.getAutoFlush(), formatWithComment.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_nullCharacter_setsCommentMarkerNull() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        CSVFormat formatWithNullComment = baseFormat.withCommentMarker((Character) null);

        assertNotNull(formatWithNullComment);
        assertNull(formatWithNullComment.getCommentMarker());
        // Other properties remain unchanged
        assertEquals(baseFormat.getDelimiter(), formatWithNullComment.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), formatWithNullComment.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), formatWithNullComment.getQuoteMode());
        assertEquals(baseFormat.getEscapeCharacter(), formatWithNullComment.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), formatWithNullComment.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), formatWithNullComment.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), formatWithNullComment.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), formatWithNullComment.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), formatWithNullComment.getHeaderComments());
        assertArrayEquals(baseFormat.getHeader(), formatWithNullComment.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), formatWithNullComment.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), formatWithNullComment.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), formatWithNullComment.getIgnoreHeaderCase());
        assertEquals(baseFormat.getTrim(), formatWithNullComment.getTrim());
        assertEquals(baseFormat.getTrailingDelimiter(), formatWithNullComment.getTrailingDelimiter());
        assertEquals(baseFormat.getAutoFlush(), formatWithNullComment.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakCharacter_throwsIllegalArgumentException() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        char[] lineBreakChars = {'\n', '\r'};
        for (char lbChar : lineBreakChars) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                    () -> baseFormat.withCommentMarker(Character.valueOf(lbChar)));
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }
}