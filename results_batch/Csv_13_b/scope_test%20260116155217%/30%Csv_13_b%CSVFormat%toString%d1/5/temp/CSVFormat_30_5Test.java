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
import java.lang.reflect.Method;
import java.util.Arrays;

class CSVFormat_30_5Test {

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter, boolean ignoreSurroundingSpaces,
                                      boolean ignoreEmptyLines, String recordSeparator, String nullString,
                                      Object[] headerComments, String[] header, boolean skipHeaderRecord,
                                      boolean allowMissingColumnNames, boolean ignoreHeaderCase) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        CSVFormat format = createCSVFormat(
                ';',                       // delimiter
                '"',                       // quoteCharacter
                QuoteMode.ALL,             // quoteMode
                '#',                       // commentMarker
                '\\',                      // escapeCharacter
                true,                      // ignoreSurroundingSpaces
                true,                      // ignoreEmptyLines
                "\r\n",                    // recordSeparator
                "NULL",                    // nullString
                new Object[]{"c1", "c2"},  // headerComments
                new String[]{"h1", "h2"},  // header
                true,                      // skipHeaderRecord
                true,                      // allowMissingColumnNames
                true                       // ignoreHeaderCase
        );

        String result = format.toString();

        assertTrue(result.contains("Delimiter=<;>"));
        assertTrue(result.contains("Escape=<\\>"));
        assertTrue(result.contains("QuoteChar=<\">"));
        assertTrue(result.contains("CommentStart=<#>"));
        assertTrue(result.contains("NullString=<NULL>"));
        assertTrue(result.contains("RecordSeparator=<\r\n>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertTrue(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:true"));
        assertTrue(result.contains("HeaderComments:[c1, c2]"));
        assertTrue(result.contains("Header:[h1, h2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_NoOptionalFields() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',                       // delimiter
                null,                      // quoteCharacter
                QuoteMode.MINIMAL,         // quoteMode
                null,                      // commentMarker
                null,                      // escapeCharacter
                false,                     // ignoreSurroundingSpaces
                false,                     // ignoreEmptyLines
                null,                      // recordSeparator
                null,                      // nullString
                null,                      // headerComments
                null,                      // header
                false,                     // skipHeaderRecord
                false,                     // allowMissingColumnNames
                false                      // ignoreHeaderCase
        );

        String result = format.toString();

        assertTrue(result.contains("Delimiter=<,>"));
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
                '|',                       // delimiter
                '\"',                      // quoteCharacter
                QuoteMode.MINIMAL,         // quoteMode
                null,                      // commentMarker
                null,                      // escapeCharacter
                false,                     // ignoreSurroundingSpaces
                true,                      // ignoreEmptyLines
                "\n",                      // recordSeparator
                null,                      // nullString
                null,                      // headerComments
                new String[]{"header1"},   // header
                false,                     // skipHeaderRecord
                false,                     // allowMissingColumnNames
                true                       // ignoreHeaderCase
        );

        String result = format.toString();

        assertTrue(result.contains("Delimiter=<>".replace("<>", "|")));
        assertTrue(result.contains("QuoteChar=<\""));
        assertFalse(result.contains("Escape=<"));
        assertFalse(result.contains("CommentStart=<"));
        assertFalse(result.contains("NullString=<"));
        assertTrue(result.contains("RecordSeparator=<\n>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertFalse(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
        assertFalse(result.contains("HeaderComments:"));
        assertTrue(result.contains("Header:[header1]"));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreakChar() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);
        boolean cr = (boolean) method.invoke(null, '\r');
        boolean lf = (boolean) method.invoke(null, '\n');
        boolean tab = (boolean) method.invoke(null, '\t');
        boolean a = (boolean) method.invoke(null, 'a');

        assertTrue(cr);
        assertTrue(lf);
        assertFalse(tab);
        assertFalse(a);
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreakCharacter() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        boolean cr = (boolean) method.invoke(null, '\r');
        boolean lf = (boolean) method.invoke(null, '\n');
        boolean tab = (boolean) method.invoke(null, '\t');
        boolean a = (boolean) method.invoke(null, 'a');
        boolean nullChar = (boolean) method.invoke(null, (Character) null);

        assertTrue(cr);
        assertTrue(lf);
        assertFalse(tab);
        assertFalse(a);
        assertFalse(nullChar);
    }
}