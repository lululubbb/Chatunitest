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

    @Test
    @Timeout(8000)
    void testToString_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        String toString = format.toString();
        assertTrue(toString.contains("Delimiter=<,>"));
        assertTrue(toString.contains("QuoteChar=<\""));
        assertTrue(toString.contains("RecordSeparator=<\r\n>"));
        assertTrue(toString.contains("EmptyLines:ignored"));
        assertTrue(toString.contains("SurroundingSpaces:ignored"));
        assertTrue(toString.contains("IgnoreHeaderCase:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        setField(format, "delimiter", '|');
        setField(format, "escapeCharacter", '\\');
        setField(format, "quoteCharacter", '\'');
        setField(format, "commentMarker", '#');
        setField(format, "nullString", "NULL");
        setField(format, "recordSeparator", "\n");
        setField(format, "ignoreEmptyLines", true);
        setField(format, "ignoreSurroundingSpaces", true);
        setField(format, "ignoreHeaderCase", true);
        setField(format, "skipHeaderRecord", true);
        setField(format, "headerComments", new String[] {"comment1", "comment2"});
        setField(format, "header", new String[] {"header1", "header2"});

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<|>"));
        assertTrue(toString.contains("Escape=<\\>"));
        assertTrue(toString.contains("QuoteChar=<'>'"));
        assertTrue(toString.contains("CommentStart=<#>"));
        assertTrue(toString.contains("NullString=<NULL>"));
        assertTrue(toString.contains("RecordSeparator=<\n>"));
        assertTrue(toString.contains("EmptyLines:ignored"));
        assertTrue(toString.contains("SurroundingSpaces:ignored"));
        assertTrue(toString.contains("IgnoreHeaderCase:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:true"));
        assertTrue(toString.contains("HeaderComments:[comment1, comment2]"));
        assertTrue(toString.contains("Header:[header1, header2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_NoOptionalFields() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        setField(format, "escapeCharacter", null);
        setField(format, "quoteCharacter", null);
        setField(format, "commentMarker", null);
        setField(format, "nullString", null);
        setField(format, "recordSeparator", null);
        setField(format, "ignoreEmptyLines", false);
        setField(format, "ignoreSurroundingSpaces", false);
        setField(format, "ignoreHeaderCase", false);
        setField(format, "skipHeaderRecord", false);
        setField(format, "headerComments", null);
        setField(format, "header", null);

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<,>"));
        assertFalse(toString.contains("Escape=<"));
        assertFalse(toString.contains("QuoteChar=<"));
        assertFalse(toString.contains("CommentStart=<"));
        assertFalse(toString.contains("NullString=<"));
        assertFalse(toString.contains("RecordSeparator=<"));
        assertFalse(toString.contains("EmptyLines:ignored"));
        assertFalse(toString.contains("SurroundingSpaces:ignored"));
        assertFalse(toString.contains("IgnoreHeaderCase:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:false"));
        assertFalse(toString.contains("HeaderComments:"));
        assertFalse(toString.contains("Header:"));
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        Class<?> fieldType = field.getType();

        if (fieldType == char.class) {
            if (value == null) {
                throw new IllegalArgumentException("Cannot set primitive char field '" + fieldName + "' to null");
            }
            if (value instanceof Character) {
                field.setChar(target, (Character) value);
            } else if (value instanceof String && ((String) value).length() == 1) {
                field.setChar(target, ((String) value).charAt(0));
            } else {
                throw new IllegalArgumentException("Invalid value for char field '" + fieldName + "'");
            }
        } else if (fieldType == Boolean.TYPE) {
            if (value == null) {
                throw new IllegalArgumentException("Cannot set primitive boolean field '" + fieldName + "' to null");
            }
            if (value instanceof Boolean) {
                field.setBoolean(target, (Boolean) value);
            } else {
                throw new IllegalArgumentException("Invalid value for boolean field '" + fieldName + "'");
            }
        } else {
            field.set(target, value);
        }
    }
}