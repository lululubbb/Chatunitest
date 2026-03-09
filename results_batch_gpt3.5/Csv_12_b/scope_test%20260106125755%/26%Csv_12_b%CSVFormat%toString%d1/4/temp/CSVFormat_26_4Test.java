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

class CSVFormat_26_4Test {

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord, boolean allowMissingColumnNames) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header,
                skipHeaderRecord, allowMissingColumnNames);
    }

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        CSVFormat format = createCSVFormat(
                ';',                      // delimiter
                '"',                      // quoteCharacter
                null,                     // quoteMode (not used in toString)
                '#',                      // commentMarker
                '\\',                     // escapeCharacter
                true,                     // ignoreSurroundingSpaces
                true,                     // ignoreEmptyLines
                "\n",                     // recordSeparator
                "NULL",                   // nullString
                new String[]{"A", "B"},   // header
                true,                     // skipHeaderRecord
                false                     // allowMissingColumnNames (not used in toString)
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
        assertTrue(toStringResult.contains("Header:" + Arrays.toString(new String[]{"A", "B"})));
    }

    @Test
    @Timeout(8000)
    void testToString_NoOptionalFields() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',        // delimiter
                null,       // quoteCharacter
                null,       // quoteMode
                null,       // commentMarker
                null,       // escapeCharacter
                false,      // ignoreSurroundingSpaces
                false,      // ignoreEmptyLines
                null,       // recordSeparator
                null,       // nullString
                null,       // header
                false,      // skipHeaderRecord
                false       // allowMissingColumnNames
        );

        String toStringResult = format.toString();

        assertEquals("Delimiter=<,> SkipHeaderRecord:false", toStringResult);
    }

    @Test
    @Timeout(8000)
    void testToString_PartialFields() throws Exception {
        CSVFormat format = createCSVFormat(
                '|',        // delimiter
                null,       // quoteCharacter
                null,       // quoteMode
                '!',        // commentMarker
                null,       // escapeCharacter
                false,      // ignoreSurroundingSpaces
                true,       // ignoreEmptyLines
                null,       // recordSeparator
                "NULL",     // nullString
                new String[]{"X"}, // header
                false,      // skipHeaderRecord
                false       // allowMissingColumnNames
        );

        String toStringResult = format.toString();

        assertTrue(toStringResult.contains("Delimiter=<|>"));
        assertFalse(toStringResult.contains("Escape=<"));
        assertFalse(toStringResult.contains("QuoteChar=<"));
        assertTrue(toStringResult.contains("CommentStart=<!>"));
        assertTrue(toStringResult.contains("NullString=<NULL>"));
        assertFalse(toStringResult.contains("RecordSeparator=<"));
        assertTrue(toStringResult.contains("EmptyLines:ignored"));
        assertFalse(toStringResult.contains("SurroundingSpaces:ignored"));
        assertTrue(toStringResult.contains("SkipHeaderRecord:false"));
        assertTrue(toStringResult.contains("Header:" + Arrays.toString(new String[]{"X"})));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreakChar() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        assertTrue((boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((boolean) isLineBreakChar.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreakCharacter() throws Exception {
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        assertTrue((boolean) isLineBreakCharacter.invoke(null, '\n'));
        assertTrue((boolean) isLineBreakCharacter.invoke(null, '\r'));
        assertFalse((boolean) isLineBreakCharacter.invoke(null, 'a'));
        assertFalse((boolean) isLineBreakCharacter.invoke(null, (Character) null));
    }

}