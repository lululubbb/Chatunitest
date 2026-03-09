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

public class CSVFormat_54_3Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_NewSeparator() {
        CSVFormat original = CSVFormat.DEFAULT;
        String newSeparator = "\n";
        CSVFormat modified = original.withRecordSeparator(newSeparator);

        assertNotSame(original, modified, "withRecordSeparator should return a new instance");
        assertEquals(newSeparator, modified.getRecordSeparator(), "Record separator should be updated");
        assertEquals(original.getDelimiter(), modified.getDelimiter(), "Delimiter should remain unchanged");
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter(), "Quote character should remain unchanged");
        assertEquals(original.getQuoteMode(), modified.getQuoteMode(), "Quote mode should remain unchanged");
        assertEquals(original.getCommentMarker(), modified.getCommentMarker(), "Comment marker should remain unchanged");
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter(), "Escape character should remain unchanged");
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces(), "Ignore surrounding spaces should remain unchanged");
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines(), "Ignore empty lines should remain unchanged");
        assertEquals(original.getNullString(), modified.getNullString(), "Null string should remain unchanged");
        assertArrayEquals(original.getHeaderComments(), modified.getHeaderComments(), "Header comments should remain unchanged");
        assertArrayEquals(original.getHeader(), modified.getHeader(), "Header should remain unchanged");
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord(), "Skip header record should remain unchanged");
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames(), "Allow missing column names should remain unchanged");
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase(), "Ignore header case should remain unchanged");
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_NullSeparator() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withRecordSeparator((String) null);

        assertNotSame(original, modified, "withRecordSeparator should return a new instance even if null");
        assertNull(modified.getRecordSeparator(), "Record separator should be null");
        assertEquals(original.getDelimiter(), modified.getDelimiter(), "Delimiter should remain unchanged");
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_EmptySeparator() {
        CSVFormat original = CSVFormat.DEFAULT;
        String emptySeparator = "";
        CSVFormat modified = original.withRecordSeparator(emptySeparator);

        assertNotSame(original, modified, "withRecordSeparator should return a new instance");
        assertEquals(emptySeparator, modified.getRecordSeparator(), "Record separator should be empty string");
    }
}