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

class CSVFormat_41_5Test {

    @Test
    @Timeout(8000)
    void testToString_DefaultDelimiterOnly() throws Exception {
        CSVFormat format = CSVFormat.newFormat(';');
        String result = format.toString();
        assertTrue(result.contains("Delimiter=<;>"));
        // No escape, quote, comment, nullString, recordSeparator, emptyLines, surroundingSpaces, headerCase, skipHeaderRecord, headerComments, header
    }

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withEscape('\\')
                .withQuote('"')
                .withCommentMarker('#')
                .withNullString("NULL")
                .withRecordSeparator("\n")
                .withIgnoreEmptyLines(true)
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(true)
                .withSkipHeaderRecord(true)
                .withHeaderComments("a", "b")
                .withHeader("h1", "h2");
        String str = format.toString();

        assertTrue(str.contains("Delimiter=<,>"));
        assertTrue(str.contains("Escape=<\\>"));
        assertTrue(str.contains("QuoteChar=<\">"));
        assertTrue(str.contains("CommentStart=<#>"));
        assertTrue(str.contains("NullString=<NULL>"));
        assertTrue(str.contains("RecordSeparator=<\n>"));
        assertTrue(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("IgnoreHeaderCase:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:true"));
        assertTrue(str.contains("HeaderComments:[a, b]"));
        assertTrue(str.contains("Header:[h1, h2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_NoOptionalFields() throws Exception {
        CSVFormat format = CSVFormat.newFormat('|')
                .withSkipHeaderRecord(false)
                .withIgnoreEmptyLines(false)
                .withIgnoreSurroundingSpaces(false)
                .withIgnoreHeaderCase(false);

        String str = format.toString();
        assertTrue(str.contains("Delimiter=<|>"));
        assertFalse(str.contains("Escape=<"));
        assertFalse(str.contains("QuoteChar=<"));
        assertFalse(str.contains("CommentStart=<"));
        assertFalse(str.contains("NullString=<"));
        assertFalse(str.contains("RecordSeparator=<"));
        assertFalse(str.contains("EmptyLines:ignored"));
        assertFalse(str.contains("SurroundingSpaces:ignored"));
        assertFalse(str.contains("IgnoreHeaderCase:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    void testToString_HeaderNullAndHeaderCommentsNull() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',')
                .withHeader((String[]) null)
                .withHeaderComments((Object[]) null)
                .withSkipHeaderRecord(false);

        String str = format.toString();
        assertTrue(str.contains("Delimiter=<,>"));
        assertFalse(str.contains("HeaderComments:"));
        assertFalse(str.contains("Header:"));
        assertTrue(str.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    void testToString_RecordSeparatorNull() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',')
                .withRecordSeparator((String) null)
                .withSkipHeaderRecord(false);

        String str = format.toString();
        assertTrue(str.contains("Delimiter=<,>"));
        assertFalse(str.contains("RecordSeparator=<"));
    }
}