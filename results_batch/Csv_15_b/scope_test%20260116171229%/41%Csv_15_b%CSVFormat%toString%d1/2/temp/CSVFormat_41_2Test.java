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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class CSVFormat_41_2Test {

    @Test
    @Timeout(8000)
    public void testToString_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        String str = format.toString();
        assertTrue(str.contains("Delimiter=<,>"));
        assertTrue(str.contains("QuoteChar=<\""));
        assertTrue(str.contains("RecordSeparator=<\r\n>"));
        assertTrue(str.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    public void testToString_AllFieldsSet() throws Exception {
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

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<;>"));
        assertTrue(str.contains("Escape=<\\>"));
        assertTrue(str.contains("QuoteChar=<'"));
        assertTrue(str.contains("CommentStart=<#>"));
        assertTrue(str.contains("NullString=<NULL>"));
        assertTrue(str.contains("RecordSeparator=<\n>"));
        assertTrue(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("IgnoreHeaderCase:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:true"));
        assertTrue(str.contains("HeaderComments:[c1, c2]"));
        assertTrue(str.contains("Header:[h1, h2]"));
    }

    @Test
    @Timeout(8000)
    public void testToString_NullRecordSeparatorAndNullFields() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to set recordSeparator to null and other nullable fields to null
        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);

        // Remove final modifier to allow modification
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);

        modifiersField.setInt(recordSeparatorField, recordSeparatorField.getModifiers() & ~Modifier.FINAL);
        recordSeparatorField.set(format, null);

        modifiersField.setInt(escapeCharacterField, escapeCharacterField.getModifiers() & ~Modifier.FINAL);
        escapeCharacterField.set(format, null);

        modifiersField.setInt(quoteCharacterField, quoteCharacterField.getModifiers() & ~Modifier.FINAL);
        quoteCharacterField.set(format, null);

        modifiersField.setInt(commentMarkerField, commentMarkerField.getModifiers() & ~Modifier.FINAL);
        commentMarkerField.set(format, null);

        modifiersField.setInt(nullStringField, nullStringField.getModifiers() & ~Modifier.FINAL);
        nullStringField.set(format, null);

        modifiersField.setInt(headerCommentsField, headerCommentsField.getModifiers() & ~Modifier.FINAL);
        headerCommentsField.set(format, null);

        modifiersField.setInt(headerField, headerField.getModifiers() & ~Modifier.FINAL);
        headerField.set(format, null);

        // Also set booleans to false
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        modifiersField.setInt(ignoreEmptyLinesField, ignoreEmptyLinesField.getModifiers() & ~Modifier.FINAL);
        ignoreEmptyLinesField.setBoolean(format, false);

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        modifiersField.setInt(ignoreSurroundingSpacesField, ignoreSurroundingSpacesField.getModifiers() & ~Modifier.FINAL);
        ignoreSurroundingSpacesField.setBoolean(format, false);

        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        modifiersField.setInt(ignoreHeaderCaseField, ignoreHeaderCaseField.getModifiers() & ~Modifier.FINAL);
        ignoreHeaderCaseField.setBoolean(format, false);

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        modifiersField.setInt(skipHeaderRecordField, skipHeaderRecordField.getModifiers() & ~Modifier.FINAL);
        skipHeaderRecordField.setBoolean(format, false);

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<,>"));
        assertFalse(str.contains("Escape=<"));
        assertFalse(str.contains("QuoteChar=<"));
        assertFalse(str.contains("CommentStart=<"));
        assertFalse(str.contains("NullString=<"));
        assertFalse(str.contains("RecordSeparator=<"));
        assertFalse(str.contains("EmptyLines:ignored"));
        assertFalse(str.contains("SurroundingSpaces:ignored"));
        assertFalse(str.contains("IgnoreHeaderCase:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:false"));
        assertFalse(str.contains("HeaderComments:"));
        assertFalse(str.contains("Header:"));
    }
}