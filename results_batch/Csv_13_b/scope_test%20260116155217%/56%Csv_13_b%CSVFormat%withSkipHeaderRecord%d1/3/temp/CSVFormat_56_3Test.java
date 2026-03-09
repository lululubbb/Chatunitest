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

class CSVFormat_56_3Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withSkipHeaderRecord(true);

        assertNotSame(format, result);
        assertTrue(result.getSkipHeaderRecord());
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), result.getQuoteMode());
        assertEquals(format.getCommentMarker(), result.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertArrayEquals(
            format.getHeaderComments() == null ? new String[0] : format.getHeaderComments(),
            result.getHeaderComments() == null ? new String[0] : result.getHeaderComments());
        assertArrayEquals(
            format.getHeader() == null ? new String[0] : format.getHeader(),
            result.getHeader() == null ? new String[0] : result.getHeader());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat result = format.withSkipHeaderRecord(false);

        assertNotSame(format, result);
        assertFalse(result.getSkipHeaderRecord());
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), result.getQuoteMode());
        assertEquals(format.getCommentMarker(), result.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertArrayEquals(
            format.getHeaderComments() == null ? new String[0] : format.getHeaderComments(),
            result.getHeaderComments() == null ? new String[0] : result.getHeaderComments());
        assertArrayEquals(
            format.getHeader() == null ? new String[0] : format.getHeader(),
            result.getHeader() == null ? new String[0] : result.getHeader());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }
}