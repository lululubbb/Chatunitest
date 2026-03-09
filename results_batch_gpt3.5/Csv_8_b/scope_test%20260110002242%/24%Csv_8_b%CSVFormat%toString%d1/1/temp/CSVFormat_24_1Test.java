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

class CSVFormat_24_1Test {

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, Quote quotePolicy, Character commentStart,
                                      Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header, boolean skipHeaderRecord) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteChar, quotePolicy, commentStart,
                escape, ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        String[] header = new String[]{"col1", "col2"};
        CSVFormat format = createCSVFormat(
                ';',                    // delimiter
                '"',                    // quoteChar
                Quote.MINIMAL,          // quotePolicy (mock or real enum)
                '#',                    // commentStart
                '\\',                   // escape
                true,                   // ignoreSurroundingSpaces
                true,                   // ignoreEmptyLines
                "\n",                   // recordSeparator
                "NULL",                 // nullString
                header,                 // header
                true                    // skipHeaderRecord
        );

        String toStringResult = format.toString();

        assertTrue(toStringResult.contains("Delimiter=<;>"));
        assertTrue(toStringResult.contains("Escape=<\\>"));
        assertTrue(toStringResult.contains("QuoteChar=<\">"));
        assertTrue(toStringResult.contains("CommentStart=<#>"));
        assertTrue(toStringResult.contains("NullString=<NULL>"));
        assertTrue(toStringResult.contains("RecordSeparator=<\n>"));
        assertTrue(toStringResult.contains("EmptyLines:ignored"));
        assertTrue(toStringResult.contains("SurroundingSpaces:ignored"));
        assertTrue(toStringResult.contains("SkipHeaderRecord:true"));
        assertTrue(toStringResult.contains("Header:" + Arrays.toString(header)));
    }

    @Test
    @Timeout(8000)
    void testToString_NoOptionalFields() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',                    // delimiter
                null,                   // quoteChar
                null,                   // quotePolicy
                null,                   // commentStart
                null,                   // escape
                false,                  // ignoreSurroundingSpaces
                false,                  // ignoreEmptyLines
                null,                   // recordSeparator
                null,                   // nullString
                null,                   // header
                false                   // skipHeaderRecord
        );

        String toStringResult = format.toString();

        assertTrue(toStringResult.contains("Delimiter=<,>"));
        assertFalse(toStringResult.contains("Escape=<"));
        assertFalse(toStringResult.contains("QuoteChar=<"));
        assertFalse(toStringResult.contains("CommentStart=<"));
        assertFalse(toStringResult.contains("NullString=<"));
        assertFalse(toStringResult.contains("RecordSeparator=<"));
        assertFalse(toStringResult.contains("EmptyLines:ignored"));
        assertFalse(toStringResult.contains("SurroundingSpaces:ignored"));
        assertTrue(toStringResult.contains("SkipHeaderRecord:false"));
        assertFalse(toStringResult.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_PartialFields() throws Exception {
        CSVFormat format = createCSVFormat(
                '\t',                   // delimiter
                '"',                    // quoteChar
                null,                   // quotePolicy
                null,                   // commentStart
                null,                   // escape
                false,                  // ignoreSurroundingSpaces
                true,                   // ignoreEmptyLines
                "\r\n",                 // recordSeparator
                null,                   // nullString
                null,                   // header
                false                   // skipHeaderRecord
        );

        String toStringResult = format.toString();

        assertTrue(toStringResult.contains("Delimiter=<\t>"));
        assertFalse(toStringResult.contains("Escape=<"));
        assertTrue(toStringResult.contains("QuoteChar=<\">"));
        assertFalse(toStringResult.contains("CommentStart=<"));
        assertFalse(toStringResult.contains("NullString=<"));
        assertTrue(toStringResult.contains("RecordSeparator=<\r\n>"));
        assertTrue(toStringResult.contains("EmptyLines:ignored"));
        assertFalse(toStringResult.contains("SurroundingSpaces:ignored"));
        assertTrue(toStringResult.contains("SkipHeaderRecord:false"));
        assertFalse(toStringResult.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreakChar() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);

        assertTrue((boolean) method.invoke(null, '\n'));
        assertTrue((boolean) method.invoke(null, '\r'));
        assertFalse((boolean) method.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreakCharacter() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);

        assertTrue((boolean) method.invoke(null, '\n'));
        assertTrue((boolean) method.invoke(null, '\r'));
        assertFalse((boolean) method.invoke(null, 'z'));
        assertFalse((boolean) method.invoke(null, (Character) null));
    }
}