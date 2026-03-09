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

public class CSVFormat_57_4Test {

    @Test
    @Timeout(8000)
    public void testWithHeaderComments_NullHeaderComments() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments((Object[]) null);
        assertNotNull(result);
        assertNotSame(format, result);
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
    public void testWithHeaderComments_EmptyHeaderComments() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments(new Object[0]);
        assertNotNull(result);
        assertNotSame(format, result);
        assertNotNull(result.getHeaderComments());
        assertEquals(0, result.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderComments_WithMultipleComments() {
        CSVFormat format = CSVFormat.DEFAULT;
        Object[] comments = new Object[] {"Comment1", "Comment2", 123, null};
        CSVFormat result = format.withHeaderComments(comments);
        assertNotNull(result);
        assertNotSame(format, result);
        String[] actualComments = result.getHeaderComments();
        assertNotNull(actualComments);
        assertEquals(comments.length, actualComments.length);
        for (int i = 0; i < comments.length; i++) {
            assertEquals(comments[i] == null ? "null" : comments[i].toString(), actualComments[i]);
        }
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderComments_Immutability() {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments("Initial");
        CSVFormat result = format.withHeaderComments("NewComment");
        assertNotSame(format, result);
        assertArrayEquals(new String[] {"Initial"}, format.getHeaderComments());
        assertArrayEquals(new String[] {"NewComment"}, result.getHeaderComments());
    }
}