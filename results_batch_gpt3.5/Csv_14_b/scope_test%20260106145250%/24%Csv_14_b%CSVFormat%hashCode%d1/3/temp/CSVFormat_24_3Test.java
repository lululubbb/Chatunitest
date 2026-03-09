package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_24_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testHashCode_consistentForSameObject() {
        int hash1 = csvFormat.hashCode();
        int hash2 = csvFormat.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentForDifferentDelimiter() throws Exception {
        CSVFormat modified = withField(csvFormat, "delimiter", (char) (csvFormat.getDelimiter() + 1));
        assertNotEquals(csvFormat.hashCode(), modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentForDifferentQuoteMode() throws Exception {
        QuoteMode original = csvFormat.getQuoteMode();
        QuoteMode different = (original == null) ? QuoteMode.ALL : null;
        CSVFormat modified = withField(csvFormat, "quoteMode", different);
        assertNotEquals(csvFormat.hashCode(), modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentForDifferentQuoteCharacter() throws Exception {
        Character original = csvFormat.getQuoteCharacter();
        Character different = (original == null) ? '"' : null;
        CSVFormat modified = withField(csvFormat, "quoteCharacter", different);
        assertNotEquals(csvFormat.hashCode(), modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentForDifferentCommentMarker() throws Exception {
        Character original = csvFormat.getCommentMarker();
        Character different = (original == null) ? '#' : null;
        CSVFormat modified = withField(csvFormat, "commentMarker", different);
        assertNotEquals(csvFormat.hashCode(), modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentForDifferentEscapeCharacter() throws Exception {
        Character original = csvFormat.getEscapeCharacter();
        Character different = (original == null) ? '\\' : null;
        CSVFormat modified = withField(csvFormat, "escapeCharacter", different);
        assertNotEquals(csvFormat.hashCode(), modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentForDifferentNullString() throws Exception {
        String original = csvFormat.getNullString();
        String different = (original == null) ? "null" : null;
        CSVFormat modified = withField(csvFormat, "nullString", different);
        assertNotEquals(csvFormat.hashCode(), modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentForDifferentIgnoreSurroundingSpaces() throws Exception {
        boolean original = csvFormat.getIgnoreSurroundingSpaces();
        CSVFormat modified = withField(csvFormat, "ignoreSurroundingSpaces", !original);
        assertNotEquals(csvFormat.hashCode(), modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentForDifferentIgnoreHeaderCase() throws Exception {
        boolean original = csvFormat.getIgnoreHeaderCase();
        CSVFormat modified = withField(csvFormat, "ignoreHeaderCase", !original);
        assertNotEquals(csvFormat.hashCode(), modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentForDifferentIgnoreEmptyLines() throws Exception {
        boolean original = csvFormat.getIgnoreEmptyLines();
        CSVFormat modified = withField(csvFormat, "ignoreEmptyLines", !original);
        assertNotEquals(csvFormat.hashCode(), modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentForDifferentSkipHeaderRecord() throws Exception {
        boolean original = csvFormat.getSkipHeaderRecord();
        CSVFormat modified = withField(csvFormat, "skipHeaderRecord", !original);
        assertNotEquals(csvFormat.hashCode(), modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentForDifferentRecordSeparator() throws Exception {
        String original = csvFormat.getRecordSeparator();
        String different = (original == null) ? "\n" : null;
        CSVFormat modified = withField(csvFormat, "recordSeparator", different);
        assertNotEquals(csvFormat.hashCode(), modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentForDifferentHeader() throws Exception {
        String[] original = csvFormat.getHeader();
        String[] different = (original == null) ? new String[]{"A"} : new String[0];
        CSVFormat modified = withField(csvFormat, "header", different);
        assertNotEquals(csvFormat.hashCode(), modified.hashCode());
    }

    private CSVFormat withField(CSVFormat original, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Create a new CSVFormat instance copying all fields from original
        CSVFormat copy = copyCSVFormat(original);

        // Set the field to the new value on the copy
        field.set(copy, value);

        return copy;
    }

    private CSVFormat copyCSVFormat(CSVFormat original) throws Exception {
        // Use reflection to copy all fields to new instance
        // Find constructor with all args
        // CSVFormat constructor signature:
        // private CSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode, Character commentStart,
        // Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines, String recordSeparator,
        // String nullString, Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim, boolean trailingDelimiter)

        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        Field headerField = CSVFormat.class.getDeclaredField("header");
        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        Field trimField = CSVFormat.class.getDeclaredField("trim");
        Field trailingDelimiterField = CSVFormat.class.getDeclaredField("trailingDelimiter");

        delimiterField.setAccessible(true);
        quoteCharacterField.setAccessible(true);
        quoteModeField.setAccessible(true);
        commentMarkerField.setAccessible(true);
        escapeCharacterField.setAccessible(true);
        ignoreSurroundingSpacesField.setAccessible(true);
        ignoreEmptyLinesField.setAccessible(true);
        recordSeparatorField.setAccessible(true);
        nullStringField.setAccessible(true);
        headerCommentsField.setAccessible(true);
        headerField.setAccessible(true);
        skipHeaderRecordField.setAccessible(true);
        allowMissingColumnNamesField.setAccessible(true);
        ignoreHeaderCaseField.setAccessible(true);
        trimField.setAccessible(true);
        trailingDelimiterField.setAccessible(true);

        char delimiter = (char) delimiterField.get(original);
        Character quoteCharacter = (Character) quoteCharacterField.get(original);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(original);
        Character commentMarker = (Character) commentMarkerField.get(original);
        Character escapeCharacter = (Character) escapeCharacterField.get(original);
        boolean ignoreSurroundingSpaces = (boolean) ignoreSurroundingSpacesField.get(original);
        boolean ignoreEmptyLines = (boolean) ignoreEmptyLinesField.get(original);
        String recordSeparator = (String) recordSeparatorField.get(original);
        String nullString = (String) nullStringField.get(original);
        Object[] headerComments = (Object[]) headerCommentsField.get(original);
        String[] header = (String[]) headerField.get(original);
        boolean skipHeaderRecord = (boolean) skipHeaderRecordField.get(original);
        boolean allowMissingColumnNames = (boolean) allowMissingColumnNamesField.get(original);
        boolean ignoreHeaderCase = (boolean) ignoreHeaderCaseField.get(original);
        boolean trim = (boolean) trimField.get(original);
        boolean trailingDelimiter = (boolean) trailingDelimiterField.get(original);

        // Use constructor to create new instance
        return createCSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter);
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, Object[] headerComments,
                                      String[] header, boolean skipHeaderRecord,
                                      boolean allowMissingColumnNames, boolean ignoreHeaderCase,
                                      boolean trim, boolean trailingDelimiter) throws Exception {
        // CSVFormat constructor is private, use reflection to invoke it
        var constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return (CSVFormat) constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter);
    }
}