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
import org.junit.jupiter.api.DisplayName;

class CSVFormat_56_2Test {

    @Test
    @Timeout(8000)
    @DisplayName("Test withHeader returns new CSVFormat with given header and preserves other fields")
    void testWithHeader() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] newHeader = new String[] {"col1", "col2", "col3"};

        CSVFormat result = original.withHeader(newHeader);

        assertNotNull(result, "Resulting CSVFormat should not be null");
        assertNotSame(original, result, "withHeader should return a new CSVFormat instance");
        assertArrayEquals(newHeader, result.getHeader(), "Header should be set to newHeader");

        // Check that all other fields remain the same as original
        assertEquals(original.getDelimiter(), result.getDelimiter(), "Delimiter should be unchanged");
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter(), "QuoteCharacter should be unchanged");
        assertEquals(original.getQuoteMode(), result.getQuoteMode(), "QuoteMode should be unchanged");
        assertEquals(original.getCommentMarker(), result.getCommentMarker(), "CommentMarker should be unchanged");
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter(), "EscapeCharacter should be unchanged");
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces(), "IgnoreSurroundingSpaces should be unchanged");
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines(), "IgnoreEmptyLines should be unchanged");
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator(), "RecordSeparator should be unchanged");
        assertEquals(original.getNullString(), result.getNullString(), "NullString should be unchanged");

        // headerComments can be null, so handle nulls properly
        String[] originalHeaderComments = getHeaderComments(original);
        String[] resultHeaderComments = getHeaderComments(result);
        if (originalHeaderComments == null && resultHeaderComments == null) {
            // ok
        } else {
            assertArrayEquals(originalHeaderComments, resultHeaderComments, "HeaderComments should be unchanged");
        }

        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord(), "SkipHeaderRecord should be unchanged");
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames(), "AllowMissingColumnNames should be unchanged");
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase(), "IgnoreHeaderCase should be unchanged");
        assertEquals(original.getTrim(), result.getTrim(), "Trim should be unchanged");
        assertEquals(original.getTrailingDelimiter(), result.getTrailingDelimiter(), "TrailingDelimiter should be unchanged");
        assertEquals(original.getAutoFlush(), result.getAutoFlush(), "AutoFlush should be unchanged");
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test withHeader with null header returns CSVFormat with null header")
    void testWithHeaderNull() {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat result = original.withHeader((String[]) null);

        assertNotNull(result, "Resulting CSVFormat should not be null");
        assertNull(result.getHeader(), "Header should be null when null passed");
        assertNotSame(original, result, "withHeader should return a new CSVFormat instance");
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test withHeader with empty array returns CSVFormat with empty header array")
    void testWithHeaderEmptyArray() {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] emptyHeader = new String[0];

        CSVFormat result = original.withHeader(emptyHeader);

        assertNotNull(result, "Resulting CSVFormat should not be null");
        assertNotNull(result.getHeader(), "Header should not be null");
        assertEquals(0, result.getHeader().length, "Header should be empty array");
        assertNotSame(original, result, "withHeader should return a new CSVFormat instance");
    }

    private String[] getHeaderComments(CSVFormat csvFormat) throws Exception {
        // Use reflection to access private field headerComments
        java.lang.reflect.Field field = CSVFormat.class.getDeclaredField("headerComments");
        field.setAccessible(true);
        return (String[]) field.get(csvFormat);
    }
}