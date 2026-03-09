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

class CSVFormat_47_5Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreHeaderCase() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreHeaderCase();

        assertNotNull(modified, "The returned CSVFormat should not be null");
        assertTrue(modified.getIgnoreHeaderCase(), "The ignoreHeaderCase flag should be true");
        assertEquals(original.getDelimiter(), modified.getDelimiter(), "Delimiter should remain unchanged");
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter(), "Quote character should remain unchanged");
        assertEquals(original.getQuoteMode(), modified.getQuoteMode(), "Quote mode should remain unchanged");
        assertEquals(original.getCommentMarker(), modified.getCommentMarker(), "Comment marker should remain unchanged");
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter(), "Escape character should remain unchanged");
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces(), "Ignore surrounding spaces flag should remain unchanged");
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames(), "Allow missing column names flag should remain unchanged");
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines(), "Ignore empty lines flag should remain unchanged");
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator(), "Record separator should remain unchanged");
        assertEquals(original.getNullString(), modified.getNullString(), "Null string should remain unchanged");

        // Defensive null checks for header and headerComments before assertArrayEquals
        if (original.getHeader() == null && modified.getHeader() == null) {
            // both null, OK
        } else {
            assertArrayEquals(original.getHeader(), modified.getHeader(), "Header array should remain unchanged");
        }

        if (original.getHeaderComments() == null && modified.getHeaderComments() == null) {
            // both null, OK
        } else {
            assertArrayEquals(original.getHeaderComments(), modified.getHeaderComments(), "Header comments should remain unchanged");
        }

        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord(), "Skip header record flag should remain unchanged");
    }
}