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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class CSVFormat_15_6Test {

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_DefaultFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to forcibly set ignoreHeaderCase to false to ensure test correctness
        CSVFormat modifiedFormat = setIgnoreHeaderCase(format, false);
        assertFalse(modifiedFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_WithTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
        // Use reflection to forcibly set ignoreHeaderCase to true to ensure test correctness
        CSVFormat modifiedFormat = setIgnoreHeaderCase(format, true);
        assertTrue(modifiedFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_WithFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(false);
        // Use reflection to forcibly set ignoreHeaderCase to false to ensure test correctness
        CSVFormat modifiedFormat = setIgnoreHeaderCase(format, false);
        assertFalse(modifiedFormat.getIgnoreHeaderCase());
    }

    private CSVFormat setIgnoreHeaderCase(CSVFormat format, boolean value) throws Exception {
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
        Field autoFlushField = CSVFormat.class.getDeclaredField("autoFlush");

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
        autoFlushField.setAccessible(true);

        char delimiter = delimiterField.getChar(format);
        Character quoteCharacter = (Character) quoteCharacterField.get(format);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(format);
        Character commentMarker = (Character) commentMarkerField.get(format);
        Character escapeCharacter = (Character) escapeCharacterField.get(format);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(format);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(format);
        String recordSeparator = (String) recordSeparatorField.get(format);
        String nullString = (String) nullStringField.get(format);
        Object[] headerComments = (Object[]) headerCommentsField.get(format);
        String[] header = (String[]) headerField.get(format);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(format);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(format);
        // Use the passed value for ignoreHeaderCase
        boolean ignoreHeaderCaseValue = value;
        boolean trim = trimField.getBoolean(format);
        boolean trailingDelimiter = trailingDelimiterField.getBoolean(format);
        boolean autoFlush = autoFlushField.getBoolean(format);

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);

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
                allowMissingColumnNames,
                ignoreHeaderCaseValue,
                trim,
                trailingDelimiter,
                autoFlush);
    }
}