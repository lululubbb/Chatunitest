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
import java.util.Arrays;

class CSVFormatToStringTest {

    // Helper to create CSVFormat instance using the public factory method and with* methods
    private CSVFormat createCSVFormat(char delimiter,
                                      Character quoteCharacter,
                                      QuoteMode quoteMode,
                                      Character commentMarker,
                                      Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces,
                                      boolean ignoreEmptyLines,
                                      String recordSeparator,
                                      String nullString,
                                      String[] header,
                                      boolean skipHeaderRecord,
                                      boolean allowMissingColumnNames) {
        CSVFormat format = CSVFormat.newFormat(delimiter);

        if (quoteCharacter != null) {
            format = format.withQuote(quoteCharacter);
        } else {
            format = format.withQuote((Character) null);
        }

        if (quoteMode != null) {
            format = format.withQuoteMode(quoteMode);
        }

        if (commentMarker != null) {
            format = format.withCommentMarker(commentMarker);
        }

        if (escapeCharacter != null) {
            format = format.withEscape(escapeCharacter);
        }

        format = format.withIgnoreSurroundingSpaces(ignoreSurroundingSpaces);
        format = format.withIgnoreEmptyLines(ignoreEmptyLines);

        if (recordSeparator != null) {
            format = format.withRecordSeparator(recordSeparator);
        }

        if (nullString != null) {
            format = format.withNullString(nullString);
        }

        if (header != null) {
            format = format.withHeader(header);
        }

        format = format.withSkipHeaderRecord(skipHeaderRecord);
        format = format.withAllowMissingColumnNames(allowMissingColumnNames);

        return format;
    }

    @Test
    @Timeout(8000)
    void testToString_Minimal() {
        CSVFormat format = createCSVFormat(',', null, null, null,
                null, false, false, null, null, null, false, false);
        String s = format.toString();
        assertTrue(s.contains("Delimiter=<,>"));
        assertFalse(s.contains("Escape=<"));
        assertFalse(s.contains("QuoteChar=<"));
        assertFalse(s.contains("CommentStart=<"));
        assertFalse(s.contains("NullString=<"));
        assertFalse(s.contains("RecordSeparator=<"));
        assertFalse(s.contains("EmptyLines:ignored"));
        assertFalse(s.contains("SurroundingSpaces:ignored"));
        assertTrue(s.contains("SkipHeaderRecord:false"));
        assertFalse(s.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() {
        String[] header = new String[]{"h1", "h2"};
        CSVFormat format = createCSVFormat(';', '"', QuoteMode.ALL, '#',
                '\\', true, true, "\r\n", "NULL", header, true, true);
        String s = format.toString();
        assertTrue(s.contains("Delimiter=<;>"));
        assertTrue(s.contains("Escape=<\\>"));
        assertTrue(s.contains("QuoteChar=<\">"));
        assertTrue(s.contains("CommentStart=<#>"));
        assertTrue(s.contains("NullString=<NULL>"));
        assertTrue(s.contains("RecordSeparator=<\r\n>"));
        assertTrue(s.contains("EmptyLines:ignored"));
        assertTrue(s.contains("SurroundingSpaces:ignored"));
        assertTrue(s.contains("SkipHeaderRecord:true"));
        assertTrue(s.contains("Header:" + Arrays.toString(header)));
    }

    @Test
    @Timeout(8000)
    void testToString_PartialFields() {
        String[] header = new String[]{"header"};
        CSVFormat format = createCSVFormat('|', null, null, null,
                'e', false, true, null, "nullStr", header, false, false);
        String s = format.toString();
        assertTrue(s.contains("Delimiter=<|>"));
        assertTrue(s.contains("Escape=<e>"));
        assertFalse(s.contains("QuoteChar=<"));
        assertFalse(s.contains("CommentStart=<"));
        assertTrue(s.contains("NullString=<nullStr>"));
        assertFalse(s.contains("RecordSeparator=<"));
        assertTrue(s.contains("EmptyLines:ignored"));
        assertFalse(s.contains("SurroundingSpaces:ignored"));
        assertTrue(s.contains("SkipHeaderRecord:false"));
        assertTrue(s.contains("Header:" + Arrays.toString(header)));
    }

    @Test
    @Timeout(8000)
    void testToString_RecordSeparatorEmptyString() {
        CSVFormat format = createCSVFormat(',', null, null, null,
                null, false, false, "", null, null, false, false);
        String s = format.toString();
        assertTrue(s.contains("RecordSeparator=<>"));
    }

    @Test
    @Timeout(8000)
    void testToString_HeaderNull() {
        CSVFormat format = createCSVFormat(',', null, null, null,
                null, false, false, null, null, null, false, false);
        String s = format.toString();
        assertFalse(s.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    void testToString_HeaderEmptyArray() {
        CSVFormat format = createCSVFormat(',', null, null, null,
                null, false, false, null, null, new String[0], false, false);
        String s = format.toString();
        assertTrue(s.contains("Header:[]"));
    }
}