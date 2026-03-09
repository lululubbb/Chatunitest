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

class CSVFormat_24_5Test {

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
    void testToString_AllFieldsSet() throws Exception {
        String[] header = new String[] {"a","b"};
        Class<?> quoteClass = null;
        for (Class<?> cls : CSVFormat.class.getDeclaredClasses()) {
            if ("Quote".equals(cls.getSimpleName())) {
                quoteClass = cls;
                break;
            }
        }
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, quoteClass, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        ctor.setAccessible(true);
        CSVFormat format = ctor.newInstance(
                ';',
                'Q',
                null,
                '#',
                '\\',
                true,
                true,
                "\n",
                "NULL",
                header,
                true);

        String str = format.toString();
        assertTrue(str.contains("Delimiter=<;>"));
        assertTrue(str.contains("Escape=<\\>"));
        assertTrue(str.contains("QuoteChar=<Q>"));
        assertTrue(str.contains("CommentStart=<#>"));
        assertTrue(str.contains("NullString=<NULL>"));
        assertTrue(str.contains("RecordSeparator=<\n>"));
        assertTrue(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:true"));
        assertTrue(str.contains("Header:" + Arrays.toString(header)));
    }

    @Test
    @Timeout(8000)
    void testToString_NoEscapeNoQuoteNoCommentNoNullStringNoRecordSeparator() throws Exception {
        Class<?> quoteClass = null;
        for (Class<?> cls : CSVFormat.class.getDeclaredClasses()) {
            if ("Quote".equals(cls.getSimpleName())) {
                quoteClass = cls;
                break;
            }
        }
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, quoteClass, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        ctor.setAccessible(true);
        CSVFormat format = ctor.newInstance(
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

        String str = format.toString();
        assertTrue(str.contains("Delimiter=|"));
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
    void testToString_WithHeaderEmpty() throws Exception {
        Class<?> quoteClass = null;
        for (Class<?> cls : CSVFormat.class.getDeclaredClasses()) {
            if ("Quote".equals(cls.getSimpleName())) {
                quoteClass = cls;
                break;
            }
        }
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, quoteClass, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        ctor.setAccessible(true);
        CSVFormat format = ctor.newInstance(
                ',',
                '"',
                null,
                null,
                null,
                false,
                false,
                "\r",
                null,
                new String[0],
                false);

        String str = format.toString();
        assertTrue(str.contains("Delimiter=<,>"));
        assertTrue(str.contains("QuoteChar=<\""));
        assertTrue(str.contains("RecordSeparator=<\r>"));
        assertFalse(str.contains("EmptyLines:ignored"));
        assertFalse(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:false"));
        assertTrue(str.contains("Header:[]"));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreakChar() throws Exception {
        Method m = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        m.setAccessible(true);
        assertTrue((boolean) m.invoke(null, '\n'));
        assertTrue((boolean) m.invoke(null, '\r'));
        assertFalse((boolean) m.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreakCharacter() throws Exception {
        Method m = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        m.setAccessible(true);
        assertTrue((boolean) m.invoke(null, '\n'));
        assertTrue((boolean) m.invoke(null, '\r'));
        assertFalse((boolean) m.invoke(null, 'a'));
        assertFalse((boolean) m.invoke(null, (Character) null));
    }
}