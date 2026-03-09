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

class CSVFormat_55_1Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_nullAndEmpty() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeaderComments((Object[]) null);
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        assertNull(result.getHeaderComments());

        result = baseFormat.withHeaderComments();
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        assertNull(result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_singleAndMultipleComments() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeaderComments("comment1");
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        String[] comments = result.getHeaderComments();
        assertNotNull(comments);
        assertEquals(1, comments.length);
        assertEquals("comment1", comments[0]);

        result = baseFormat.withHeaderComments("comment1", "comment2", (Object) null);
        comments = result.getHeaderComments();
        assertNotNull(comments);
        assertEquals(3, comments.length);
        assertEquals("comment1", comments[0]);
        assertEquals("comment2", comments[1]);
        assertNull(comments[2]);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_preservesOtherProperties() {
        CSVFormat baseFormat = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withQuote('\'')
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withNullString("NULL")
                .withHeader("h1", "h2")
                .withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(true)
                .withIgnoreHeaderCase(true)
                .withTrim(true)
                .withTrailingDelimiter(true);

        CSVFormat result = baseFormat.withHeaderComments("c1", "c2");

        assertNotSame(baseFormat, result);
        assertArrayEquals(new String[]{"c1", "c2"}, result.getHeaderComments());
        assertEquals(';', result.getDelimiter());
        assertEquals(Character.valueOf('\''), result.getQuoteCharacter());
        assertEquals(Character.valueOf('#'), result.getCommentMarker());
        assertEquals(Character.valueOf('\\'), result.getEscapeCharacter());
        assertTrue(result.getIgnoreSurroundingSpaces());
        assertFalse(result.getIgnoreEmptyLines());
        assertEquals("\n", result.getRecordSeparator());
        assertEquals("NULL", result.getNullString());
        assertArrayEquals(new String[]{"h1", "h2"}, result.getHeader());
        assertTrue(result.getSkipHeaderRecord());
        assertTrue(result.getAllowMissingColumnNames());
        assertTrue(result.getIgnoreHeaderCase());
        assertTrue(result.getTrim());
        assertTrue(result.getTrailingDelimiter());
    }
}