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
import org.junit.jupiter.api.BeforeEach;
import java.lang.reflect.Method;

class CSVFormatToStringTest {

    private CSVFormat baseFormat;

    @BeforeEach
    void setUp() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testToString_DefaultFormat() {
        String toString = baseFormat.toString();
        assertTrue(toString.contains("Delimiter=<,>"));
        assertTrue(toString.contains("QuoteChar=<\""));
        assertTrue(toString.contains("SkipHeaderRecord:false"));
        assertTrue(toString.contains("RecordSeparator=<\r\n>"));
        // Escape, CommentStart, NullString not set in DEFAULT
        assertFalse(toString.contains("Escape=<"));
        assertFalse(toString.contains("CommentStart=<"));
        assertFalse(toString.contains("NullString=<"));
        // IgnoreEmptyLines true in DEFAULT
        assertTrue(toString.contains("EmptyLines:ignored"));
        // IgnoreSurroundingSpaces false in DEFAULT
        assertFalse(toString.contains("SurroundingSpaces:ignored"));
        // Header null in DEFAULT
        assertFalse(toString.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithEscapeCharacter() {
        CSVFormat format = baseFormat.withEscape('\\');
        String toString = format.toString();
        assertTrue(toString.contains("Escape=<\\>"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithQuoteCharacterNull() {
        CSVFormat format = baseFormat.withQuote((Character) null);
        String toString = format.toString();
        assertFalse(toString.contains("QuoteChar=<"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithCommentMarker() {
        CSVFormat format = baseFormat.withCommentMarker('#');
        String toString = format.toString();
        assertTrue(toString.contains("CommentStart=<#>"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithNullString() {
        CSVFormat format = baseFormat.withNullString("NULL");
        String toString = format.toString();
        assertTrue(toString.contains("NullString=<NULL>"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithRecordSeparatorCustom() {
        CSVFormat format = baseFormat.withRecordSeparator("\n");
        String toString = format.toString();
        assertTrue(toString.contains("RecordSeparator=<\n>"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithIgnoreEmptyLinesFalse() {
        CSVFormat format = baseFormat.withIgnoreEmptyLines(false);
        String toString = format.toString();
        assertFalse(toString.contains("EmptyLines:ignored"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithIgnoreSurroundingSpacesTrue() {
        CSVFormat format = baseFormat.withIgnoreSurroundingSpaces(true);
        String toString = format.toString();
        assertTrue(toString.contains("SurroundingSpaces:ignored"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithSkipHeaderRecordTrue() {
        CSVFormat format = baseFormat.withSkipHeaderRecord(true);
        String toString = format.toString();
        assertTrue(toString.contains("SkipHeaderRecord:true"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithHeader() {
        CSVFormat format = baseFormat.withHeader("a", "b", "c");
        String toString = format.toString();
        assertTrue(toString.contains("Header:[a, b, c]"));
    }

    @Test
    @Timeout(8000)
    void testToString_PrivateMethodInvocation() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withQuote('"')
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r")
                .withNullString("NULL")
                .withHeader("h1", "h2")
                .withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(true);

        Method toStringMethod = CSVFormat.class.getDeclaredMethod("toString");
        toStringMethod.setAccessible(true);
        String result = (String) toStringMethod.invoke(format);
        assertTrue(result.contains("Delimiter=<;>"));
        assertTrue(result.contains("Escape=<\\>"));
        assertTrue(result.contains("QuoteChar=<\""));
        assertTrue(result.contains("CommentStart=<#>"));
        assertTrue(result.contains("NullString=<NULL>"));
        assertTrue(result.contains("RecordSeparator=<\r>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertTrue(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:true"));
        assertTrue(result.contains("Header:[h1, h2]"));
    }
}