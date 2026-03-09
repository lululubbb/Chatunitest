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

class CSVFormatWithDelimiterTest {

    @Test
    @Timeout(8000)
    void withDelimiter_validDelimiter_shouldReturnNewCSVFormatWithDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT;
        char newDelimiter = ';';

        CSVFormat newFormat = format.withDelimiter(newDelimiter);

        assertNotNull(newFormat);
        assertEquals(newDelimiter, newFormat.getDelimiter());
        // Other properties should be preserved
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(format.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(format.getNullString(), newFormat.getNullString());
        assertArrayEquals(format.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(format.getHeader(), newFormat.getHeader());
        assertEquals(format.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void withDelimiter_lineBreakDelimiterChar_shouldThrowIllegalArgumentException() {
        CSVFormat format = CSVFormat.DEFAULT;

        char[] lineBreakChars = {'\n', '\r'};

        for (char c : lineBreakChars) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                format.withDelimiter(c);
            });
            assertEquals("The delimiter cannot be a line break", exception.getMessage());
        }
    }
}