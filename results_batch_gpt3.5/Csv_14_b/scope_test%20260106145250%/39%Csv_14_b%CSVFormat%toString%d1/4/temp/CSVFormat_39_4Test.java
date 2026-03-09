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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class CSVFormat_39_4Test {

    @Test
    @Timeout(8000)
    void testToString_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        String result = format.toString();
        assertTrue(result.contains("Delimiter=<,>"));
        assertTrue(result.contains("QuoteChar=<\">"));
        assertFalse(result.contains("Escape=<"));
        assertFalse(result.contains("CommentStart=<"));
        assertFalse(result.contains("NullString=<"));
        assertTrue(result.contains("RecordSeparator=<\r\n>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertTrue(result.contains("SurroundingSpaces:ignored"));
        assertFalse(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithEscapeQuoteCommentNullStringRecordSeparatorAndFlags() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Using reflection to set private final fields: need to remove final modifier first
        setFinalField(format, "escapeCharacter", '\\');
        setFinalField(format, "quoteCharacter", '\'');
        setFinalField(format, "commentMarker", '#');
        setFinalField(format, "nullString", "NULL");
        setFinalField(format, "recordSeparator", "\n");
        setFinalField(format, "ignoreEmptyLines", false);
        setFinalField(format, "ignoreSurroundingSpaces", false);
        setFinalField(format, "ignoreHeaderCase", true);
        setFinalField(format, "skipHeaderRecord", true);

        String[] header = new String[] { "col1", "col2" };
        String[] headerComments = new String[] { "comment1", "comment2" };

        setFinalField(format, "header", header);
        setFinalField(format, "headerComments", headerComments);

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
        assertTrue(result.contains("HeaderComments:" + Arrays.toString(headerComments)));
        assertTrue(result.contains("Header:" + Arrays.toString(header)));
    }

    @Test
    @Timeout(8000)
    void testToString_NullRecordSeparatorAndNullArrays() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        setFinalField(format, "recordSeparator", null);
        setFinalField(format, "header", null);
        setFinalField(format, "headerComments", null);
        setFinalField(format, "ignoreEmptyLines", false);
        setFinalField(format, "ignoreSurroundingSpaces", false);
        setFinalField(format, "ignoreHeaderCase", false);
        setFinalField(format, "skipHeaderRecord", false);

        String result = format.toString();

        assertTrue(result.contains("Delimiter=<,>"));
        assertFalse(result.contains("RecordSeparator=<"));
        assertFalse(result.contains("HeaderComments:"));
        assertFalse(result.contains("Header:"));
        assertFalse(result.contains("EmptyLines:ignored"));
        assertFalse(result.contains("SurroundingSpaces:ignored"));
        assertFalse(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
    }

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // Handle primitive char fields when value is Character or char
        if (field.getType() == char.class) {
            if (value instanceof Character) {
                field.setChar(target, (Character) value);
            } else if (value instanceof Character) {
                field.setChar(target, (char) value);
            } else if (value instanceof String && ((String) value).length() == 1) {
                field.setChar(target, ((String) value).charAt(0));
            } else if (value instanceof Character) {
                field.setChar(target, (Character) value);
            } else if (value instanceof Character) {
                field.setChar(target, (char) value);
            } else {
                throw new IllegalArgumentException("Invalid value for char field " + fieldName);
            }
        } else {
            field.set(target, value);
        }
    }
}