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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CSVFormat_41_4Test {

    @Test
    @Timeout(8000)
    void testToString_allFieldsSet() {
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
                .withHeader("h1", "h2")
                .withHeaderComments("c1", "c2");

        String result = format.toString();

        assertTrue(result.contains("Delimiter=<;>"));
        assertTrue(result.contains("Escape=<\\>"));
        assertTrue(result.contains("QuoteChar=<\">"));
        assertTrue(result.contains("CommentStart=<#>"));
        assertTrue(result.contains("NullString=<NULL>"));
        assertTrue(result.contains("RecordSeparator=<\n>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertTrue(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:true"));
        assertTrue(result.contains("HeaderComments:[c1, c2]"));
        assertTrue(result.contains("Header:[h1, h2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_noOptionalFields() {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(',')
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

        String result = format.toString();

        assertTrue(result.contains("Delimiter=<,>"));
        assertFalse(result.contains("Escape=<"));
        assertFalse(result.contains("QuoteChar=<"));
        assertFalse(result.contains("CommentStart=<"));
        assertFalse(result.contains("NullString=<"));
        assertFalse(result.contains("RecordSeparator=<"));
        assertFalse(result.contains("EmptyLines:ignored"));
        assertFalse(result.contains("SurroundingSpaces:ignored"));
        assertFalse(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
        assertFalse(result.contains("HeaderComments:"));
        assertFalse(result.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_partialFields() {
        CSVFormat format = CSVFormat.DEFAULT
                .withEscape('\\')
                .withNullString("NULL")
                .withIgnoreEmptyLines(true)
                .withSkipHeaderRecord(true);

        String result = format.toString();

        assertTrue(result.contains("Delimiter=<,>"));
        assertTrue(result.contains("Escape=<\\>"));
        assertFalse(result.contains("QuoteChar=<"));
        assertFalse(result.contains("CommentStart=<"));
        assertTrue(result.contains("NullString=<NULL>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:true"));
    }

    @Test
    @Timeout(8000)
    void testToString_recordSeparatorWithCR() {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator("\r");

        String result = format.toString();

        assertTrue(result.contains("RecordSeparator=<\r>"));
    }

    @Test
    @Timeout(8000)
    void testToString_recordSeparatorWithCRLF() {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator("\r\n");

        String result = format.toString();

        assertTrue(result.contains("RecordSeparator=<\r\n>"));
    }
}