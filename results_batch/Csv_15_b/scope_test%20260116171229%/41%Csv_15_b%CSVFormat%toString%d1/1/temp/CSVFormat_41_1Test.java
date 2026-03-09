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
import org.apache.commons.csv.QuoteMode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

class CSVFormatToStringTest {

    private CSVFormat createCSVFormatWithFields(Character escapeCharacter, Character quoteCharacter,
                                                Character commentMarker, String nullString,
                                                String recordSeparator, boolean ignoreEmptyLines,
                                                boolean ignoreSurroundingSpaces, boolean ignoreHeaderCase,
                                                boolean skipHeaderRecord, String[] headerComments,
                                                String[] header) throws Exception {
        // Use DEFAULT as base
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to create a new instance with all fields set, since CSVFormat is final and fields are final
        // Instead, create a new CSVFormat using withXXX methods to set fields where possible
        format = format.withEscape(escapeCharacter)
                .withQuote(quoteCharacter)
                .withCommentMarker(commentMarker)
                .withNullString(nullString)
                .withRecordSeparator(recordSeparator)
                .withIgnoreEmptyLines(ignoreEmptyLines)
                .withIgnoreSurroundingSpaces(ignoreSurroundingSpaces)
                .withIgnoreHeaderCase(ignoreHeaderCase)
                .withSkipHeaderRecord(skipHeaderRecord);

        // headerComments and header are final fields, no withHeaderComments method taking String[] directly.
        // withHeaderComments(Object...) exists, use that for headerComments
        if (headerComments != null) {
            format = format.withHeaderComments((Object[]) headerComments);
        }
        if (header != null) {
            format = format.withHeader(header);
        }
        return format;
    }

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        String[] headerComments = new String[]{"comment1", "comment2"};
        String[] header = new String[]{"h1", "h2"};

        CSVFormat format = createCSVFormatWithFields('e', 'q', '#', "NULL", "\n",
                true, true, true,
                true, headerComments, header);

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<,>"));
        assertTrue(str.contains("Escape=<e>"));
        assertTrue(str.contains("QuoteChar=<q>"));
        assertTrue(str.contains("CommentStart=<#>"));
        assertTrue(str.contains("NullString=<NULL>"));
        assertTrue(str.contains("RecordSeparator=<\n>"));
        assertTrue(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("IgnoreHeaderCase:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:true"));
        assertTrue(str.contains("HeaderComments:" + Arrays.toString(headerComments)));
        assertTrue(str.contains("Header:" + Arrays.toString(header)));
    }

    @Test
    @Timeout(8000)
    void testToString_NoOptionalFields() throws Exception {
        CSVFormat format = createCSVFormatWithFields(null, null, null, null, null,
                false, false, false,
                false, null, null);

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
    void testToString_PartialFieldsSet() throws Exception {
        String[] header = new String[]{"header1"};

        CSVFormat format = createCSVFormatWithFields(null, '"', null, null, "\r\n",
                false, true, false,
                false, null, header);

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<,>"));
        assertFalse(str.contains("Escape=<"));
        assertTrue(str.contains("QuoteChar=<\""));
        assertFalse(str.contains("CommentStart=<"));
        assertFalse(str.contains("NullString=<"));
        assertTrue(str.contains("RecordSeparator=<\r\n>"));
        assertFalse(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains("SurroundingSpaces:ignored"));
        assertFalse(str.contains("IgnoreHeaderCase:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:false"));
        assertFalse(str.contains("HeaderComments:"));
        assertTrue(str.contains("Header:" + Arrays.toString(header)));
    }

    @Test
    @Timeout(8000)
    void testToString_SkipHeaderRecordTrue() throws Exception {
        CSVFormat format = createCSVFormatWithFields(null, null, null, null, null,
                false, false, false,
                true, null, null);

        String str = format.toString();

        assertTrue(str.contains("SkipHeaderRecord:true"));
    }

    @Test
    @Timeout(8000)
    void testToString_RecordSeparatorNull() throws Exception {
        CSVFormat format = createCSVFormatWithFields(null, null, null, null, null,
                false, false, false,
                false, null, null);

        String str = format.toString();

        assertFalse(str.contains("RecordSeparator=<"));
    }
}