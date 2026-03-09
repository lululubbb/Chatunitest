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

class CSVFormatWithNullStringTest {

    @Test
    @Timeout(8000)
    void testWithNullString() {
        CSVFormat original = CSVFormat.DEFAULT;

        // Test with a non-null string
        String nullStr = "NULL";
        CSVFormat modified = original.withNullString(nullStr);
        assertNotSame(original, modified);
        assertEquals(nullStr, modified.getNullString());
        // Original should remain unchanged
        assertNull(original.getNullString());

        // Test with null string set to null explicitly
        CSVFormat modifiedNull = original.withNullString(null);
        assertNotSame(original, modifiedNull);
        assertNull(modifiedNull.getNullString());
        assertNull(original.getNullString());

        // Test chaining withNullString multiple times
        CSVFormat chain1 = original.withNullString("A");
        CSVFormat chain2 = chain1.withNullString("B");
        assertEquals("A", chain1.getNullString());
        assertEquals("B", chain2.getNullString());

        // Test that other properties are preserved
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertArrayEquals(original.getHeaderComments(), modified.getHeaderComments());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
    }
}