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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

class CSVFormatWithCommentMarkerTest {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_validChar_returnsNewInstanceWithCommentMarker() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char commentMarker = '#';

        CSVFormat newFormat = baseFormat.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        assertEquals(Character.valueOf(commentMarker), newFormat.getCommentMarker());

        // All other fields should be equal to the baseFormat's fields
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
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakChar_throwsIllegalArgumentException() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        char[] lineBreaks = {'\n', '\r'};

        for (char lb : lineBreaks) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> baseFormat.withCommentMarker(lb));
            assertEquals("The comment start marker character cannot be a line break", exception.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_nullCommentMarker_returnsNewInstanceWithNullCommentMarker() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        CSVFormat newFormat = baseFormat.withCommentMarker((Character) null);

        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        assertNull(newFormat.getCommentMarker());

        // Other fields unchanged
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
    }
}