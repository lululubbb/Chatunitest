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

public class CSVFormat_41_1Test {

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withEscape('\\')
                .withQuote('\'')
                .withCommentMarker('#')
                .withNullString("NULL")
                .withRecordSeparator("\n")
                .withIgnoreEmptyLines(true)
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(true)
                .withSkipHeaderRecord(true)
                .withHeader("h1", "h2")
                .withHeaderComments("c1", "c2");

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<;>"));
        assertTrue(toString.contains("Escape=<\\>"));
        assertTrue(toString.contains("QuoteChar=<'>'"));
        assertTrue(toString.contains("CommentStart=<#>"));
        assertTrue(toString.contains("NullString=<NULL>"));
        assertTrue(toString.contains("RecordSeparator=<\n>"));
        assertTrue(toString.contains("EmptyLines:ignored"));
        assertTrue(toString.contains("SurroundingSpaces:ignored"));
        assertTrue(toString.contains("IgnoreHeaderCase:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:true"));
        assertTrue(toString.contains("HeaderComments:[c1, c2]"));
        assertTrue(toString.contains("Header:[h1, h2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_MinimalFields() {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(',')
                .withSkipHeaderRecord(false);

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<,>"));
        assertFalse(toString.contains("Escape=<"));
        assertFalse(toString.contains("QuoteChar=<"));
        assertFalse(toString.contains("CommentStart=<"));
        assertFalse(toString.contains("NullString=<"));
        assertFalse(toString.contains("RecordSeparator=<null>"));
        assertFalse(toString.contains("EmptyLines:ignored"));
        assertFalse(toString.contains("SurroundingSpaces:ignored"));
        assertFalse(toString.contains("IgnoreHeaderCase:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:false"));
        assertFalse(toString.contains("HeaderComments:"));
        assertFalse(toString.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_Reflection_SetPrivateFields() throws Exception {
        // Create a new CSVFormat instance using the constructor (or a clone of DEFAULT)
        CSVFormat format = createEmptyCSVFormatInstance();

        setField(format, "delimiter", '|');
        setField(format, "escapeCharacter", '*');
        setField(format, "quoteCharacter", '_');
        setField(format, "commentMarker", '!');
        setField(format, "nullString", "nullVal");
        setField(format, "recordSeparator", "\r\n");
        setField(format, "ignoreEmptyLines", true);
        setField(format, "ignoreSurroundingSpaces", true);
        setField(format, "ignoreHeaderCase", true);
        setField(format, "skipHeaderRecord", true);
        setField(format, "headerComments", new String[]{"hc1"});
        setField(format, "header", new String[]{"h1"});

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<|>"));
        assertTrue(toString.contains("Escape=<*>"));
        assertTrue(toString.contains("QuoteChar=<_>"));
        assertTrue(toString.contains("CommentStart=<!>"));
        assertTrue(toString.contains("NullString=<nullVal>"));
        assertTrue(toString.contains("RecordSeparator=<\r\n>"));
        assertTrue(toString.contains("EmptyLines:ignored"));
        assertTrue(toString.contains("SurroundingSpaces:ignored"));
        assertTrue(toString.contains("IgnoreHeaderCase:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:true"));
        assertTrue(toString.contains("HeaderComments:[hc1]"));
        assertTrue(toString.contains("Header:[h1]"));
    }

    private CSVFormat createEmptyCSVFormatInstance() throws Exception {
        // Use reflection to invoke the private constructor with default values
        // Constructor signature:
        // CSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
        // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        // boolean ignoreEmptyLines, String recordSeparator, String nullString,
        // Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim,
        // boolean trailingDelimiter, boolean autoFlush)

        Class<CSVFormat> clazz = CSVFormat.class;

        java.lang.reflect.Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);

        return constructor.newInstance(
                ',',       // delimiter
                null,      // quoteChar
                null,      // quoteMode
                null,      // commentStart
                null,      // escape
                false,     // ignoreSurroundingSpaces
                false,     // ignoreEmptyLines
                null,      // recordSeparator
                null,      // nullString
                null,      // headerComments
                null,      // header
                false,     // skipHeaderRecord
                false,     // allowMissingColumnNames
                false,     // ignoreHeaderCase
                false,     // trim
                false,     // trailingDelimiter
                false      // autoFlush
        );
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier using reflection on the modifiers field if present
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException ignored) {
            // Java 12+ removes the "modifiers" field, ignore if not present
        }

        field.set(target, value);
    }
}