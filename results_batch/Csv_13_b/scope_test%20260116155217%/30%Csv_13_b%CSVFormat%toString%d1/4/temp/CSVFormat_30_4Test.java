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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class CSVFormat_30_4Test {

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        ctor.setAccessible(true);

        CSVFormat format = ctor.newInstance(
                ';',                          // delimiter
                Character.valueOf('"'),       // quoteCharacter
                QuoteMode.ALL,                // quoteMode (not used in toString)
                Character.valueOf('#'),       // commentMarker
                Character.valueOf('\\'),      // escapeCharacter
                true,                        // ignoreSurroundingSpaces
                true,                        // ignoreEmptyLines
                "\r\n",                      // recordSeparator
                "NULL",                      // nullString
                new String[]{"comment1", "comment2"}, // headerComments
                new String[]{"h1", "h2"},    // header
                true,                        // skipHeaderRecord
                true,                        // allowMissingColumnNames (not used in toString)
                true                         // ignoreHeaderCase
        );

        String result = format.toString();

        assertTrue(result.startsWith("Delimiter=<;>"));
        assertTrue(result.contains("Escape=<\\>"));
        assertTrue(result.contains("QuoteChar=<\""));
        assertTrue(result.contains("CommentStart=<#>"));
        assertTrue(result.contains("NullString=<NULL>"));
        assertTrue(result.contains("RecordSeparator=<\r\n>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertTrue(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:true"));
        assertTrue(result.contains("HeaderComments:[comment1, comment2]"));
        assertTrue(result.contains("Header:[h1, h2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_MinimalFields() throws Exception {
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        ctor.setAccessible(true);

        CSVFormat format = ctor.newInstance(
                ',',        // delimiter
                null,       // quoteCharacter
                null,       // quoteMode
                null,       // commentMarker
                null,       // escapeCharacter
                false,      // ignoreSurroundingSpaces
                false,      // ignoreEmptyLines
                null,       // recordSeparator
                null,       // nullString
                null,       // headerComments
                null,       // header
                false,      // skipHeaderRecord
                false,      // allowMissingColumnNames
                false       // ignoreHeaderCase
        );

        String result = format.toString();

        assertEquals("Delimiter=<,> SkipHeaderRecord:false", result);
    }

    @Test
    @Timeout(8000)
    void testToString_SomeFieldsSet() throws Exception {
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        ctor.setAccessible(true);

        CSVFormat format = ctor.newInstance(
                '|',        // delimiter
                Character.valueOf('\''), // quoteCharacter
                null,       // quoteMode
                null,       // commentMarker
                null,       // escapeCharacter
                true,       // ignoreSurroundingSpaces
                false,      // ignoreEmptyLines
                "\n",       // recordSeparator
                null,       // nullString
                null,       // headerComments
                new String[]{"header1"}, // header
                false,      // skipHeaderRecord
                false,      // allowMissingColumnNames
                true        // ignoreHeaderCase
        );

        String result = format.toString();

        assertTrue(result.startsWith("Delimiter=<|>"));
        assertTrue(result.contains("QuoteChar=<'"));
        assertTrue(result.contains("RecordSeparator=<\n>"));
        assertTrue(result.contains("SurroundingSpaces:ignored"));
        assertFalse(result.contains("EmptyLines:ignored"));
        assertTrue(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
        assertTrue(result.contains("Header:[header1]"));
    }

    @Test
    @Timeout(8000)
    void testToString_ReflectionInvocation() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Method toStringMethod = CSVFormat.class.getDeclaredMethod("toString");
        toStringMethod.setAccessible(true);
        String result = (String) toStringMethod.invoke(format);

        assertNotNull(result);
        assertTrue(result.startsWith("Delimiter=<,>"));
    }
}