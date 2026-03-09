package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_46_5Test {

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNamesTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withAllowMissingColumnNames(true);
        assertNotNull(modified);

        // Use reflection to call getAllowMissingColumnNames() to avoid compilation issues if method is not public
        Method getter = CSVFormat.class.getDeclaredMethod("getAllowMissingColumnNames");
        getter.setAccessible(true);
        boolean modifiedValue = (boolean) getter.invoke(modified);
        boolean originalValue = (boolean) getter.invoke(original);

        assertTrue(modifiedValue);
        // Original remains unchanged
        assertFalse(originalValue);

        // Other properties remain equal
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
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), modified.getTrim());
        assertEquals(original.getTrailingDelimiter(), modified.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), modified.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNamesFalse() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        CSVFormat modified = original.withAllowMissingColumnNames(false);
        assertNotNull(modified);

        Method getter = CSVFormat.class.getDeclaredMethod("getAllowMissingColumnNames");
        getter.setAccessible(true);
        boolean modifiedValue = (boolean) getter.invoke(modified);
        boolean originalValue = (boolean) getter.invoke(original);

        assertFalse(modifiedValue);
        // Original remains unchanged
        assertTrue(originalValue);

        // Other properties remain equal
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
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), modified.getTrim());
        assertEquals(original.getTrailingDelimiter(), modified.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), modified.getAutoFlush());
    }
}