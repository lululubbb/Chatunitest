package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_55_2Test {

    @Test
    @Timeout(8000)
    void testWithHeaderCommentsWithNull() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat result = base.withHeaderComments((Object[]) null);
        assertNotNull(result);
        assertNull(result.getHeaderComments());
        assertEquals(base.getDelimiter(), result.getDelimiter());
        assertEquals(base.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), result.getQuoteMode());
        assertEquals(base.getCommentMarker(), result.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(base.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(base.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(base.getNullString(), result.getNullString());
        assertArrayEquals(base.getHeader(), result.getHeader());
        assertEquals(base.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(base.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), result.getTrim());
        assertEquals(base.getTrailingDelimiter(), result.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderCommentsWithEmpty() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat result = base.withHeaderComments(new Object[0]);
        assertNotNull(result);
        assertArrayEquals(new String[0], result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderCommentsWithMultipleObjects() {
        CSVFormat base = CSVFormat.DEFAULT;
        Object[] comments = new Object[] { "comment1", 123, null, "comment4" };
        CSVFormat result = base.withHeaderComments(comments);
        assertNotNull(result);
        String[] expected = new String[] { "comment1", "123", null, "comment4" };
        assertArrayEquals(expected, result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderCommentsImmutability() {
        CSVFormat base = CSVFormat.DEFAULT;
        Object[] comments = new Object[] { "a" };
        CSVFormat result = base.withHeaderComments(comments);
        assertNotSame(base, result);
        assertNull(base.getHeaderComments());
        assertArrayEquals(new String[] { "a" }, result.getHeaderComments());
    }
}