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

class CSVFormatWithIgnoreSurroundingSpacesTest {

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpacesTrue() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat result = base.withIgnoreSurroundingSpaces(true);
        assertNotNull(result);
        assertTrue(result.getIgnoreSurroundingSpaces());
        assertEquals(base.getDelimiter(), result.getDelimiter());
        assertEquals(base.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), result.getQuoteMode());
        assertEquals(base.getCommentMarker(), result.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(base.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(base.getNullString(), result.getNullString());
        assertArrayEquals(base.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(base.getHeader(), result.getHeader());
        assertEquals(base.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(base.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpacesFalse() {
        CSVFormat base = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        CSVFormat result = base.withIgnoreSurroundingSpaces(false);
        assertNotNull(result);
        assertFalse(result.getIgnoreSurroundingSpaces());
        assertEquals(base.getDelimiter(), result.getDelimiter());
        assertEquals(base.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), result.getQuoteMode());
        assertEquals(base.getCommentMarker(), result.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(base.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(base.getNullString(), result.getNullString());
        assertArrayEquals(base.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(base.getHeader(), result.getHeader());
        assertEquals(base.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(base.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }
}