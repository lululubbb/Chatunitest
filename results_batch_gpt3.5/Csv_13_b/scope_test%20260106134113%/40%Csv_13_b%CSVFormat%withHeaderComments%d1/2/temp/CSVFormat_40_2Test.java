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

class CSVFormatWithHeaderCommentsTest {

    @Test
    @Timeout(8000)
    void testWithHeaderCommentsCreatesNewInstanceWithGivenComments() {
        CSVFormat original = CSVFormat.DEFAULT;
        Object[] headerComments = new Object[]{"comment1", "comment2", 123};

        CSVFormat result = original.withHeaderComments(headerComments);

        assertNotNull(result);
        assertNotSame(original, result);
        assertArrayEquals(new String[]{"comment1", "comment2", "123"}, result.getHeaderComments());
        // Other properties should remain the same
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderCommentsWithNullInput() {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat result = original.withHeaderComments((Object[]) null);

        assertNotNull(result);
        assertNotSame(original, result);
        assertNull(result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderCommentsWithEmptyInput() {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat result = original.withHeaderComments();

        assertNotNull(result);
        assertNotSame(original, result);
        assertArrayEquals(new String[0], result.getHeaderComments());
    }
}