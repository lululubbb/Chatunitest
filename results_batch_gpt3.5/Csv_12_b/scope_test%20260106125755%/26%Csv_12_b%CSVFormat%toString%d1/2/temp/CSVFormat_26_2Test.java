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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

class CSVFormat_26_2Test {

    @Test
    @Timeout(8000)
    void testToString_minimalDelimiterOnly() throws Exception {
        CSVFormat format = CSVFormat.newFormat(';');
        String result = format.toString();
        assertTrue(result.contains("Delimiter=<;>"));
        // None of the optional fields should be present
        assertFalse(result.contains("Escape=<"));
        assertFalse(result.contains("QuoteChar=<"));
        assertFalse(result.contains("CommentStart=<"));
        assertFalse(result.contains("NullString=<"));
        assertFalse(result.contains("RecordSeparator=<"));
        assertFalse(result.contains("EmptyLines:ignored"));
        assertFalse(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
        assertFalse(result.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_allFieldsSet() throws Exception {
        // Use reflection to create CSVFormat instance with private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                '|',
                Character.valueOf('\"'),
                QuoteMode.ALL,
                Character.valueOf('#'),
                Character.valueOf('\\'),
                true,
                true,
                "\n",
                "NULL",
                new String[]{"H1", "H2"},
                true,
                false // allowMissingColumnNames
        );
        String result = format.toString();
        assertTrue(result.contains("Delimiter=<|>"));
        assertTrue(result.contains("Escape=<\\>"));
        assertTrue(result.contains("QuoteChar=<\">"));
        assertTrue(result.contains("CommentStart=<#>"));
        assertTrue(result.contains("NullString=<NULL>"));
        assertTrue(result.contains("RecordSeparator=<\n>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertTrue(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:true"));
        assertTrue(result.contains("Header:[H1, H2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_someFieldsSet() throws Exception {
        // Use reflection to create CSVFormat instance with private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                ',',
                null,
                QuoteMode.MINIMAL,
                null,
                Character.valueOf('\\'),
                false,
                false,
                null,
                null,
                null,
                false,
                false // allowMissingColumnNames
        );
        String result = format.toString();
        assertTrue(result.contains("Delimiter=<,>"));
        assertTrue(result.contains("Escape=<\\>"));
        assertFalse(result.contains("QuoteChar=<"));
        assertFalse(result.contains("CommentStart=<"));
        assertFalse(result.contains("NullString=<"));
        assertFalse(result.contains("RecordSeparator=<"));
        assertFalse(result.contains("EmptyLines:ignored"));
        assertFalse(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
        assertFalse(result.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_ignoreFlags() {
        CSVFormat format = CSVFormat.DEFAULT
                .withIgnoreEmptyLines(true)
                .withIgnoreSurroundingSpaces(true)
                .withSkipHeaderRecord(true);
        String s = format.toString();
        assertTrue(s.contains("EmptyLines:ignored"));
        assertTrue(s.contains("SurroundingSpaces:ignored"));
        assertTrue(s.contains("SkipHeaderRecord:true"));
    }

    @Test
    @Timeout(8000)
    void testToString_headerArray() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("a", "b", "c");
        String s = format.toString();
        assertTrue(s.contains("Header:[a, b, c]"));
    }

    @Test
    @Timeout(8000)
    void testToString_reflectionInvoke() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Method toStringMethod = CSVFormat.class.getDeclaredMethod("toString");
        toStringMethod.setAccessible(true);
        Object result = toStringMethod.invoke(format);
        assertNotNull(result);
        assertTrue(result instanceof String);
        String s = (String) result;
        assertTrue(s.contains("Delimiter=<,>"));
    }
}