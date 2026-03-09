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

import java.lang.reflect.Field;

class CSVFormat_41_6Test {

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withEscape('\\')
                .withQuote('"')
                .withCommentMarker('#')
                .withNullString("NULL")
                .withRecordSeparator("\n")
                .withIgnoreEmptyLines(true)
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(true)
                .withSkipHeaderRecord(true)
                .withHeader("col1", "col2")
                .withHeaderComments("comment1", "comment2");

        // Use reflection to set private fields header and headerComments with arrays
        setField(format, "header", new String[]{"col1", "col2"});
        setField(format, "headerComments", new String[]{"comment1", "comment2"});

        String result = format.toString();

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
    void testToString_MinimalFields() {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(',')
                .withSkipHeaderRecord(false);

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<,>"));
        assertFalse(str.contains("Escape=<"));
        assertTrue(str.contains("QuoteChar=<\"")); // QuoteChar default is DOUBLE_QUOTE_CHAR in DEFAULT
        assertFalse(str.contains("CommentStart=<"));
        assertFalse(str.contains("NullString=<"));
        assertTrue(str.contains("SkipHeaderRecord:false"));
        assertFalse(str.contains("HeaderComments:"));
        assertFalse(str.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_NullRecordSeparator() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator(null);
        // Using reflection to set recordSeparator to null explicitly
        setField(format, "recordSeparator", null);

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<,>"));
        assertFalse(str.contains("RecordSeparator=<"));
    }

    @Test
    @Timeout(8000)
    void testToString_EmptyHeaderCommentsAndHeader() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        setField(format, "headerComments", null);
        setField(format, "header", null);

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<,>"));
        assertFalse(str.contains("HeaderComments:"));
        assertFalse(str.contains("Header:"));
    }

    // Helper method to set private fields via reflection
    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = null;
        Class<?> clazz = target.getClass();
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        if (field == null) {
            throw new NoSuchFieldException(fieldName);
        }
        field.setAccessible(true);

        // Remove final modifier if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        field.set(target, value);
    }
}