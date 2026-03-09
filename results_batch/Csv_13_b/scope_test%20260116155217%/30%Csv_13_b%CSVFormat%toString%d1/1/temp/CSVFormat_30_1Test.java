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

class CSVFormat_30_1Test {

    @Test
    @Timeout(8000)
    void testToString_default() {
        CSVFormat format = CSVFormat.DEFAULT;
        String toString = format.toString();
        assertTrue(toString.contains("Delimiter=<,>"));
        assertTrue(toString.contains("QuoteChar=<\">"));
        assertTrue(toString.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    void testToString_escapeCharacterSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        String s = format.toString();
        assertTrue(s.contains("Escape=<\\>"));
    }

    @Test
    @Timeout(8000)
    void testToString_quoteCharacterSetFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuote((Character) null);
        String s = format.toString();
        assertFalse(s.contains("QuoteChar=<"));
    }

    @Test
    @Timeout(8000)
    void testToString_commentMarkerSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');
        String s = format.toString();
        assertTrue(s.contains("CommentStart=<#>"));
    }

    @Test
    @Timeout(8000)
    void testToString_nullStringSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");
        String s = format.toString();
        assertTrue(s.contains("NullString=<NULL>"));
    }

    @Test
    @Timeout(8000)
    void testToString_recordSeparatorSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator("\n");
        String s = format.toString();
        assertTrue(s.contains("RecordSeparator=<\n>"));
    }

    @Test
    @Timeout(8000)
    void testToString_ignoreEmptyLines() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        String s = format.toString();
        assertTrue(s.contains("EmptyLines:ignored"));
    }

    @Test
    @Timeout(8000)
    void testToString_ignoreSurroundingSpaces() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        String s = format.toString();
        assertTrue(s.contains("SurroundingSpaces:ignored"));
    }

    @Test
    @Timeout(8000)
    void testToString_ignoreHeaderCase() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
        String s = format.toString();
        assertTrue(s.contains("IgnoreHeaderCase:ignored"));
    }

    @Test
    @Timeout(8000)
    void testToString_skipHeaderRecordTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        String s = format.toString();
        assertTrue(s.contains("SkipHeaderRecord:true"));
    }

    @Test
    @Timeout(8000)
    void testToString_headerCommentsAndHeader() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments("comment1", "comment2").withHeader("h1", "h2");
        String s = format.toString();
        assertTrue(s.contains("HeaderComments:[comment1, comment2]"));
        assertTrue(s.contains("Header:[h1, h2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_allCombined() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withEscape('\\')
                .withQuote('\'')
                .withCommentMarker('#')
                .withNullString("NULL")
                .withRecordSeparator("\r\n")
                .withIgnoreEmptyLines(true)
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(true)
                .withSkipHeaderRecord(true)
                .withHeaderComments("c1")
                .withHeader("h1");
        String s = format.toString();
        assertTrue(s.contains("Delimiter=<,>"));
        assertTrue(s.contains("Escape=<\\>"));
        assertTrue(s.contains("QuoteChar=<'>'"));
        assertTrue(s.contains("CommentStart=<#>"));
        assertTrue(s.contains("NullString=<NULL>"));
        assertTrue(s.contains("RecordSeparator=<\r\n>"));
        assertTrue(s.contains("EmptyLines:ignored"));
        assertTrue(s.contains("SurroundingSpaces:ignored"));
        assertTrue(s.contains("IgnoreHeaderCase:ignored"));
        assertTrue(s.contains("SkipHeaderRecord:true"));
        assertTrue(s.contains("HeaderComments:[c1]"));
        assertTrue(s.contains("Header:[h1]"));
    }
}