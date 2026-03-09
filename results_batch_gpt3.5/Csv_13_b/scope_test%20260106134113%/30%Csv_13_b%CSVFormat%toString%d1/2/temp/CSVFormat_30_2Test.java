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

class CSVFormat_30_2Test {

    private CSVFormat createCSVFormatReflectively(
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
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                QuoteMode.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                Object[].class,
                String[].class,
                boolean.class,
                boolean.class,
                boolean.class);
        ctor.setAccessible(true);

        // Fix for varargs: wrap headerComments and header arrays correctly
        Object headerCommentsArg = headerComments;
        Object headerArg = header;

        return ctor.newInstance(
                delimiter,
                quoteCharacter,
                quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                headerCommentsArg,
                headerArg,
                skipHeaderRecord,
                allowMissingColumnNames,
                ignoreHeaderCase);
    }

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        CSVFormat format = createCSVFormatReflectively(
                ';',                      // delimiter
                '\"',                     // quoteCharacter
                QuoteMode.ALL,            // quoteMode
                '#',                      // commentMarker
                '\\',                     // escapeCharacter
                true,                     // ignoreSurroundingSpaces
                true,                     // ignoreEmptyLines
                "\n",                     // recordSeparator
                "NULL",                   // nullString
                new Object[]{"c1", "c2"}, // headerComments
                new String[]{"h1", "h2"}, // header
                true,                     // skipHeaderRecord
                true,                     // allowMissingColumnNames
                true                      // ignoreHeaderCase
        );

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<;>"));
        assertTrue(str.contains("Escape=<\\>"));
        assertTrue(str.contains("QuoteChar=<\">"));
        assertTrue(str.contains("CommentStart=<#>"));
        assertTrue(str.contains("NullString=<NULL>"));
        assertTrue(str.contains("RecordSeparator=<\n>"));
        assertTrue(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("IgnoreHeaderCase:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:true"));
        assertTrue(str.contains("HeaderComments:" + Arrays.toString(new Object[]{"c1", "c2"})));
        assertTrue(str.contains("Header:" + Arrays.toString(new String[]{"h1", "h2"})));
    }

    @Test
    @Timeout(8000)
    void testToString_MinimalFieldsSet() throws Exception {
        CSVFormat format = createCSVFormatReflectively(
                ',',     // delimiter
                null,    // quoteCharacter
                QuoteMode.MINIMAL,
                null,    // commentMarker
                null,    // escapeCharacter
                false,   // ignoreSurroundingSpaces
                false,   // ignoreEmptyLines
                null,    // recordSeparator
                null,    // nullString
                null,    // headerComments
                null,    // header
                false,   // skipHeaderRecord
                false,   // allowMissingColumnNames
                false    // ignoreHeaderCase
        );

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

    @Test
    @Timeout(8000)
    void testToString_PartialFieldsSet() throws Exception {
        CSVFormat format = createCSVFormatReflectively(
                '\t',       // delimiter
                '\"',       // quoteCharacter
                QuoteMode.ALL,
                null,       // commentMarker
                '\\',       // escapeCharacter
                true,       // ignoreSurroundingSpaces
                false,      // ignoreEmptyLines
                "\r\n",     // recordSeparator
                null,       // nullString
                null,       // headerComments
                new String[]{"header1"}, // header
                false,      // skipHeaderRecord
                false,      // allowMissingColumnNames
                true        // ignoreHeaderCase
        );

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<\t>"));
        assertTrue(str.contains("Escape=<\\>"));
        assertTrue(str.contains("QuoteChar=<\">"));
        assertFalse(str.contains("CommentStart=<"));
        assertFalse(str.contains("NullString=<"));
        assertTrue(str.contains("RecordSeparator=<\r\n>"));
        assertFalse(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("IgnoreHeaderCase:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:false"));
        assertFalse(str.contains("HeaderComments:"));
        assertTrue(str.contains("Header:" + Arrays.toString(new String[]{"header1"})));
    }

    @Test
    @Timeout(8000)
    void testToString_ReflectionInvocation() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Method toStringMethod = CSVFormat.class.getDeclaredMethod("toString");
        toStringMethod.setAccessible(true);

        Object result = toStringMethod.invoke(format);

        assertNotNull(result);
        assertTrue(result instanceof String);
        String str = (String) result;
        assertTrue(str.startsWith("Delimiter=<"));
    }
}