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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_22_5Test {

    private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSet_whenNullStringIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("someValue");

        CSVFormat modifiedFormat = createCSVFormatWithNullString(format, null);

        boolean result = modifiedFormat.isNullStringSet();
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSet_whenNullStringIsNotNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");

        CSVFormat modifiedFormat = createCSVFormatWithNullString(format, "NULL");

        boolean result = modifiedFormat.isNullStringSet();
        assertTrue(result);
    }

    private static CSVFormat createCSVFormatWithNullString(CSVFormat original, String nullString) throws Exception {
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        Field headerField = CSVFormat.class.getDeclaredField("header");
        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");

        delimiterField.setAccessible(true);
        quoteCharacterField.setAccessible(true);
        quoteModeField.setAccessible(true);
        commentMarkerField.setAccessible(true);
        escapeCharacterField.setAccessible(true);
        ignoreSurroundingSpacesField.setAccessible(true);
        allowMissingColumnNamesField.setAccessible(true);
        ignoreEmptyLinesField.setAccessible(true);
        recordSeparatorField.setAccessible(true);
        headerField.setAccessible(true);
        skipHeaderRecordField.setAccessible(true);

        char delimiter = delimiterField.getChar(original);
        Character quoteCharacter = (Character) quoteCharacterField.get(original);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(original);
        Character commentMarker = (Character) commentMarkerField.get(original);
        Character escapeCharacter = (Character) escapeCharacterField.get(original);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(original);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(original);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(original);
        String recordSeparator = (String) recordSeparatorField.get(original);
        String[] header = (String[]) headerField.get(original);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(original);

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter,
                quoteCharacter,
                quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                allowMissingColumnNames,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                header,
                skipHeaderRecord);
    }
}