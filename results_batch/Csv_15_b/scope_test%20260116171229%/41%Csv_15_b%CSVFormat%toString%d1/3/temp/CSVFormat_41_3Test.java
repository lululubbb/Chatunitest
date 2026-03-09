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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

class CSVFormat_41_3Test {

    @Test
    @Timeout(8000)
    void testToString_DefaultDelimiterOnly() {
        CSVFormat format = CSVFormat.newFormat(';');
        String toString = format.toString();
        assertTrue(toString.contains("Delimiter=<;>"));
        // Other optional fields should not be present
        assertFalse(toString.contains("Escape=<"));
        assertFalse(toString.contains("QuoteChar=<"));
        assertFalse(toString.contains("CommentStart=<"));
        assertFalse(toString.contains("NullString=<"));
        assertFalse(toString.contains("RecordSeparator=<"));
        assertFalse(toString.contains("EmptyLines:ignored"));
        assertFalse(toString.contains("SurroundingSpaces:ignored"));
        assertFalse(toString.contains("IgnoreHeaderCase:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    void testToString_AllFlagsAndFieldsSet() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        // Setting private final fields by reflection
        setFinalField(format, "escapeCharacter", '\\');
        setFinalField(format, "quoteCharacter", '"');
        setFinalField(format, "commentMarker", '#');
        setFinalField(format, "nullString", "NULL");
        setFinalField(format, "recordSeparator", "\n");
        setFinalField(format, "ignoreEmptyLines", true);
        setFinalField(format, "ignoreSurroundingSpaces", true);
        setFinalField(format, "ignoreHeaderCase", true);
        setFinalField(format, "skipHeaderRecord", true);
        setFinalField(format, "headerComments", new String[]{"comment1", "comment2"});
        setFinalField(format, "header", new String[]{"h1", "h2"});

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<,>"));
        assertTrue(toString.contains("Escape=<\\>"));
        assertTrue(toString.contains("QuoteChar=<\">"));
        assertTrue(toString.contains("CommentStart=<#>"));
        assertTrue(toString.contains("NullString=<NULL>"));
        assertTrue(toString.contains("RecordSeparator=<\n>"));
        assertTrue(toString.contains("EmptyLines:ignored"));
        assertTrue(toString.contains("SurroundingSpaces:ignored"));
        assertTrue(toString.contains("IgnoreHeaderCase:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:true"));
        assertTrue(toString.contains("HeaderComments:[comment1, comment2]"));
        assertTrue(toString.contains("Header:[h1, h2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_NullOptionalFields() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        setFinalField(format, "escapeCharacter", null);
        setFinalField(format, "quoteCharacter", null);
        setFinalField(format, "commentMarker", null);
        setFinalField(format, "nullString", null);
        setFinalField(format, "recordSeparator", null);
        setFinalField(format, "headerComments", null);
        setFinalField(format, "header", null);
        setFinalField(format, "ignoreEmptyLines", false);
        setFinalField(format, "ignoreSurroundingSpaces", false);
        setFinalField(format, "ignoreHeaderCase", false);
        setFinalField(format, "skipHeaderRecord", false);

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

    // Helper method to set private final fields via reflection
    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // For primitive char fields, handle Character value unboxing
        if (field.getType() == char.class) {
            if (value == null) {
                throw new IllegalArgumentException("Cannot set primitive char field to null");
            }
            if (value instanceof Character) {
                field.setChar(target, (Character) value);
            } else if (value instanceof Character) {
                field.setChar(target, (char) value);
            } else if (value instanceof String && ((String) value).length() == 1) {
                field.setChar(target, ((String) value).charAt(0));
            } else {
                throw new IllegalArgumentException("Invalid value for char field: " + value);
            }
        } else {
            field.set(target, value);
        }
    }
}