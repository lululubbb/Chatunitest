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

class CSVFormatToStringTest {

    @Test
    @Timeout(8000)
    void testToString_DefaultDelimiterOnly() {
        CSVFormat format = CSVFormat.newFormat(';');
        String str = format.toString();
        assertTrue(str.contains("Delimiter=<;>"));
        // Other optional fields not set
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
    void testToString_AllFieldsSet() {
        CSVFormat format = CSVFormat.DEFAULT
            .withDelimiter('|')
            .withEscape('\\')
            .withQuote('\'')
            .withCommentMarker('#')
            .withNullString("NULL")
            .withRecordSeparator("\n")
            .withIgnoreEmptyLines(true)
            .withIgnoreSurroundingSpaces(true)
            .withSkipHeaderRecord(true)
            .withHeader("col1", "col2");

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<|>"));
        assertTrue(str.contains("Escape=<\\>"));
        assertTrue(str.contains("QuoteChar=<'>'"));
        assertTrue(str.contains("CommentStart=<#>"));
        assertTrue(str.contains("NullString=<NULL>"));
        assertTrue(str.contains("RecordSeparator=<\n>"));
        assertTrue(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:true"));
        assertTrue(str.contains("Header:[col1, col2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_NullRecordSeparatorAndNullString() {
        CSVFormat format = CSVFormat.DEFAULT
                .withRecordSeparator((String) null)
                .withNullString(null)
                .withIgnoreEmptyLines(false)
                .withIgnoreSurroundingSpaces(false)
                .withSkipHeaderRecord(false);

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<,>"));
        assertFalse(str.contains("RecordSeparator=<"));
        assertFalse(str.contains("NullString=<"));
        assertFalse(str.contains("EmptyLines:ignored"));
        assertFalse(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    void testToString_WithEmptyHeader() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        String str = format.toString();
        assertTrue(str.contains("Header:[]"));
    }

    @Test
    @Timeout(8000)
    void testToString_PrivateMethodInvocation() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter('^');
        Method toStringMethod = CSVFormat.class.getMethod("toString");
        String result = (String) toStringMethod.invoke(format);
        assertNotNull(result);
        assertTrue(result.contains("Delimiter=<^>"));
    }
}