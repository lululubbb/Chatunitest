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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

import java.lang.reflect.Field;
import java.util.Arrays;

class CSVFormatHashCodeTest {

    @Test
    @Timeout(8000)
    void testHashCode_defaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_allFieldsSet() throws Exception {
        CSVFormat format = createCSVFormatWithAllFieldsSet();
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_nullFields() throws Exception {
        CSVFormat format = createCSVFormatWithNullFields();
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_booleanFlagsVariations() throws Exception {
        CSVFormat format1 = createCSVFormatWithBooleanFlags(true, true, true, true);
        CSVFormat format2 = createCSVFormatWithBooleanFlags(false, false, false, false);
        assertNotEquals(format1.hashCode(), format2.hashCode());
    }

    private CSVFormat createCSVFormatWithAllFieldsSet() throws Exception {
        CSVFormat format = cloneCSVFormat(CSVFormat.DEFAULT);

        setField(format, "delimiter", (char) 42); // '*'
        setField(format, "quoteMode", QuoteMode.ALL);
        setField(format, "quoteCharacter", Character.valueOf('Q'));
        setField(format, "commentMarker", Character.valueOf('#'));
        setField(format, "escapeCharacter", Character.valueOf('\\'));
        setField(format, "nullString", "NULL");
        setField(format, "ignoreSurroundingSpaces", true);
        setField(format, "ignoreHeaderCase", true);
        setField(format, "ignoreEmptyLines", true);
        setField(format, "skipHeaderRecord", true);
        setField(format, "recordSeparator", "\n");
        setField(format, "header", new String[]{"h1", "h2"});

        return format;
    }

    private CSVFormat createCSVFormatWithNullFields() throws Exception {
        CSVFormat format = cloneCSVFormat(CSVFormat.DEFAULT);

        setField(format, "delimiter", (char) 0);
        setField(format, "quoteMode", null);
        setField(format, "quoteCharacter", null);
        setField(format, "commentMarker", null);
        setField(format, "escapeCharacter", null);
        setField(format, "nullString", null);
        setField(format, "ignoreSurroundingSpaces", false);
        setField(format, "ignoreHeaderCase", false);
        setField(format, "ignoreEmptyLines", false);
        setField(format, "skipHeaderRecord", false);
        setField(format, "recordSeparator", null);
        setField(format, "header", null);

        return format;
    }

    private CSVFormat createCSVFormatWithBooleanFlags(boolean ignoreSurroundingSpaces,
                                                      boolean ignoreHeaderCase,
                                                      boolean ignoreEmptyLines,
                                                      boolean skipHeaderRecord) throws Exception {
        CSVFormat format = cloneCSVFormat(CSVFormat.DEFAULT);

        setField(format, "ignoreSurroundingSpaces", ignoreSurroundingSpaces);
        setField(format, "ignoreHeaderCase", ignoreHeaderCase);
        setField(format, "ignoreEmptyLines", ignoreEmptyLines);
        setField(format, "skipHeaderRecord", skipHeaderRecord);

        return format;
    }

    private CSVFormat cloneCSVFormat(CSVFormat original) throws Exception {
        // Create a new instance by copying all fields from original
        CSVFormat copy = (CSVFormat) deepClone(original);
        return copy;
    }

    private Object deepClone(Object obj) throws Exception {
        // Since CSVFormat is immutable and final fields exist,
        // create a new instance via reflection using the constructor and all fields.
        Class<?> clazz = obj.getClass();

        Field delimiterField = clazz.getDeclaredField("delimiter");
        Field quoteCharacterField = clazz.getDeclaredField("quoteCharacter");
        Field quoteModeField = clazz.getDeclaredField("quoteMode");
        Field commentMarkerField = clazz.getDeclaredField("commentMarker");
        Field escapeCharacterField = clazz.getDeclaredField("escapeCharacter");
        Field ignoreSurroundingSpacesField = clazz.getDeclaredField("ignoreSurroundingSpaces");
        Field ignoreEmptyLinesField = clazz.getDeclaredField("ignoreEmptyLines");
        Field recordSeparatorField = clazz.getDeclaredField("recordSeparator");
        Field nullStringField = clazz.getDeclaredField("nullString");
        Field headerCommentsField = clazz.getDeclaredField("headerComments");
        Field headerField = clazz.getDeclaredField("header");
        Field skipHeaderRecordField = clazz.getDeclaredField("skipHeaderRecord");
        Field allowMissingColumnNamesField = clazz.getDeclaredField("allowMissingColumnNames");
        Field ignoreHeaderCaseField = clazz.getDeclaredField("ignoreHeaderCase");
        Field trimField = clazz.getDeclaredField("trim");
        Field trailingDelimiterField = clazz.getDeclaredField("trailingDelimiter");

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

        char delimiter = delimiterField.getChar(obj);
        Character quoteCharacter = (Character) quoteCharacterField.get(obj);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(obj);
        Character commentMarker = (Character) commentMarkerField.get(obj);
        Character escapeCharacter = (Character) escapeCharacterField.get(obj);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(obj);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(obj);
        String recordSeparator = (String) recordSeparatorField.get(obj);
        String nullString = (String) nullStringField.get(obj);
        Object[] headerComments = (Object[]) headerCommentsField.get(obj);
        String[] header = (String[]) headerField.get(obj);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(obj);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(obj);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(obj);
        boolean trim = trimField.getBoolean(obj);
        boolean trailingDelimiter = trailingDelimiterField.getBoolean(obj);

        // Use constructor
        java.lang.reflect.Constructor<?> ctor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class,
                boolean.class, boolean.class, boolean.class);

        ctor.setAccessible(true);

        return ctor.newInstance(
                delimiter,
                quoteCharacter,
                quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                headerComments,
                header,
                skipHeaderRecord,
                allowMissingColumnNames,
                ignoreHeaderCase,
                trim,
                trailingDelimiter
        );
    }

    private int computeExpectedHashCode(CSVFormat format) {
        final int prime = 31;
        int result = 1;

        char delimiter = getField(format, "delimiter");
        QuoteMode quoteMode = getField(format, "quoteMode");
        Character quoteCharacter = getField(format, "quoteCharacter");
        Character commentMarker = getField(format, "commentMarker");
        Character escapeCharacter = getField(format, "escapeCharacter");
        String nullString = getField(format, "nullString");
        boolean ignoreSurroundingSpaces = getField(format, "ignoreSurroundingSpaces");
        boolean ignoreHeaderCase = getField(format, "ignoreHeaderCase");
        boolean ignoreEmptyLines = getField(format, "ignoreEmptyLines");
        boolean skipHeaderRecord = getField(format, "skipHeaderRecord");
        String recordSeparator = getField(format, "recordSeparator");
        String[] header = getField(format, "header");

        result = prime * result + delimiter;
        result = prime * result + (quoteMode == null ? 0 : quoteMode.hashCode());
        result = prime * result + (quoteCharacter == null ? 0 : quoteCharacter.hashCode());
        result = prime * result + (commentMarker == null ? 0 : commentMarker.hashCode());
        result = prime * result + (escapeCharacter == null ? 0 : escapeCharacter.hashCode());
        result = prime * result + (nullString == null ? 0 : nullString.hashCode());
        result = prime * result + (ignoreSurroundingSpaces ? 1231 : 1237);
        result = prime * result + (ignoreHeaderCase ? 1231 : 1237);
        result = prime * result + (ignoreEmptyLines ? 1231 : 1237);
        result = prime * result + (skipHeaderRecord ? 1231 : 1237);
        result = prime * result + (recordSeparator == null ? 0 : recordSeparator.hashCode());
        result = prime * result + Arrays.hashCode(header);

        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> T getField(CSVFormat format, String fieldName) {
        try {
            Field field = CSVFormat.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(format);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(CSVFormat format, String fieldName, Object value) {
        try {
            Field field = CSVFormat.class.getDeclaredField(fieldName);
            field.setAccessible(true);

            // Remove final modifier via reflection if necessary
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

            field.set(format, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}