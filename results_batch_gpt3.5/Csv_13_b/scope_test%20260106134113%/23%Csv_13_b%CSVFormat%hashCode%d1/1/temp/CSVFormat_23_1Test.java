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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatHashCodeTest {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatModified;

    @BeforeEach
    void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;
        csvFormatModified = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withQuoteMode(QuoteMode.ALL)
                .withQuote('\'')
                .withCommentMarker('#')
                .withEscape('\\')
                .withNullString("NULL")
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(true)
                .withIgnoreEmptyLines(true)
                .withSkipHeaderRecord(true)
                .withRecordSeparator("\n")
                .withHeader("a", "b", "c");
    }

    @Test
    @Timeout(8000)
    void testHashCode_consistency() {
        int hash1 = csvFormatDefault.hashCode();
        int hash2 = csvFormatDefault.hashCode();
        assertEquals(hash1, hash2, "hashCode should be consistent across multiple invocations");
    }

    @Test
    @Timeout(8000)
    void testHashCode_equalObjects() throws Exception {
        CSVFormat copy = copyCSVFormat(csvFormatDefault);
        assertEquals(csvFormatDefault.hashCode(), copy.hashCode(), "Equal objects must have same hashCode");
    }

    @Test
    @Timeout(8000)
    void testHashCode_differentDelimiter() {
        CSVFormat changed = csvFormatDefault.withDelimiter(';');
        assertNotEquals(csvFormatDefault.hashCode(), changed.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_differentQuoteMode() {
        CSVFormat changed = csvFormatDefault.withQuoteMode(QuoteMode.ALL);
        assertNotEquals(csvFormatDefault.hashCode(), changed.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_differentQuoteCharacter() {
        CSVFormat changed = csvFormatDefault.withQuote('\'');
        assertNotEquals(csvFormatDefault.hashCode(), changed.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_differentCommentMarker() {
        CSVFormat changed = csvFormatDefault.withCommentMarker('#');
        assertNotEquals(csvFormatDefault.hashCode(), changed.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_differentEscapeCharacter() {
        CSVFormat changed = csvFormatDefault.withEscape('\\');
        assertNotEquals(csvFormatDefault.hashCode(), changed.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_differentNullString() {
        CSVFormat changed = csvFormatDefault.withNullString("NULL");
        assertNotEquals(csvFormatDefault.hashCode(), changed.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_ignoreSurroundingSpacesTrueFalse() throws Exception {
        CSVFormat changed = csvFormatDefault.withIgnoreSurroundingSpaces(!csvFormatDefault.getIgnoreSurroundingSpaces());
        assertNotEquals(csvFormatDefault.hashCode(), changed.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_ignoreHeaderCaseTrueFalse() throws Exception {
        CSVFormat changed = csvFormatDefault.withIgnoreHeaderCase(!csvFormatDefault.getIgnoreHeaderCase());
        assertNotEquals(csvFormatDefault.hashCode(), changed.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_ignoreEmptyLinesTrueFalse() throws Exception {
        CSVFormat changed = csvFormatDefault.withIgnoreEmptyLines(!csvFormatDefault.getIgnoreEmptyLines());
        assertNotEquals(csvFormatDefault.hashCode(), changed.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_skipHeaderRecordTrueFalse() throws Exception {
        CSVFormat changed = csvFormatDefault.withSkipHeaderRecord(!csvFormatDefault.getSkipHeaderRecord());
        assertNotEquals(csvFormatDefault.hashCode(), changed.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_differentRecordSeparator() {
        CSVFormat changed = csvFormatDefault.withRecordSeparator("\n");
        assertNotEquals(csvFormatDefault.hashCode(), changed.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_differentHeader() {
        CSVFormat changed = csvFormatDefault.withHeader("x", "y");
        assertNotEquals(csvFormatDefault.hashCode(), changed.hashCode());
    }

    private CSVFormat copyCSVFormat(CSVFormat original) throws Exception {
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        Field headerField = CSVFormat.class.getDeclaredField("header");
        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");

        delimiterField.setAccessible(true);
        quoteCharacterField.setAccessible(true);
        quoteModeField.setAccessible(true);
        commentMarkerField.setAccessible(true);
        escapeCharacterField.setAccessible(true);
        ignoreSurroundingSpacesField.setAccessible(true);
        ignoreEmptyLinesField.setAccessible(true);
        recordSeparatorField.setAccessible(true);
        nullStringField.setAccessible(true);
        headerField.setAccessible(true);
        skipHeaderRecordField.setAccessible(true);
        ignoreHeaderCaseField.setAccessible(true);
        allowMissingColumnNamesField.setAccessible(true);

        char delimiter = delimiterField.getChar(original);
        Character quoteCharacter = (Character) quoteCharacterField.get(original);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(original);
        Character commentMarker = (Character) commentMarkerField.get(original);
        Character escapeCharacter = (Character) escapeCharacterField.get(original);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(original);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(original);
        String recordSeparator = (String) recordSeparatorField.get(original);
        String nullString = (String) nullStringField.get(original);
        String[] header = (String[]) headerField.get(original);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(original);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(original);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(original);

        // Use reflection to find and invoke the private constructor
        Class<CSVFormat> clazz = CSVFormat.class;
        Method newInstanceMethod = null;
        try {
            // Try to get the private constructor via reflection
            var constructor = clazz.getDeclaredConstructor(
                    char.class,
                    Character.class,
                    QuoteMode.class,
                    Character.class,
                    Character.class,
                    boolean.class,
                    boolean.class,
                    String.class,
                    String.class,
                    Object[].class,
                    String[].class,
                    boolean.class,
                    boolean.class,
                    boolean.class);
            constructor.setAccessible(true);
            return constructor.newInstance(
                    delimiter,
                    quoteCharacter,
                    quoteMode,
                    commentMarker,
                    escapeCharacter,
                    ignoreSurroundingSpaces,
                    ignoreEmptyLines,
                    recordSeparator,
                    nullString,
                    null,
                    header,
                    skipHeaderRecord,
                    allowMissingColumnNames,
                    ignoreHeaderCase);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find the private constructor of CSVFormat", e);
        }
    }
}