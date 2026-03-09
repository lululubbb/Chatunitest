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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

class CSVFormatToStringTest {

    private CSVFormat createCSVFormatWithFields(
            char delimiter,
            Character escapeCharacter,
            Character quoteCharacter,
            Character commentMarker,
            String nullString,
            String recordSeparator,
            boolean ignoreEmptyLines,
            boolean ignoreSurroundingSpaces,
            boolean ignoreHeaderCase,
            boolean skipHeaderRecord,
            String[] headerComments,
            String[] header) throws Exception {

        // Use the DEFAULT and override fields by reflection
        CSVFormat format = CSVFormat.DEFAULT;

        // Set final fields via reflection
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        delimiterField.setChar(format, delimiter);

        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        escapeCharacterField.set(format, escapeCharacter);

        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        quoteCharacterField.set(format, quoteCharacter);

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        commentMarkerField.set(format, commentMarker);

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        nullStringField.set(format, nullString);

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        recordSeparatorField.set(format, recordSeparator);

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        ignoreEmptyLinesField.setBoolean(format, ignoreEmptyLines);

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        ignoreSurroundingSpacesField.setBoolean(format, ignoreSurroundingSpaces);

        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        ignoreHeaderCaseField.setBoolean(format, ignoreHeaderCase);

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        skipHeaderRecordField.setBoolean(format, skipHeaderRecord);

        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        headerCommentsField.set(format, headerComments);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        headerField.set(format, header);

        return format;
    }

    @Test
    @Timeout(8000)
    void testToStringDefault() {
        String s = CSVFormat.DEFAULT.toString();
        assertTrue(s.contains("Delimiter=<,>"));
        assertTrue(s.contains("QuoteChar=<\""));
        assertTrue(s.contains("RecordSeparator=<\r\n>"));
        assertTrue(s.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    void testToStringAllFieldsSet() throws Exception {
        String[] headers = {"h1", "h2"};
        String[] headerComments = {"comment1", "comment2"};

        CSVFormat format = createCSVFormatWithFields(
                ';',
                '\\',
                '\'',
                '#',
                "NULL",
                "\n",
                true,
                true,
                true,
                true,
                headerComments,
                headers);

        String s = format.toString();

        assertTrue(s.contains("Delimiter=<;>"));
        assertTrue(s.contains("Escape=<\\>"));
        assertTrue(s.contains("QuoteChar=<'>'"));
        assertTrue(s.contains("CommentStart=<#>"));
        assertTrue(s.contains("NullString=<NULL>"));
        assertTrue(s.contains("RecordSeparator=<\n>"));
        assertTrue(s.contains("EmptyLines:ignored"));
        assertTrue(s.contains("SurroundingSpaces:ignored"));
        assertTrue(s.contains("IgnoreHeaderCase:ignored"));
        assertTrue(s.contains("SkipHeaderRecord:true"));
        assertTrue(s.contains("HeaderComments:" + Arrays.toString(headerComments)));
        assertTrue(s.contains("Header:" + Arrays.toString(headers)));
    }

    @Test
    @Timeout(8000)
    void testToStringNoOptionalFields() throws Exception {
        CSVFormat format = createCSVFormatWithFields(
                ',',
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                null,
                null);

        String s = format.toString();

        assertEquals("Delimiter=<,> SkipHeaderRecord:false", s);
    }

    @Test
    @Timeout(8000)
    void testToStringPartialFields() throws Exception {
        CSVFormat format = createCSVFormatWithFields(
                '|',
                null,
                '"',
                null,
                null,
                "\r",
                false,
                true,
                false,
                false,
                null,
                null);

        String s = format.toString();

        assertTrue(s.contains("Delimiter=<|>"));
        assertFalse(s.contains("Escape=<"));
        assertTrue(s.contains("QuoteChar=<\""));
        assertFalse(s.contains("CommentStart=<"));
        assertFalse(s.contains("NullString=<"));
        assertTrue(s.contains("RecordSeparator=<\r>"));
        assertFalse(s.contains("EmptyLines:ignored"));
        assertTrue(s.contains("SurroundingSpaces:ignored"));
        assertFalse(s.contains("IgnoreHeaderCase:ignored"));
        assertTrue(s.contains("SkipHeaderRecord:false"));
    }
}