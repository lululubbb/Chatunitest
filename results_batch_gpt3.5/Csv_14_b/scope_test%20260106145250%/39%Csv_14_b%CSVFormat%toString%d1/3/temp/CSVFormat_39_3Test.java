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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

class CSVFormatToStringTest {

    @Test
    @Timeout(8000)
    void testToString_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        String result = format.toString();
        assertTrue(result.contains("Delimiter=<,>"));
        assertTrue(result.contains("QuoteChar=<\""));
        assertTrue(result.contains("RecordSeparator=<\r\n>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertTrue(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
        assertFalse(result.contains("Escape=<"));
        assertFalse(result.contains("CommentStart=<"));
        assertFalse(result.contains("NullString=<"));
        assertFalse(result.contains("HeaderComments:"));
        assertFalse(result.contains("Header:"));
        assertFalse(result.contains("IgnoreHeaderCase:ignored"));
    }

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to set private final fields to non-default values
        setFinalField(format, "escapeCharacter", Character.valueOf('\\'));
        setFinalField(format, "quoteCharacter", Character.valueOf('\''));
        setFinalField(format, "commentMarker", Character.valueOf('#'));
        setFinalField(format, "nullString", "NULL");
        setFinalField(format, "recordSeparator", "\n");
        setFinalField(format, "ignoreEmptyLines", false);
        setFinalField(format, "ignoreSurroundingSpaces", false);
        setFinalField(format, "ignoreHeaderCase", true);
        setFinalField(format, "skipHeaderRecord", true);
        setFinalField(format, "headerComments", new String[]{"comment1", "comment2"});
        setFinalField(format, "header", new String[]{"header1", "header2"});

        String result = format.toString();

        assertTrue(result.contains("Delimiter=<,>"));
        assertTrue(result.contains("Escape=<\\>"));
        assertTrue(result.contains("QuoteChar=<'>'"));
        assertTrue(result.contains("CommentStart=<#>"));
        assertTrue(result.contains("NullString=<NULL>"));
        assertTrue(result.contains("RecordSeparator=<\n>"));
        assertFalse(result.contains("EmptyLines:ignored"));
        assertFalse(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:true"));
        assertTrue(result.contains("HeaderComments:[comment1, comment2]"));
        assertTrue(result.contains("Header:[header1, header2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_NoRecordSeparator() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        setFinalField(format, "recordSeparator", null);
        String result = format.toString();
        assertFalse(result.contains("RecordSeparator=<"));
    }

    @Test
    @Timeout(8000)
    void testToString_EmptyHeaderCommentsAndHeader() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        setFinalField(format, "headerComments", null);
        setFinalField(format, "header", null);
        String result = format.toString();
        assertFalse(result.contains("HeaderComments:"));
        assertFalse(result.contains("Header:"));
    }

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier using reflection on Field modifiers
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }
}