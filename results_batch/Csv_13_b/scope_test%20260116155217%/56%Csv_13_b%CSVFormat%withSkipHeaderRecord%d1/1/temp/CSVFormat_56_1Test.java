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
import java.lang.reflect.Method;

public class CSVFormat_56_1Test {

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withSkipHeaderRecord(true);
        assertNotNull(modified);

        // Use reflection to call getSkipHeaderRecord()
        Method getSkipHeaderRecordMethod = CSVFormat.class.getMethod("getSkipHeaderRecord");
        boolean modifiedSkip = (boolean) getSkipHeaderRecordMethod.invoke(modified);
        boolean originalSkip = (boolean) getSkipHeaderRecordMethod.invoke(original);

        assertTrue(modifiedSkip);
        // The original should remain unchanged
        assertFalse(originalSkip);

        // All other fields should be equal
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeaderComments(), modified.getHeaderComments());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordFalse() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        Method getSkipHeaderRecordMethod = CSVFormat.class.getMethod("getSkipHeaderRecord");
        boolean originalSkip = (boolean) getSkipHeaderRecordMethod.invoke(original);
        assertTrue(originalSkip);

        CSVFormat modified = original.withSkipHeaderRecord(false);
        boolean modifiedSkip = (boolean) getSkipHeaderRecordMethod.invoke(modified);
        assertFalse(modifiedSkip);

        // The original should remain unchanged
        originalSkip = (boolean) getSkipHeaderRecordMethod.invoke(original);
        assertTrue(originalSkip);

        // All other fields should be equal
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeaderComments(), modified.getHeaderComments());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
    }
}