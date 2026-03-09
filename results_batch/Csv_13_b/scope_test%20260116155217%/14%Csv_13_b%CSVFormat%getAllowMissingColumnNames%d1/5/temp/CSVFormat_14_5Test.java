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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

class CSVFormat_14_5Test {

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_DefaultFalse() throws Exception {
        CSVFormat format = createCSVFormatWithAllowMissingColumnNames(false);
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        assertTrue(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_ExcelConstant() {
        CSVFormat format = CSVFormat.EXCEL;
        assertTrue(format.getAllowMissingColumnNames());
    }

    private static CSVFormat createCSVFormatWithAllowMissingColumnNames(boolean value) throws Exception {
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

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        char delimiter = delimiterField.getChar(defaultFormat);
        Character quoteCharacter = (Character) quoteCharacterField.get(defaultFormat);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(defaultFormat);
        Character commentMarker = (Character) commentMarkerField.get(defaultFormat);
        Character escapeCharacter = (Character) escapeCharacterField.get(defaultFormat);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(defaultFormat);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(defaultFormat);
        String recordSeparator = (String) recordSeparatorField.get(defaultFormat);
        String nullString = (String) nullStringField.get(defaultFormat);
        Object[] headerComments = (Object[]) headerCommentsField.get(defaultFormat);
        String[] header = (String[]) headerField.get(defaultFormat);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(defaultFormat);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(defaultFormat);

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);

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
                headerComments,
                header,
                skipHeaderRecord,
                value,
                ignoreHeaderCase);
    }
}