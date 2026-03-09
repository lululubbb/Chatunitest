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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_25_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testHashCode_DefaultInstance() {
        int expected = computeExpectedHashCode(csvFormat);
        assertEquals(expected, csvFormat.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_DifferentDelimiter() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "delimiter", (char) (';' + 1));
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
        assertNotEquals(csvFormat.hashCode(), modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullQuoteMode() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "quoteMode", null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullQuoteCharacter() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "quoteCharacter", null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullCommentMarker() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "commentMarker", null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullEscapeCharacter() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "escapeCharacter", null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullNullString() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "nullString", null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_IgnoreSurroundingSpacesTrue() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "ignoreSurroundingSpaces", true);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_IgnoreSurroundingSpacesFalse() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "ignoreSurroundingSpaces", false);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_IgnoreHeaderCaseTrue() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "ignoreHeaderCase", true);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_IgnoreHeaderCaseFalse() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "ignoreHeaderCase", false);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_IgnoreEmptyLinesTrue() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "ignoreEmptyLines", true);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_IgnoreEmptyLinesFalse() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "ignoreEmptyLines", false);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_SkipHeaderRecordTrue() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "skipHeaderRecord", true);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_SkipHeaderRecordFalse() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "skipHeaderRecord", false);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullRecordSeparator() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "recordSeparator", null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NonNullRecordSeparator() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "recordSeparator", "\n");
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_HeaderArray() throws Exception {
        String[] header1 = new String[] {"A", "B"};
        CSVFormat modified1 = modifyField(csvFormat, "header", header1);
        int expected1 = computeExpectedHashCode(modified1);
        assertEquals(expected1, modified1.hashCode());

        String[] header2 = new String[] {"A", "C"};
        CSVFormat modified2 = modifyField(csvFormat, "header", header2);
        int expected2 = computeExpectedHashCode(modified2);
        assertEquals(expected2, modified2.hashCode());

        // Different headers produce different hashCodes
        assertNotEquals(modified1.hashCode(), modified2.hashCode());
    }

    private int computeExpectedHashCode(CSVFormat format) {
        final int prime = 31;
        int result = 1;
        result = prime * result + format.getDelimiter();
        QuoteMode quoteMode = format.getQuoteMode();
        result = prime * result + (quoteMode == null ? 0 : quoteMode.hashCode());
        Character quoteCharacter = format.getQuoteCharacter();
        result = prime * result + (quoteCharacter == null ? 0 : quoteCharacter.hashCode());
        Character commentMarker = format.getCommentMarker();
        result = prime * result + (commentMarker == null ? 0 : commentMarker.hashCode());
        Character escapeCharacter = format.getEscapeCharacter();
        result = prime * result + (escapeCharacter == null ? 0 : escapeCharacter.hashCode());
        String nullString = format.getNullString();
        result = prime * result + (nullString == null ? 0 : nullString.hashCode());
        result = prime * result + (format.getIgnoreSurroundingSpaces() ? 1231 : 1237);
        result = prime * result + (format.getIgnoreHeaderCase() ? 1231 : 1237);
        result = prime * result + (format.getIgnoreEmptyLines() ? 1231 : 1237);
        result = prime * result + (format.getSkipHeaderRecord() ? 1231 : 1237);
        String recordSeparator = format.getRecordSeparator();
        result = prime * result + (recordSeparator == null ? 0 : recordSeparator.hashCode());
        result = prime * result + Arrays.hashCode(format.getHeader());
        return result;
    }

    private CSVFormat modifyField(CSVFormat original, String fieldName, Object newValue) throws Exception {
        CSVFormat copy = copyCSVFormat(original);
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(copy, newValue);
        return copy;
    }

    private CSVFormat copyCSVFormat(CSVFormat original) throws Exception {
        // Use reflection to copy all fields to a new CSVFormat instance
        // Because CSVFormat constructor is private, create via DEFAULT and modify fields
        CSVFormat copy = CSVFormat.DEFAULT;

        for (Field field : CSVFormat.class.getDeclaredFields()) {
            field.setAccessible(true);
            if ((field.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0) {
                continue; // skip static fields
            }
            Object value = field.get(original);
            field.set(copy, value);
        }
        return copy;
    }
}