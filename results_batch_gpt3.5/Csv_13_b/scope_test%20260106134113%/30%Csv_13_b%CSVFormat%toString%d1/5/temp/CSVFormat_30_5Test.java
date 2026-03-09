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

    @Test
    @Timeout(8000)
    void testToString_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        String str = format.toString();
        assertTrue(str.contains("Delimiter=<,>"));
        assertFalse(str.contains("Escape=<"));
        assertTrue(str.contains("QuoteChar=<\""));
        assertFalse(str.contains("CommentStart=<"));
        assertFalse(str.contains("NullString=<"));
        assertTrue(str.contains("RecordSeparator=<\r\n>"));
        assertTrue(str.contains("EmptyLines:ignored"));
        assertFalse(str.contains(" SurroundingSpaces:ignored"));
        assertFalse(str.contains(" IgnoreHeaderCase:ignored"));
        assertTrue(str.contains(" SkipHeaderRecord:false"));
        assertFalse(str.contains("HeaderComments:"));
        assertFalse(str.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                ';',
                'Q',
                null,
                '#',
                '\\',
                true,
                false,
                "\n",
                "NULL",
                new String[]{"comment1", "comment2"},
                new String[]{"h1", "h2"},
                true,
                true,
                true
        );
        String str = format.toString();
        assertTrue(str.contains("Delimiter=<;>"));
        assertTrue(str.contains(" Escape=<\\>"));
        assertTrue(str.contains(" QuoteChar=<Q>"));
        assertTrue(str.contains(" CommentStart=<#>"));
        assertTrue(str.contains(" NullString=<NULL>"));
        assertTrue(str.contains(" RecordSeparator=<\n>"));
        assertFalse(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains(" SurroundingSpaces:ignored"));
        assertTrue(str.contains(" IgnoreHeaderCase:ignored"));
        assertTrue(str.contains(" SkipHeaderRecord:true"));
        assertTrue(str.contains("HeaderComments:[comment1, comment2]"));
        assertTrue(str.contains("Header:[h1, h2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_NoOptionalFields() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                ',',
                null,
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                null,
                null,
                false,
                false,
                false
        );
        String str = format.toString();
        assertEquals("Delimiter=<,> SkipHeaderRecord:false", str);
    }

    @Test
    @Timeout(8000)
    void testToString_PrivateConstructorAndReflection() throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                '|',
                'Q',
                null,
                '!',
                '\\',
                true,
                true,
                "\r",
                "null",
                new String[]{"c1"},
                new String[]{"header1"},
                true,
                true,
                true
        );
        Method toStringMethod = clazz.getDeclaredMethod("toString");
        toStringMethod.setAccessible(true);
        String str = (String) toStringMethod.invoke(format);
        assertTrue(str.contains("Delimiter=<|>"));
        assertTrue(str.contains(" Escape=<\\>"));
        assertTrue(str.contains(" QuoteChar=<Q>"));
        assertTrue(str.contains(" CommentStart=<!>"));
        assertTrue(str.contains(" NullString=<null>"));
        assertTrue(str.contains(" RecordSeparator=<\r>"));
        assertTrue(str.contains(" EmptyLines:ignored"));
        assertTrue(str.contains(" SurroundingSpaces:ignored"));
        assertTrue(str.contains(" IgnoreHeaderCase:ignored"));
        assertTrue(str.contains(" SkipHeaderRecord:true"));
        assertTrue(str.contains("HeaderComments:[c1]"));
        assertTrue(str.contains("Header:[header1]"));
    }
}