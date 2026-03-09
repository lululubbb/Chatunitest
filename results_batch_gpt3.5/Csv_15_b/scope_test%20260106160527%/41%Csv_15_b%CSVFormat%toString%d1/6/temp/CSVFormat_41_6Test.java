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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

class CSVFormat_41_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testToString_Default() {
        String str = csvFormat.toString();
        assertTrue(str.contains("Delimiter=<,>"));
        assertTrue(str.contains("QuoteChar=<\""));
        assertTrue(str.contains("SkipHeaderRecord:false"));
        assertTrue(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("IgnoreHeaderCase:ignored"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithEscapeCharacter() {
        CSVFormat format = csvFormat.withEscape('\\');
        String str = format.toString();
        assertTrue(str.contains("Escape=<\\>"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithCommentMarker() {
        CSVFormat format = csvFormat.withCommentMarker('#');
        String str = format.toString();
        assertTrue(str.contains("CommentStart=<#>"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithNullString() {
        CSVFormat format = csvFormat.withNullString("NULL");
        String str = format.toString();
        assertTrue(str.contains("NullString=<NULL>"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithRecordSeparator() {
        CSVFormat format = csvFormat.withRecordSeparator("\n");
        String str = format.toString();
        // The record separator might be printed as \n or \r\n depending on platform
        assertTrue(str.contains("RecordSeparator=<\n>") || str.contains("RecordSeparator=<\r\n>"));
    }

    @Test
    @Timeout(8000)
    void testToString_IgnoreFlags() {
        CSVFormat format = csvFormat
                .withIgnoreEmptyLines(false)
                .withIgnoreSurroundingSpaces(false)
                .withIgnoreHeaderCase(false);
        String str = format.toString();
        assertFalse(str.contains("EmptyLines:ignored"));
        assertFalse(str.contains("SurroundingSpaces:ignored"));
        assertFalse(str.contains("IgnoreHeaderCase:ignored"));

        format = csvFormat
                .withIgnoreEmptyLines(true)
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(true);
        str = format.toString();
        assertTrue(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("IgnoreHeaderCase:ignored"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithHeaderCommentsAndHeader() {
        CSVFormat format = csvFormat
                .withHeaderComments("comment1", "comment2")
                .withHeader("header1", "header2");
        String str = format.toString();
        assertTrue(str.contains("HeaderComments:" + Arrays.toString(new String[]{"comment1", "comment2"})));
        assertTrue(str.contains("Header:" + Arrays.toString(new String[]{"header1", "header2"})));
    }

    @Test
    @Timeout(8000)
    void testToString_SkipHeaderRecordTrue() {
        CSVFormat format = csvFormat.withSkipHeaderRecord(true);
        String str = format.toString();
        assertTrue(str.contains("SkipHeaderRecord:true"));
    }
}