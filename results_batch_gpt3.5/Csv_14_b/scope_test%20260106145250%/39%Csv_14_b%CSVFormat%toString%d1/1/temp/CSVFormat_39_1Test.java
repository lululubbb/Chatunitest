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

import java.lang.reflect.Field;

class CSVFormatToStringTest {

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private String invokeToString(CSVFormat format) {
        // toString is public, no need to getDeclaredMethod or setAccessible
        return format.toString();
    }

    @Test
    @Timeout(8000)
    public void testToString_DefaultDelimiterOnly() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        // delimiter set, no other fields set
        String result = invokeToString(format);
        assertTrue(result.contains("Delimiter=<,>"));
        assertFalse(result.contains("Escape=<"));
        assertFalse(result.contains("QuoteChar=<"));
        assertFalse(result.contains("CommentStart=<"));
        assertFalse(result.contains("NullString=<"));
        assertFalse(result.contains("RecordSeparator=<"));
        assertFalse(result.contains("EmptyLines:ignored"));
        assertFalse(result.contains("SurroundingSpaces:ignored"));
        assertFalse(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    public void testToString_AllFieldsSet() throws Exception {
        CSVFormat format = CSVFormat.newFormat(';');
        setField(format, "escapeCharacter", '\\');
        setField(format, "quoteCharacter", '"');
        setField(format, "commentMarker", '#');
        setField(format, "nullString", "NULL");
        setField(format, "recordSeparator", "\n");
        setField(format, "ignoreEmptyLines", true);
        setField(format, "ignoreSurroundingSpaces", true);
        setField(format, "ignoreHeaderCase", true);
        setField(format, "skipHeaderRecord", true);
        setField(format, "headerComments", new String[]{"comment1", "comment2"});
        setField(format, "header", new String[]{"col1", "col2"});

        String result = invokeToString(format);

        assertTrue(result.contains("Delimiter=<;>"));
        assertTrue(result.contains("Escape=<\\>"));
        assertTrue(result.contains("QuoteChar=<\">"));
        assertTrue(result.contains("CommentStart=<#>"));
        assertTrue(result.contains("NullString=<NULL>"));
        assertTrue(result.contains("RecordSeparator=<\n>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertTrue(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:true"));
        assertTrue(result.contains("HeaderComments:[comment1, comment2]"));
        assertTrue(result.contains("Header:[col1, col2]"));
    }

    @Test
    @Timeout(8000)
    public void testToString_NullRecordSeparatorAndNullHeaderCommentsAndHeader() throws Exception {
        CSVFormat format = CSVFormat.newFormat('|');
        setField(format, "recordSeparator", null);
        setField(format, "headerComments", null);
        setField(format, "header", null);
        setField(format, "skipHeaderRecord", false);

        String result = invokeToString(format);

        assertTrue(result.contains("Delimiter=<|>"));
        assertFalse(result.contains("RecordSeparator=<"));
        assertFalse(result.contains("HeaderComments:"));
        assertFalse(result.contains("Header:"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    public void testToString_SkipHeaderRecordFalse() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        setField(format, "skipHeaderRecord", false);

        String result = invokeToString(format);

        assertTrue(result.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    public void testToString_SkipHeaderRecordTrue() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        setField(format, "skipHeaderRecord", true);

        String result = invokeToString(format);

        assertTrue(result.contains("SkipHeaderRecord:true"));
    }
}