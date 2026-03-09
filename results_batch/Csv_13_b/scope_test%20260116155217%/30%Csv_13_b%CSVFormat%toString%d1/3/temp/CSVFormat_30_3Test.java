package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.util.Arrays;

class CSVFormat_30_3Test {

    private CSVFormat createCSVFormat(
            char delimiter,
            Character quoteCharacter,
            QuoteMode quoteMode,
            Character commentMarker,
            Character escapeCharacter,
            boolean ignoreSurroundingSpaces,
            boolean ignoreEmptyLines,
            String recordSeparator,
            String nullString,
            Object[] headerComments,
            String[] header,
            boolean skipHeaderRecord,
            boolean allowMissingColumnNames,
            boolean ignoreHeaderCase) throws Exception {

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(
                delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        CSVFormat format = createCSVFormat(
                ';',                // delimiter
                '"',                // quoteCharacter
                QuoteMode.ALL,      // quoteMode
                '#',                // commentMarker
                '\\',               // escapeCharacter
                true,               // ignoreSurroundingSpaces
                true,               // ignoreEmptyLines
                "\n",               // recordSeparator
                "NULL",             // nullString
                new Object[] {"c1","c2"}, // headerComments
                new String[] {"h1","h2"}, // header
                true,               // skipHeaderRecord
                true,               // allowMissingColumnNames
                true                // ignoreHeaderCase
        );

        String result = format.toString();

        String expectedStart = "Delimiter=<;> Escape=<\\> QuoteChar=<\"> CommentStart=<#> NullString=<NULL> RecordSeparator=<\n>";
        assertTrue(result.startsWith(expectedStart) || result.contains("Delimiter=<;>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertTrue(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:true"));
        assertTrue(result.contains("HeaderComments:[c1, c2]"));
        assertTrue(result.contains("Header:[h1, h2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_MinimalFields() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',                // delimiter
                null,               // quoteCharacter
                QuoteMode.MINIMAL,  // quoteMode
                null,               // commentMarker
                null,               // escapeCharacter
                false,              // ignoreSurroundingSpaces
                false,              // ignoreEmptyLines
                null,               // recordSeparator
                null,               // nullString
                null,               // headerComments
                null,               // header
                false,              // skipHeaderRecord
                false,              // allowMissingColumnNames
                false               // ignoreHeaderCase
        );

        String result = format.toString();

        assertTrue(result.startsWith("Delimiter=<,>"));
        assertFalse(result.contains("Escape=<"));
        assertFalse(result.contains("QuoteChar=<"));
        assertFalse(result.contains("CommentStart=<"));
        assertFalse(result.contains("NullString=<"));
        assertFalse(result.contains("RecordSeparator=<"));
        assertFalse(result.contains("EmptyLines:ignored"));
        assertFalse(result.contains("SurroundingSpaces:ignored"));
        assertFalse(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
        assertFalse(result.contains("HeaderComments:"));
        assertFalse(result.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_PartialFields() throws Exception {
        CSVFormat format = createCSVFormat(
                '|',                // delimiter
                '\'',               // quoteCharacter
                QuoteMode.NONE,     // quoteMode
                null,               // commentMarker
                null,               // escapeCharacter
                false,              // ignoreSurroundingSpaces
                true,               // ignoreEmptyLines
                "\r\n",             // recordSeparator
                null,               // nullString
                null,               // headerComments
                new String[] {"header1"}, // header
                false,              // skipHeaderRecord
                false,              // allowMissingColumnNames
                false               // ignoreHeaderCase
        );

        String result = format.toString();

        assertTrue(result.startsWith("Delimiter=<|>"));
        assertTrue(result.contains("QuoteChar=<'"));
        assertFalse(result.contains("Escape=<"));
        assertFalse(result.contains("CommentStart=<"));
        assertFalse(result.contains("NullString=<"));
        assertTrue(result.contains("RecordSeparator=<\r\n>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertFalse(result.contains("SurroundingSpaces:ignored"));
        assertFalse(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
        assertFalse(result.contains("HeaderComments:"));
        assertTrue(result.contains("Header:[header1]"));
    }

    @Test
    @Timeout(8000)
    void testToString_HeaderCommentsOnly() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',                // delimiter
                null,               // quoteCharacter
                QuoteMode.MINIMAL,  // quoteMode
                null,               // commentMarker
                null,               // escapeCharacter
                false,              // ignoreSurroundingSpaces
                false,              // ignoreEmptyLines
                null,               // recordSeparator
                null,               // nullString
                new Object[] {"comment1","comment2"}, // headerComments
                null,               // header
                false,              // skipHeaderRecord
                false,              // allowMissingColumnNames
                false               // ignoreHeaderCase
        );

        String result = format.toString();

        assertTrue(result.contains("HeaderComments:[comment1, comment2]"));
        assertFalse(result.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_UseDefaultConstants() {
        CSVFormat format = CSVFormat.DEFAULT;

        String result = format.toString();

        assertTrue(result.startsWith("Delimiter=<,>"));
        assertTrue(result.contains("QuoteChar=<\""));
        assertFalse(result.contains("Escape=<"));
        assertFalse(result.contains("CommentStart=<"));
        assertTrue(result.contains("RecordSeparator=<\r\n>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertFalse(result.contains("SurroundingSpaces:ignored"));
        assertFalse(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
    }

}