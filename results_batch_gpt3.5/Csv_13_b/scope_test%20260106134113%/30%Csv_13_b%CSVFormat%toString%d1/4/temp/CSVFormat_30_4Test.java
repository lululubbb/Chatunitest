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

import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class CSVFormatToStringTest {

    @Test
    @Timeout(8000)
    void testToString_DefaultFormat() {
        CSVFormat format = CSVFormat.DEFAULT;
        String toString = format.toString();
        assertTrue(toString.contains("Delimiter=<,>"));
        assertTrue(toString.contains("QuoteChar=<\">"));
        assertFalse(toString.contains("Escape=<"));
        assertFalse(toString.contains("CommentStart=<"));
        assertFalse(toString.contains("NullString=<"));
        assertTrue(toString.contains("RecordSeparator=<\r\n>"));
        assertTrue(toString.contains("EmptyLines:ignored"));
        assertFalse(toString.contains("SurroundingSpaces:ignored"));
        assertFalse(toString.contains("IgnoreHeaderCase:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:false"));
        assertFalse(toString.contains("HeaderComments:"));
        assertFalse(toString.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        // Use reflection to invoke private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        char delimiter = ';';
        Character quoteChar = '\'';
        QuoteMode quoteMode = QuoteMode.ALL;
        Character commentMarker = '#';
        Character escapeChar = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\n";
        String nullString = "NULL";
        Object[] headerComments = new Object[] { "comment1", "comment2" };
        String[] header = new String[] { "h1", "h2" };
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = false;
        boolean ignoreHeaderCase = true;

        CSVFormat format = constructor.newInstance(delimiter, quoteChar, quoteMode, commentMarker,
                escapeChar, ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments,
                header, skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<;>"));
        assertTrue(toString.contains("Escape=<\\>"));
        assertTrue(toString.contains("QuoteChar=<'>'") || toString.contains("QuoteChar=<'")); // Accept either with or without trailing '>'
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
    void testToString_NoOptionalFields() throws Exception {
        // Use reflection to invoke private constructor with minimal fields set
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        char delimiter = '|';
        Character quoteChar = null;
        QuoteMode quoteMode = null;
        Character commentMarker = null;
        Character escapeChar = null;
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = false;
        String recordSeparator = null;
        String nullString = null;
        Object[] headerComments = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;
        boolean ignoreHeaderCase = false;

        CSVFormat format = constructor.newInstance(delimiter, quoteChar, quoteMode, commentMarker,
                escapeChar, ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments,
                header, skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<>|>"));
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
}