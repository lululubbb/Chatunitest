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
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormatHashCodeTest {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testHashCode_DefaultInstance() {
        int expected = computeExpectedHashCode(csvFormat);
        assertEquals(expected, csvFormat.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_DifferentDelimiter() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "delimiter", (char) ';');
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullQuoteMode() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "quoteMode", null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NonNullQuoteCharacter() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "quoteCharacter", Character.valueOf('"'));
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullCommentMarker() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "commentMarker", null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NonNullEscapeCharacter() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "escapeCharacter", Character.valueOf('\\'));
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullStringSet() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "nullString", "NULL");
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_BooleanFlags() throws Exception {
        CSVFormat modified = csvFormat;
        modified = modifyField(modified, "ignoreSurroundingSpaces", true);
        modified = modifyField(modified, "ignoreHeaderCase", true);
        modified = modifyField(modified, "ignoreEmptyLines", true);
        modified = modifyField(modified, "skipHeaderRecord", true);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullRecordSeparator() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "recordSeparator", null);
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_HeaderArray() throws Exception {
        CSVFormat modified = modifyField(csvFormat, "header", new String[] {"col1", "col2"});
        int expected = computeExpectedHashCode(modified);
        assertEquals(expected, modified.hashCode());
    }

    // Helper method to modify private final fields via reflection and return modified instance
    private CSVFormat modifyField(CSVFormat original, String fieldName, Object value) throws Exception {
        // Create a new CSVFormat instance copying all fields from original
        // Because CSVFormat is immutable, we can clone via reflection or create new instance with constructor
        // For simplicity, use reflection to copy fields and set the target field to new value

        CSVFormat copy = cloneCSVFormat(original);

        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(copy, value);
        return copy;
    }

    private CSVFormat cloneCSVFormat(CSVFormat original) throws Exception {
        // Use reflection to get all fields and copy them to a new instance using constructor

        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
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
        Field autoFlushField = CSVFormat.class.getDeclaredField("autoFlush");

        delimiterField.setAccessible(true);
        quoteCharField.setAccessible(true);
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
        autoFlushField.setAccessible(true);

        char delimiter = delimiterField.getChar(original);
        Character quoteChar = (Character) quoteCharField.get(original);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(original);
        Character commentMarker = (Character) commentMarkerField.get(original);
        Character escapeCharacter = (Character) escapeCharacterField.get(original);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(original);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(original);
        String recordSeparator = (String) recordSeparatorField.get(original);
        String nullString = (String) nullStringField.get(original);
        Object[] headerComments = (Object[]) headerCommentsField.get(original);
        String[] header = (String[]) headerField.get(original);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(original);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(original);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(original);
        boolean trim = trimField.getBoolean(original);
        boolean trailingDelimiter = trailingDelimiterField.getBoolean(original);
        boolean autoFlush = autoFlushField.getBoolean(original);

        return new CSVFormat(delimiter, quoteChar, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter, autoFlush);
    }

    // Compute expected hash code based on CSVFormat fields according to hashCode() implementation
    private int computeExpectedHashCode(CSVFormat format) {
        final int prime = 31;
        int result = 1;

        try {
            Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
            Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
            Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
            Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
            Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
            Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
            Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
            Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
            Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
            Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
            Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
            Field headerField = CSVFormat.class.getDeclaredField("header");

            delimiterField.setAccessible(true);
            quoteModeField.setAccessible(true);
            quoteCharacterField.setAccessible(true);
            commentMarkerField.setAccessible(true);
            escapeCharacterField.setAccessible(true);
            nullStringField.setAccessible(true);
            ignoreSurroundingSpacesField.setAccessible(true);
            ignoreHeaderCaseField.setAccessible(true);
            ignoreEmptyLinesField.setAccessible(true);
            skipHeaderRecordField.setAccessible(true);
            recordSeparatorField.setAccessible(true);
            headerField.setAccessible(true);

            int delimiter = delimiterField.getChar(format);
            QuoteMode quoteMode = (QuoteMode) quoteModeField.get(format);
            Character quoteCharacter = (Character) quoteCharacterField.get(format);
            Character commentMarker = (Character) commentMarkerField.get(format);
            Character escapeCharacter = (Character) escapeCharacterField.get(format);
            String nullString = (String) nullStringField.get(format);
            boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(format);
            boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(format);
            boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(format);
            boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(format);
            String recordSeparator = (String) recordSeparatorField.get(format);
            String[] header = (String[]) headerField.get(format);

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
}