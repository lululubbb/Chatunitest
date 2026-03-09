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

class CSVFormatToStringTest {

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        CSVFormat format = createCSVFormat(
                ';',
                '"',
                QuoteMode.ALL,
                '#',
                '\\',
                true,
                true,
                "\n",
                "NULL",
                new String[]{"col1", "col2"},
                new String[]{"comment1", "comment2"},
                true,
                true
        );

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<;>"));
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
        assertTrue(toString.contains("Header:[col1, col2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_MinimalFieldsSet() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',
                null,
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                null,
                null,
                false,
                false
        );

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

    @Test
    @Timeout(8000)
    void testToString_PartialFieldsSet() throws Exception {
        CSVFormat format = createCSVFormat(
                '\t',
                '\'',
                QuoteMode.MINIMAL,
                null,
                null,
                true,
                false,
                "\r",
                null,
                new String[]{"header1"},
                null,
                false,
                true
        );

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<\t>"));
        assertFalse(toString.contains("Escape=<"));
        assertTrue(toString.contains("QuoteChar=<'>'"));
        assertFalse(toString.contains("CommentStart=<"));
        assertFalse(toString.contains("NullString=<"));
        assertTrue(toString.contains("RecordSeparator=<\r>"));
        assertFalse(toString.contains("EmptyLines:ignored"));
        assertTrue(toString.contains("SurroundingSpaces:ignored"));
        assertTrue(toString.contains("IgnoreHeaderCase:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:false"));
        assertFalse(toString.contains("HeaderComments:"));
        assertTrue(toString.contains("Header:[header1]"));
    }

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
            String[] header,
            String[] headerComments,
            boolean skipHeaderRecord,
            boolean ignoreHeaderCase
    ) throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class,
                Character.class,
                QuoteMode.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                String[].class,
                String[].class,
                boolean.class,
                boolean.class,
                boolean.class
        );
        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter,
                quoteCharacter,
                quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                header,
                headerComments,
                skipHeaderRecord,
                false, // allowMissingColumnNames (not tested here)
                ignoreHeaderCase
        );
    }
}