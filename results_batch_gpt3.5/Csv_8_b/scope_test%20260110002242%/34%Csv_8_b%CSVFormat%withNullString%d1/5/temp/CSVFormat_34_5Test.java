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
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

class CSVFormat_34_5Test {

    @Test
    @Timeout(8000)
    void testWithNullString_NewInstanceHasCorrectNullStringAndOtherFieldsUnchanged() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String newNullString = "NULL";

        // Invoke withNullString method normally
        CSVFormat updated = original.withNullString(newNullString);

        // Check the original instance remains unchanged
        assertNull(original.getNullString());

        // Check the updated instance has the new nullString value
        assertEquals(newNullString, updated.getNullString());

        // Check other fields remain the same between original and updated
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteChar(), updated.getQuoteChar());
        assertEquals(original.getQuotePolicy(), updated.getQuotePolicy());
        assertEquals(original.getCommentStart(), updated.getCommentStart());
        assertEquals(original.getEscape(), updated.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithNullString_ReflectionInvocation() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String newNullString = "NULL";

        // Use getMethod instead of getDeclaredMethod because withNullString is public
        Method withNullStringMethod = CSVFormat.class.getMethod("withNullString", String.class);

        Object result = withNullStringMethod.invoke(original, newNullString);
        assertNotNull(result);
        assertTrue(result instanceof CSVFormat);

        CSVFormat updated = (CSVFormat) result;

        assertEquals(newNullString, updated.getNullString());
        assertNull(original.getNullString());

        // Verify other fields remain unchanged
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteChar(), updated.getQuoteChar());
        assertEquals(original.getQuotePolicy(), updated.getQuotePolicy());
        assertEquals(original.getCommentStart(), updated.getCommentStart());
        assertEquals(original.getEscape(), updated.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
    }
}