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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;
import java.util.Arrays;

class CSVFormatHashCodeTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testHashCode_DefaultInstance() {
        int expected = computeHashCode(csvFormat);
        assertEquals(expected, csvFormat.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_DifferentDelimiter() throws Exception {
        CSVFormat format = cloneCSVFormat(csvFormat);
        setField(format, "delimiter", (char) (format.getDelimiter() + 1));
        int expected = computeHashCode(format);
        assertEquals(expected, format.hashCode());
        assertNotEquals(csvFormat.hashCode(), format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_QuoteModeNull() throws Exception {
        CSVFormat format = cloneCSVFormat(csvFormat);
        setField(format, "quoteMode", null);
        int expected = computeHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_QuoteCharacterNull() throws Exception {
        CSVFormat format = cloneCSVFormat(csvFormat);
        setField(format, "quoteCharacter", null);
        int expected = computeHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_CommentMarkerNull() throws Exception {
        CSVFormat format = cloneCSVFormat(csvFormat);
        setField(format, "commentMarker", null);
        int expected = computeHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_EscapeCharacterNull() throws Exception {
        CSVFormat format = cloneCSVFormat(csvFormat);
        setField(format, "escapeCharacter", null);
        int expected = computeHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_NullStringNull() throws Exception {
        CSVFormat format = cloneCSVFormat(csvFormat);
        setField(format, "nullString", null);
        int expected = computeHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_BooleanFields() throws Exception {
        CSVFormat format = cloneCSVFormat(csvFormat);
        setField(format, "ignoreSurroundingSpaces", true);
        setField(format, "ignoreHeaderCase", false);
        setField(format, "ignoreEmptyLines", true);
        setField(format, "skipHeaderRecord", false);
        int expected = computeHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_RecordSeparatorNull() throws Exception {
        CSVFormat format = cloneCSVFormat(csvFormat);
        setField(format, "recordSeparator", null);
        int expected = computeHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_HeaderArray() throws Exception {
        CSVFormat format = cloneCSVFormat(csvFormat);
        setField(format, "header", new String[]{"A", "B", "C"});
        int expected = computeHashCode(format);
        assertEquals(expected, format.hashCode());

        setField(format, "header", null);
        expected = computeHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    // Helper method to compute hashCode as per focal method logic
    private int computeHashCode(CSVFormat format) {
        final int prime = 31;
        int result = 1;

        try {
            char delimiter = (char) getField(format, "delimiter");
            QuoteMode quoteMode = (QuoteMode) getField(format, "quoteMode");
            Character quoteCharacter = (Character) getField(format, "quoteCharacter");
            Character commentMarker = (Character) getField(format, "commentMarker");
            Character escapeCharacter = (Character) getField(format, "escapeCharacter");
            String nullString = (String) getField(format, "nullString");
            boolean ignoreSurroundingSpaces = (boolean) getField(format, "ignoreSurroundingSpaces");
            boolean ignoreHeaderCase = (boolean) getField(format, "ignoreHeaderCase");
            boolean ignoreEmptyLines = (boolean) getField(format, "ignoreEmptyLines");
            boolean skipHeaderRecord = (boolean) getField(format, "skipHeaderRecord");
            String recordSeparator = (String) getField(format, "recordSeparator");
            String[] header = (String[]) getField(format, "header");

            result = prime * result + delimiter;
            result = prime * result + ((quoteMode == null) ? 0 : quoteMode.hashCode());
            result = prime * result + ((quoteCharacter == null) ? 0 : quoteCharacter.hashCode());
            result = prime * result + ((commentMarker == null) ? 0 : commentMarker.hashCode());
            result = prime * result + ((escapeCharacter == null) ? 0 : escapeCharacter.hashCode());
            result = prime * result + ((nullString == null) ? 0 : nullString.hashCode());
            result = prime * result + (ignoreSurroundingSpaces ? 1231 : 1237);
            result = prime * result + (ignoreHeaderCase ? 1231 : 1237);
            result = prime * result + (ignoreEmptyLines ? 1231 : 1237);
            result = prime * result + (skipHeaderRecord ? 1231 : 1237);
            result = prime * result + ((recordSeparator == null) ? 0 : recordSeparator.hashCode());
            result = prime * result + Arrays.hashCode(header);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // Helper method to get private field value via reflection
    private Object getField(Object obj, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    // Helper method to set private field value via reflection
    private void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    // Helper method to clone CSVFormat instance by copying all fields via reflection
    private CSVFormat cloneCSVFormat(CSVFormat original) throws Exception {
        // Using the constructor since fields are final, but constructor is private
        // Use reflection to invoke private constructor
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = (char) delimiterField.get(original);

        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharField.setAccessible(true);
        Character quoteChar = (Character) quoteCharField.get(original);

        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(original);

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(original);

        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeCharacter = (Character) escapeCharacterField.get(original);

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = (boolean) ignoreSurroundingSpacesField.get(original);

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = (boolean) ignoreEmptyLinesField.get(original);

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(original);

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullString = (String) nullStringField.get(original);

        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Object[] headerComments = (Object[]) headerCommentsField.get(original);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(original);

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = (boolean) skipHeaderRecordField.get(original);

        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = (boolean) allowMissingColumnNamesField.get(original);

        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = (boolean) ignoreHeaderCaseField.get(original);

        Field trimField = CSVFormat.class.getDeclaredField("trim");
        trimField.setAccessible(true);
        boolean trim = (boolean) trimField.get(original);

        Field trailingDelimiterField = CSVFormat.class.getDeclaredField("trailingDelimiter");
        trailingDelimiterField.setAccessible(true);
        boolean trailingDelimiter = (boolean) trailingDelimiterField.get(original);

        Field autoFlushField = CSVFormat.class.getDeclaredField("autoFlush");
        autoFlushField.setAccessible(true);
        boolean autoFlush = (boolean) autoFlushField.get(original);

        // Get private constructor
        var constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class,
                boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter, quoteChar, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter, autoFlush);
    }
}