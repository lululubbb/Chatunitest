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

class CSVFormat_57_1Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_nullInput() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments((Object[]) null);
        assertNotNull(result);
        assertNull(result.getHeaderComments());
        assertArrayEquals(format.getHeader(), result.getHeader());
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(format.getQuoteMode(), result.getQuoteMode());
        assertEquals(format.getCommentMarker(), result.getCommentMarker());
        assertEquals(format.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(format.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(format.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(format.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(format.getNullString(), result.getNullString());
        assertEquals(format.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(format.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(format.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(format.getTrim(), result.getTrim());
        assertEquals(format.getTrailingDelimiter(), result.getTrailingDelimiter());
        assertEquals(format.getAutoFlush(), result.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_emptyInput() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments(new Object[0]);
        assertNotNull(result);
        assertNotNull(result.getHeaderComments());
        assertEquals(0, result.getHeaderComments().length);
        assertArrayEquals(format.getHeader(), result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_singleComment() {
        CSVFormat format = CSVFormat.DEFAULT;
        String comment = "Header comment";
        CSVFormat result = format.withHeaderComments((Object) comment);
        assertNotNull(result);
        assertArrayEquals(new String[] { comment }, result.getHeaderComments());
        assertArrayEquals(format.getHeader(), result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_multipleComments() {
        CSVFormat format = CSVFormat.DEFAULT;
        Object[] comments = new Object[] { "Comment1", "Comment2", 123, null };
        CSVFormat result = format.withHeaderComments(comments);
        assertNotNull(result);
        String[] expected = new String[] { "Comment1", "Comment2", "123", null };
        assertArrayEquals(expected, result.getHeaderComments());
        assertArrayEquals(format.getHeader(), result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_immutability() {
        CSVFormat format = CSVFormat.DEFAULT;
        Object[] comments = new Object[] { "Comment" };
        CSVFormat result = format.withHeaderComments(comments);
        assertNotSame(format, result);
        assertNull(format.getHeaderComments());
        assertArrayEquals(new String[] { "Comment" }, result.getHeaderComments());
    }
}