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
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class CSVFormat_30_1Test {

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter, boolean ignoreSurroundingSpaces,
                                      boolean ignoreEmptyLines, String recordSeparator, String nullString,
                                      Object[] headerComments, String[] header, boolean skipHeaderRecord,
                                      boolean allowMissingColumnNames, boolean ignoreHeaderCase) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteCharacter, quoteMode,
                commentMarker, escapeCharacter, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord,
                allowMissingColumnNames, ignoreHeaderCase);
    }

    @Test
    @Timeout(8000)
    public void testToString_AllFieldsSet() throws Exception {
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
                new Object[] {"c1", "c2"},  // headerComments
                new String[] {"h1", "h2"},  // header
                true,               // skipHeaderRecord
                true,               // allowMissingColumnNames
                true                // ignoreHeaderCase
        );

        String toString = format.toString();

        String expectedStart = "Delimiter=<;> Escape=<\\> QuoteChar=<\"> CommentStart=<#> NullString=<NULL> RecordSeparator=<\n>";
        assertTrue(toString.startsWith(expectedStart));
        assertTrue(toString.contains("EmptyLines:ignored"));
        assertTrue(toString.contains("SurroundingSpaces:ignored"));
        assertTrue(toString.contains("IgnoreHeaderCase:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:true"));
        assertTrue(toString.contains("HeaderComments:[c1, c2]"));
        assertTrue(toString.contains("Header:[h1, h2]"));
    }

    @Test
    @Timeout(8000)
    public void testToString_MinimalFields() throws Exception {
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

        String toString = format.toString();

        assertEquals("Delimiter=<,> SkipHeaderRecord:false", toString);
    }

    @Test
    @Timeout(8000)
    public void testToString_PartialFields() throws Exception {
        CSVFormat format = createCSVFormat(
                '\t',               // delimiter
                '\'',               // quoteCharacter
                QuoteMode.ALL,      // quoteMode
                null,               // commentMarker
                null,               // escapeCharacter
                false,              // ignoreSurroundingSpaces
                true,               // ignoreEmptyLines
                "\r\n",             // recordSeparator
                null,               // nullString
                null,               // headerComments
                new String[] {"header1"},  // header
                false,              // skipHeaderRecord
                false,              // allowMissingColumnNames
                true                // ignoreHeaderCase
        );

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<\t>"));
        assertTrue(toString.contains("QuoteChar=<'>'"));
        assertFalse(toString.contains("Escape=<"));
        assertFalse(toString.contains("CommentStart=<"));
        assertFalse(toString.contains("NullString=<"));
        assertTrue(toString.contains("RecordSeparator=<\r\n>"));
        assertTrue(toString.contains("EmptyLines:ignored"));
        assertFalse(toString.contains("SurroundingSpaces:ignored"));
        assertTrue(toString.contains("IgnoreHeaderCase:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:false"));
        assertFalse(toString.contains("HeaderComments:"));
        assertTrue(toString.contains("Header:[header1]"));
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreakChar() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);

        // test line break chars
        assertTrue((Boolean) method.invoke(null, '\n'));
        assertTrue((Boolean) method.invoke(null, '\r'));

        // test non-line break char
        assertFalse((Boolean) method.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreakCharacter() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);

        // test line break chars
        assertTrue((Boolean) method.invoke(null, '\n'));
        assertTrue((Boolean) method.invoke(null, '\r'));

        // test null and non-line break char
        assertFalse((Boolean) method.invoke(null, (Character) null));
        assertFalse((Boolean) method.invoke(null, 'a'));
    }
}