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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_57_5Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_null() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat result = base.withHeaderComments((Object[]) null);
        assertNotNull(result);
        assertNotSame(base, result);
        assertNull(result.getHeaderComments());
        assertArrayEquals(base.getHeader(), result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_empty() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat result = base.withHeaderComments(new Object[0]);
        assertNotNull(result);
        assertNotSame(base, result);
        assertNotNull(result.getHeaderComments());
        assertEquals(0, result.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_variousObjects() {
        CSVFormat base = CSVFormat.DEFAULT;
        Object[] comments = new Object[] { "comment1", 2, null, new StringBuilder("comment4") };
        CSVFormat result = base.withHeaderComments(comments);
        assertNotNull(result);
        assertNotSame(base, result);
        String[] headerComments = result.getHeaderComments();
        assertNotNull(headerComments);
        assertEquals(comments.length, headerComments.length);
        for (int i = 0; i < comments.length; i++) {
            Object expected = comments[i];
            String actual = headerComments[i];
            if (expected == null) {
                assertNull(actual);
            } else {
                assertEquals(expected.toString(), actual);
            }
        }
        // Confirm other fields remain the same
        assertArrayEquals(base.getHeader(), result.getHeader());
        assertEquals(base.getDelimiter(), result.getDelimiter());
        assertEquals(base.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), result.getQuoteMode());
        assertEquals(base.getCommentMarker(), result.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(base.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(base.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(base.getNullString(), result.getNullString());
        assertEquals(base.getHeader() == null, result.getHeader() == null);
        assertEquals(base.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(base.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), result.getTrim());
        assertEquals(base.getTrailingDelimiter(), result.getTrailingDelimiter());
        assertEquals(base.getAutoFlush(), result.getAutoFlush());
    }
}