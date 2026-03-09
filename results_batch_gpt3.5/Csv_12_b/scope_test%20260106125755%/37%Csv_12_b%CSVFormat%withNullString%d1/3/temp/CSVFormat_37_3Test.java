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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_37_3Test {

    @Test
    @Timeout(8000)
    void testWithNullString_NewInstanceHasCorrectNullString() {
        CSVFormat original = CSVFormat.DEFAULT;
        String nullStr = "NULL";
        CSVFormat modified = original.withNullString(nullStr);

        // Verify original is unchanged
        assertNull(original.getNullString());

        // Verify modified instance has the new nullString
        assertEquals(nullStr, modified.getNullString());

        // Verify other fields remain same
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithNullString_NullValue() {
        CSVFormat original = CSVFormat.DEFAULT.withNullString("abc");
        CSVFormat modified = original.withNullString(null);

        // original has non-null nullString
        assertEquals("abc", original.getNullString());

        // modified has null nullString
        assertNull(modified.getNullString());
    }

    @Test
    @Timeout(8000)
    void testWithNullString_SameValueReturnsDifferentInstance() {
        CSVFormat original = CSVFormat.DEFAULT.withNullString("same");
        CSVFormat modified = original.withNullString("same");

        // Should be different instances
        assertNotSame(original, modified);

        // But equal in fields
        assertEquals(original.getNullString(), modified.getNullString());
        assertEquals(original.getDelimiter(), modified.getDelimiter());
    }
}