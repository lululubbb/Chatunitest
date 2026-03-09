package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_33_5Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_validCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentMarker = '#';

        CSVFormat newFormat = format.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentMarker), newFormat.getCommentMarker());
        // verify other fields remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(format.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_nullCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentMarker = null;

        CSVFormat newFormat = format.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
        // verify other fields remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(format.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakCharacterCR() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentMarker = '\r';

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> format.withCommentMarker(commentMarker));
        assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakCharacterLF() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentMarker = '\n';

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> format.withCommentMarker(commentMarker));
        assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakCharacterCRLF() {
        // CRLF is a String, the method accepts char, so test with '\r' and '\n' separately
        // This test is redundant with above tests but included for completeness
        CSVFormat format = CSVFormat.DEFAULT;

        char crlf = '\r'; // testing '\r' again
        IllegalArgumentException thrown1 = assertThrows(IllegalArgumentException.class,
                () -> format.withCommentMarker(crlf));
        assertEquals("The comment start marker character cannot be a line break", thrown1.getMessage());

        char lf = '\n';
        IllegalArgumentException thrown2 = assertThrows(IllegalArgumentException.class,
                () -> format.withCommentMarker(lf));
        assertEquals("The comment start marker character cannot be a line break", thrown2.getMessage());
    }
}