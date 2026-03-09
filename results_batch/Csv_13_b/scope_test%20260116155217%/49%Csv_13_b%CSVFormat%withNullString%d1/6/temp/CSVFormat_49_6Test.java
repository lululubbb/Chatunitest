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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_49_6Test {

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    public void testWithNullString_NullInput() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Defensive: set headerComments and header to empty arrays if null to avoid NPE in assertArrayEquals
        if (original.getHeaderComments() == null) {
            setField(original, "headerComments", new String[0]);
        }
        if (original.getHeader() == null) {
            setField(original, "header", new String[0]);
        }

        CSVFormat modified = original.withNullString(null);
        assertNotNull(modified);
        assertNotSame(original, modified);
        assertNull(modified.getNullString());
        // Other properties remain unchanged
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());

        assertArrayEquals(original.getHeaderComments() == null ? new String[0] : original.getHeaderComments(),
                modified.getHeaderComments() == null ? new String[0] : modified.getHeaderComments());
        assertArrayEquals(original.getHeader() == null ? new String[0] : original.getHeader(),
                modified.getHeader() == null ? new String[0] : modified.getHeader());

        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithNullString_NonNullInput() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Defensive: set headerComments and header to empty arrays if null to avoid NPE in assertArrayEquals
        if (original.getHeaderComments() == null) {
            setField(original, "headerComments", new String[0]);
        }
        if (original.getHeader() == null) {
            setField(original, "header", new String[0]);
        }

        String nullStr = "NULL_VALUE";
        CSVFormat modified = original.withNullString(nullStr);
        assertNotNull(modified);
        assertNotSame(original, modified);
        assertEquals(nullStr, modified.getNullString());
        // Other properties remain unchanged
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());

        assertArrayEquals(original.getHeaderComments() == null ? new String[0] : original.getHeaderComments(),
                modified.getHeaderComments() == null ? new String[0] : modified.getHeaderComments());
        assertArrayEquals(original.getHeader() == null ? new String[0] : original.getHeader(),
                modified.getHeader() == null ? new String[0] : modified.getHeader());

        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
    }
}