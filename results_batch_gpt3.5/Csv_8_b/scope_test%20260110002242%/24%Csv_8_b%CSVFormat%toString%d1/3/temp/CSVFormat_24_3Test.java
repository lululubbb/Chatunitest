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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

class CSVFormat_24_3Test {

    @Test
    @Timeout(8000)
    void testToString_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        String s = format.toString();
        assertTrue(s.contains("Delimiter=<,>"));
        assertTrue(s.contains("QuoteChar=<\""));
        assertFalse(s.contains("Escape=<"));
        assertFalse(s.contains("CommentStart=<"));
        assertFalse(s.contains("NullString=<"));
        assertTrue(s.contains("RecordSeparator=<\r\n>"));
        assertTrue(s.contains("EmptyLines:ignored"));
        assertFalse(s.contains("SurroundingSpaces:ignored"));
        assertTrue(s.contains("SkipHeaderRecord:false"));
        assertFalse(s.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithEscapeQuoteCommentNullStringHeaderSkip() throws Exception {
        // Use reflection to invoke the private constructor
        Class<CSVFormat> clazz = CSVFormat.class;
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
            char.class,
            Character.class,
            Quote.class,
            Character.class,
            Character.class,
            boolean.class,
            boolean.class,
            String.class,
            String.class,
            String[].class,
            boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
            ';',
            '\'',
            null,
            '#',
            '\\',
            true,
            false,
            "\n",
            "NULL",
            new String[]{"h1", "h2"},
            true);

        String s = format.toString();

        assertTrue(s.contains("Delimiter=<;>"));
        assertTrue(s.contains("Escape=<\\>"));
        assertTrue(s.contains("QuoteChar=<'>'"));
        assertTrue(s.contains("CommentStart=<#>"));
        assertTrue(s.contains("NullString=<NULL>"));
        assertTrue(s.contains("RecordSeparator=<\n>"));
        assertFalse(s.contains("EmptyLines:ignored"));
        assertTrue(s.contains("SurroundingSpaces:ignored"));
        assertTrue(s.contains("SkipHeaderRecord:true"));
        assertTrue(s.contains("Header:[h1, h2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_NoQuoteNoEscapeNoCommentNoNullStringNoHeader() throws Exception {
        // Use reflection to invoke the private constructor
        Class<CSVFormat> clazz = CSVFormat.class;
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
            char.class,
            Character.class,
            Quote.class,
            Character.class,
            Character.class,
            boolean.class,
            boolean.class,
            String.class,
            String.class,
            String[].class,
            boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
            '|',
            null,
            null,
            null,
            null,
            false,
            false,
            null,
            null,
            null,
            false);

        String s = format.toString();
        assertTrue(s.contains("Delimiter=<|>"));
        assertFalse(s.contains("Escape=<"));
        assertFalse(s.contains("QuoteChar=<"));
        assertFalse(s.contains("CommentStart=<"));
        assertFalse(s.contains("NullString=<"));
        assertFalse(s.contains("RecordSeparator=<"));
        assertFalse(s.contains("EmptyLines:ignored"));
        assertFalse(s.contains("SurroundingSpaces:ignored"));
        assertTrue(s.contains("SkipHeaderRecord:false"));
        assertFalse(s.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreak_char() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(null, '\n'));
        assertTrue((Boolean) method.invoke(null, '\r'));
        assertFalse((Boolean) method.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreak_Character() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\r')));
        assertFalse((Boolean) method.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) method.invoke(null, (Character) null));
    }
}