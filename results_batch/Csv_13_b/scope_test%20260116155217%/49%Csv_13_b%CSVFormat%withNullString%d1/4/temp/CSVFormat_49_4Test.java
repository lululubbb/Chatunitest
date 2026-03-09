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
    void testWithNullString_NewInstanceHasCorrectNullStringAndOtherFieldsCopied() {
        CSVFormat original = CSVFormat.DEFAULT;
        String nullStr = "NULL";

        CSVFormat updated = original.withNullString(nullStr);

        assertNotSame(original, updated, "withNullString should return a new CSVFormat instance");
        assertEquals(nullStr, updated.getNullString(), "Null string should be updated");
        // Other fields should remain unchanged
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());

        String[] originalHeaderComments = original.getHeaderComments();
        String[] updatedHeaderComments = updated.getHeaderComments();
        if (originalHeaderComments == null) {
            assertNull(updatedHeaderComments);
        } else {
            assertArrayEquals(originalHeaderComments, updatedHeaderComments);
        }

        String[] originalHeader = original.getHeader();
        String[] updatedHeader = updated.getHeader();
        if (originalHeader == null) {
            assertNull(updatedHeader);
        } else {
            assertArrayEquals(originalHeader, updatedHeader);
        }

        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithNullString_NullValue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withNullString(null);
        assertNull(updated.getNullString(), "Null string can be set to null");
        assertNotSame(original, updated);
    }

    @Test
    @Timeout(8000)
    void testWithNullString_EmptyString() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withNullString("");
        assertEquals("", updated.getNullString(), "Null string can be set to empty string");
        assertNotSame(original, updated);
    }
}