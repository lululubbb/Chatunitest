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

class CSVFormat_34_6Test {

    @Test
    @Timeout(8000)
    void withDelimiter_validDelimiter_shouldReturnNewCSVFormatWithGivenDelimiter() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        char newDelimiter = ';';
        CSVFormat updated = original.withDelimiter(newDelimiter);

        assertNotNull(updated);
        assertEquals(newDelimiter, updated.getDelimiter());
        // Ensure other properties remain unchanged
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeaderComments(), updated.getHeaderComments());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void withDelimiter_lineBreakDelimiter_shouldThrowIllegalArgumentException() {
        CSVFormat original = CSVFormat.DEFAULT;

        char[] lineBreaks = {'\n', '\r'};

        for (char lineBreak : lineBreaks) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                original.withDelimiter(lineBreak);
            });
            assertEquals("The delimiter cannot be a line break", exception.getMessage());
        }
    }
}