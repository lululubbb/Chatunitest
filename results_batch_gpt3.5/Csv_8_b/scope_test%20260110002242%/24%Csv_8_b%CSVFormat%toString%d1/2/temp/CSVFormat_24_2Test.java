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

class CSVFormat_24_2Test {

    @Test
    @Timeout(8000)
    void testToString_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        String str = format.toString();
        assertTrue(str.contains("Delimiter=<,>"));
        assertTrue(str.contains("QuoteChar=<\""));
        assertFalse(str.contains("Escape=<"));
        assertFalse(str.contains("CommentStart=<"));
        assertFalse(str.contains("NullString=<"));
        assertTrue(str.contains("RecordSeparator=<\r\n>"));
        assertTrue(str.contains("EmptyLines:ignored"));
        assertFalse(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:false"));
        assertFalse(str.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithEscapeQuoteCommentNullHeader_SkipHeader() throws Exception {
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
                boolean.class
        );
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
                true
        );
        String str = format.toString();
        assertTrue(str.contains("Delimiter=<;>"));
        assertTrue(str.contains("Escape=<\\>"));
        assertTrue(str.contains("QuoteChar=<'>'"));
        assertTrue(str.contains("CommentStart=<#>"));
        assertTrue(str.contains("NullString=<NULL>"));
        assertTrue(str.contains("RecordSeparator=<\n>"));
        assertFalse(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:true"));
        assertTrue(str.contains("Header:[h1, h2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_NoQuoteNoEscapeNoCommentNoNull() throws Exception {
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
                boolean.class
        );
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
                false
        );
        String str = format.toString();
        assertTrue(str.contains("Delimiter=<|>"));
        assertFalse(str.contains("Escape=<"));
        assertFalse(str.contains("QuoteChar=<"));
        assertFalse(str.contains("CommentStart=<"));
        assertFalse(str.contains("NullString=<"));
        assertFalse(str.contains("RecordSeparator=<"));
        assertFalse(str.contains("EmptyLines:ignored"));
        assertFalse(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:false"));
        assertFalse(str.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreakChar() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);
        assertTrue((Boolean) method.invoke(null, '\n'));
        assertTrue((Boolean) method.invoke(null, '\r'));
        assertFalse((Boolean) method.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreakCharacter() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        assertTrue((Boolean) method.invoke(null, '\n'));
        assertTrue((Boolean) method.invoke(null, '\r'));
        assertFalse((Boolean) method.invoke(null, 'a'));
        assertFalse((Boolean) method.invoke(null, (Character) null));
    }
}