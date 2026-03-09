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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class CSVFormat_18_5Test {

    @Test
    @Timeout(8000)
    void testGetNullString_DefaultIsNull() {
        // Using DEFAULT constant - nullString should be null
        CSVFormat format = CSVFormat.DEFAULT;
        assertNull(format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullStringSet() throws Exception {
        // Create CSVFormat instance via reflection to set private final nullString field
        CSVFormat format = createCSVFormatWithNullString("NULL_VALUE");
        assertEquals("NULL_VALUE", format.getNullString());
    }

    private CSVFormat createCSVFormatWithNullString(String nullStringValue) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class,
                String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(defaultFormat);

        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharField.setAccessible(true);
        Character quoteChar = (Character) quoteCharField.get(defaultFormat);

        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(defaultFormat);

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(defaultFormat);

        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeCharacter = (Character) escapeCharacterField.get(defaultFormat);

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(defaultFormat);

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(defaultFormat);

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(defaultFormat);

        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Object[] headerComments = (Object[]) headerCommentsField.get(defaultFormat);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(defaultFormat);

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(defaultFormat);

        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(defaultFormat);

        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(defaultFormat);

        return constructor.newInstance(delimiter, quoteChar, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullStringValue,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }
}