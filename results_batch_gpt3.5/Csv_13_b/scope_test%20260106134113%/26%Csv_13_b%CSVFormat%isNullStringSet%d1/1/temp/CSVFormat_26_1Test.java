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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormatIsNullStringSetTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        // Use the DEFAULT instance as base
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSet_whenNullStringIsNull() {
        // nullString is null in DEFAULT instance, verify isNullStringSet returns false
        assertFalse(csvFormat.isNullStringSet());
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSet_whenNullStringIsNotNull() throws Exception {
        // Create a new CSVFormat instance with non-null nullString using reflection
        CSVFormat formatWithNullString = createCSVFormatWithNullString("NULL");

        assertTrue(formatWithNullString.isNullStringSet());
    }

    private CSVFormat createCSVFormatWithNullString(String nullString) throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;

        Constructor<CSVFormat> ctor = clazz.getDeclaredConstructor(
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
                boolean.class
        );
        ctor.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        Field delimiterField = clazz.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(defaultFormat);

        Field quoteCharacterField = clazz.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        Character quoteCharacter = (Character) quoteCharacterField.get(defaultFormat);

        Field quoteModeField = clazz.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(defaultFormat);

        Field commentMarkerField = clazz.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(defaultFormat);

        Field escapeCharacterField = clazz.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeCharacter = (Character) escapeCharacterField.get(defaultFormat);

        Field ignoreSurroundingSpacesField = clazz.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(defaultFormat);

        Field ignoreEmptyLinesField = clazz.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(defaultFormat);

        Field recordSeparatorField = clazz.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(defaultFormat);

        Field headerCommentsField = clazz.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Object[] headerComments = (Object[]) headerCommentsField.get(defaultFormat);

        Field headerField = clazz.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(defaultFormat);

        Field skipHeaderRecordField = clazz.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(defaultFormat);

        Field allowMissingColumnNamesField = clazz.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(defaultFormat);

        Field ignoreHeaderCaseField = clazz.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(defaultFormat);

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
                ignoreHeaderCase
        );
    }
}