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

class CSVFormat_26_3Test {

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
                                      Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
                                      boolean ignoreEmptyLines, String recordSeparator, String nullString,
                                      String[] header, boolean skipHeaderRecord, boolean allowMissingColumnNames) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header,
                skipHeaderRecord, allowMissingColumnNames);
    }

    @Test
    @Timeout(8000)
    void testToString_allFieldsSet() throws Exception {
        // Create CSVFormat instance with all fields set
        String[] header = {"col1", "col2"};
        CSVFormat format = createCSVFormat(
                ';',
                '"',
                QuoteMode.ALL,
                '#',
                '\\',
                true,
                true,
                "\n",
                "NULL",
                header,
                true,
                true);

        String result = format.toString();

        assertTrue(result.contains("Delimiter=<;>"));
        assertTrue(result.contains("Escape=<\\>"));
        assertTrue(result.contains("QuoteChar=<\">"));
        assertTrue(result.contains("CommentStart=<#>"));
        assertTrue(result.contains("NullString=<NULL>"));
        assertTrue(result.contains("RecordSeparator=<\n>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertTrue(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:true"));
        assertTrue(result.contains("Header:" + Arrays.toString(header)));
    }

    @Test
    @Timeout(8000)
    void testToString_minimalFields() throws Exception {
        // Create CSVFormat instance with minimal fields set (only delimiter)
        CSVFormat format = createCSVFormat(
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
                false,
                false);

        String result = format.toString();

        assertEquals("Delimiter=<,> SkipHeaderRecord:false", result);
    }

    @Test
    @Timeout(8000)
    void testToString_someFieldsSet() throws Exception {
        // Create CSVFormat instance with some fields set
        CSVFormat format = createCSVFormat(
                ',',
                '\'',
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                null,
                false,
                false);

        String result = format.toString();

        assertTrue(result.contains("Delimiter=<,>"));
        assertTrue(result.contains("QuoteChar=<'"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    void testToString_ignoreFlags() throws Exception {
        // Create CSVFormat instance with ignoreEmptyLines and ignoreSurroundingSpaces true
        CSVFormat format = createCSVFormat(
                ',',
                null,
                null,
                null,
                null,
                true,
                true,
                null,
                null,
                null,
                false,
                false);

        String result = format.toString();

        assertTrue(result.contains("Delimiter=<,>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertTrue(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
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