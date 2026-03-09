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
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class CSVFormat_24_4Test {

    @Test
    @Timeout(8000)
    void testToString_DefaultFormat() {
        CSVFormat format = CSVFormat.DEFAULT;

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<,>"));
        assertTrue(toString.contains("QuoteChar=<\""));
        assertFalse(toString.contains("Escape=<"));
        assertFalse(toString.contains("CommentStart=<"));
        assertFalse(toString.contains("NullString=<"));
        assertTrue(toString.contains("RecordSeparator=<\r\n>"));
        assertTrue(toString.contains("EmptyLines:ignored"));
        assertFalse(toString.contains("SurroundingSpaces:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:false"));
        assertFalse(toString.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        String[] header = new String[] {"A", "B"};
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
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
        ctor.setAccessible(true);
        CSVFormat format = ctor.newInstance(
                ';',
                (Character) 'Q',
                Quote.ALL,
                (Character) '#',
                (Character) '\\',
                true,
                true,
                "\n",
                "NULL",
                header,
                true);

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<;>"));
        assertTrue(toString.contains("Escape=<\\>"));
        assertTrue(toString.contains("QuoteChar=<Q>"));
        assertTrue(toString.contains("CommentStart=<#>"));
        assertTrue(toString.contains("NullString=<NULL>"));
        assertTrue(toString.contains("RecordSeparator=<\n>"));
        assertTrue(toString.contains("EmptyLines:ignored"));
        assertTrue(toString.contains("SurroundingSpaces:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:true"));
        assertTrue(toString.contains("Header:" + Arrays.toString(header)));
    }

    @Test
    @Timeout(8000)
    void testToString_NoEscapeNoQuoteNoCommentNoNullStringNoRecordSeparator() throws Exception {
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
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
        ctor.setAccessible(true);
        CSVFormat format = ctor.newInstance(
                ',',
                null,
                Quote.NONE,
                null,
                null,
                false,
                false,
                null,
                null,
                null,
                false);

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<,>"));
        assertFalse(toString.contains("Escape=<"));
        assertFalse(toString.contains("QuoteChar=<"));
        assertFalse(toString.contains("CommentStart=<"));
        assertFalse(toString.contains("NullString=<"));
        assertFalse(toString.contains("RecordSeparator=<"));
        assertFalse(toString.contains("EmptyLines:ignored"));
        assertFalse(toString.contains("SurroundingSpaces:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:false"));
        assertFalse(toString.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreakChar() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);

        boolean resultCr = (boolean) method.invoke(null, '\r');
        boolean resultLf = (boolean) method.invoke(null, '\n');
        boolean resultOther = (boolean) method.invoke(null, 'a');

        assertTrue(resultCr);
        assertTrue(resultLf);
        assertFalse(resultOther);
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreakCharacter() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);

        boolean resultCr = (boolean) method.invoke(null, Character.valueOf('\r'));
        boolean resultLf = (boolean) method.invoke(null, Character.valueOf('\n'));
        boolean resultNull = (boolean) method.invoke(null, new Object[] { null });
        boolean resultOther = (boolean) method.invoke(null, Character.valueOf('a'));

        assertTrue(resultCr);
        assertTrue(resultLf);
        assertFalse(resultNull);
        assertFalse(resultOther);
    }
}