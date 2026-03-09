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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_46_5Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpacesTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withIgnoreSurroundingSpaces(true);
        assertNotNull(newFormat);
        assertTrue(newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(format.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(format.getNullString(), newFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpacesFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        CSVFormat newFormat = format.withIgnoreSurroundingSpaces(false);
        assertNotNull(newFormat);
        assertFalse(newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(format.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(format.getNullString(), newFormat.getNullString());
    }
}