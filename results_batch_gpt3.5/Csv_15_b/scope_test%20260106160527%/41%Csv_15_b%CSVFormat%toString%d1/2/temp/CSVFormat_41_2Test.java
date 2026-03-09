package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.apache.commons.csv.CSVFormat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

class CSVFormatToStringTest {

    @Test
    @Timeout(8000)
    void testToString_allFieldsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withEscape('\\')
                .withQuote('"')
                .withCommentMarker('#')
                .withNullString("NULL")
                .withRecordSeparator("\n")
                .withIgnoreEmptyLines(true)
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(true)
                .withSkipHeaderRecord(true)
                .withHeader("a", "b")
                .withHeaderComments("c1", "c2");

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<;>"));
        assertTrue(str.contains("Escape=<\\>"));
        assertTrue(str.contains("QuoteChar=<\">"));
        assertTrue(str.contains("CommentStart=<#>"));
        assertTrue(str.contains("NullString=<NULL>"));
        assertTrue(str.contains("RecordSeparator=<\n>") || str.contains("RecordSeparator=<\r\n>") || str.contains("RecordSeparator=<\r>"));
        assertTrue(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("IgnoreHeaderCase:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:true"));
        assertTrue(str.contains("HeaderComments:[c1, c2]"));
        assertTrue(str.contains("Header:[a, b]"));
    }

    @Test
    @Timeout(8000)
    void testToString_noOptionalFieldsSet() {
        CSVFormat format = CSVFormat.DEFAULT
                .withEscape(null)
                .withQuote(null)
                .withCommentMarker(null)
                .withNullString(null)
                .withRecordSeparator(null)
                .withIgnoreEmptyLines(false)
                .withIgnoreSurroundingSpaces(false)
                .withIgnoreHeaderCase(false)
                .withSkipHeaderRecord(false)
                .withHeader((String[]) null)
                .withHeaderComments((Object[]) null);

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<,>"));
        assertFalse(str.contains("Escape=<"));
        assertFalse(str.contains("QuoteChar=<"));
        assertFalse(str.contains("CommentStart=<"));
        assertFalse(str.contains("NullString=<"));
        assertFalse(str.contains("RecordSeparator=<"));
        assertFalse(str.contains("EmptyLines:ignored"));
        assertFalse(str.contains("SurroundingSpaces:ignored"));
        assertFalse(str.contains("IgnoreHeaderCase:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:false"));
        assertFalse(str.contains("HeaderComments:"));
        assertFalse(str.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_partialFieldsSet() {
        CSVFormat format = CSVFormat.DEFAULT
                .withEscape('\\')
                .withQuote(null)
                .withCommentMarker('#')
                .withNullString(null)
                .withRecordSeparator("\r\n")
                .withIgnoreEmptyLines(true)
                .withIgnoreSurroundingSpaces(false)
                .withIgnoreHeaderCase(true)
                .withSkipHeaderRecord(false)
                .withHeader("header1")
                .withHeaderComments();

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<,>"));
        assertTrue(str.contains("Escape=<\\>"));
        assertFalse(str.contains("QuoteChar=<"));
        assertTrue(str.contains("CommentStart=<#>"));
        assertFalse(str.contains("NullString=<"));
        assertTrue(str.contains("RecordSeparator=<\r\n>") || str.contains("RecordSeparator=<\n>") || str.contains("RecordSeparator=<\r>"));
        assertTrue(str.contains("EmptyLines:ignored"));
        assertFalse(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("IgnoreHeaderCase:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:false"));
        assertFalse(str.contains("HeaderComments:"));
        assertTrue(str.contains("Header:[header1]"));
    }

    @Test
    @Timeout(8000)
    void testToString_reflectionPrivateFields() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to set private fields
        setField(format, "delimiter", ':');
        setField(format, "escapeCharacter", '*');
        setField(format, "quoteCharacter", '\'');
        setField(format, "commentMarker", '!');
        setField(format, "nullString", "NULLVAL");
        setField(format, "recordSeparator", "\r");
        setField(format, "ignoreEmptyLines", true);
        setField(format, "ignoreSurroundingSpaces", true);
        setField(format, "ignoreHeaderCase", false);
        setField(format, "skipHeaderRecord", true);
        setField(format, "headerComments", new String[]{"com1"});
        setField(format, "header", new String[]{"head1", "head2"});

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<>") == false);
        assertTrue(str.contains("Delimiter=<:>"));
        assertTrue(str.contains("Escape=<*>"));
        assertTrue(str.contains("QuoteChar=<'>'"));
        assertTrue(str.contains("CommentStart=<!>"));
        assertTrue(str.contains("NullString=<NULLVAL>"));
        assertTrue(str.contains("RecordSeparator=<\r>"));
        assertTrue(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains("SurroundingSpaces:ignored"));
        assertFalse(str.contains("IgnoreHeaderCase:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:true"));
        assertTrue(str.contains("HeaderComments:[com1]"));
        assertTrue(str.contains("Header:[head1, head2]"));
    }

    private void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}