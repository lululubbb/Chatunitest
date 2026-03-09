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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormat_8_5Test {

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_withAllowMissingColumnNames_true() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        assertTrue(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_withAllowMissingColumnNames_false() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_preservesValue() {
        CSVFormat formatTrue = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        assertTrue(formatTrue.getAllowMissingColumnNames());

        CSVFormat formatFalse = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        assertFalse(formatFalse.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testSetAllowMissingColumnNamesUsingReflection() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Create a new instance with allowMissingColumnNames = true
        CSVFormat newFormatTrue = createCSVFormatWithAllowMissingColumnNames(format, true);
        assertTrue(newFormatTrue.getAllowMissingColumnNames());

        // Create a new instance with allowMissingColumnNames = false
        CSVFormat newFormatFalse = createCSVFormatWithAllowMissingColumnNames(format, false);
        assertFalse(newFormatFalse.getAllowMissingColumnNames());
    }

    // Helper method to create a new CSVFormat instance with modified allowMissingColumnNames field
    private CSVFormat createCSVFormatWithAllowMissingColumnNames(CSVFormat baseFormat, boolean allowMissing) throws Exception {
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

        char delimiter = delimiterField.getChar(baseFormat);
        Character quoteChar = (Character) quoteCharField.get(baseFormat);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(baseFormat);
        Character commentMarker = (Character) commentMarkerField.get(baseFormat);
        Character escapeCharacter = (Character) escapeCharacterField.get(baseFormat);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(baseFormat);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(baseFormat);
        String recordSeparator = (String) recordSeparatorField.get(baseFormat);
        String nullString = (String) nullStringField.get(baseFormat);
        Object[] headerComments = (Object[]) headerCommentsField.get(baseFormat);
        String[] header = (String[]) headerField.get(baseFormat);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(baseFormat);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(baseFormat);
        boolean trim = trimField.getBoolean(baseFormat);
        boolean trailingDelimiter = trailingDelimiterField.getBoolean(baseFormat);

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class,
                boolean.class);

        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter, quoteChar, quoteMode,
                commentMarker, escapeCharacter, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord,
                allowMissing, ignoreHeaderCase, trim,
                trailingDelimiter);
    }
}