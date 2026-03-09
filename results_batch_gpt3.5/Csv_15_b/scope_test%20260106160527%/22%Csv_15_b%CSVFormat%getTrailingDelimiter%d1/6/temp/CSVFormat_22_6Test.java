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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class CSVFormat_22_6Test {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatWithTrailingTrue;
    private CSVFormat csvFormatWithTrailingFalse;

    @BeforeEach
    public void setUp() throws Exception {
        csvFormatDefault = CSVFormat.DEFAULT;

        // Use reflection to create new CSVFormat instances with trailingDelimiter true and false
        csvFormatWithTrailingTrue = createCSVFormatWithTrailingDelimiter(true);
        csvFormatWithTrailingFalse = createCSVFormatWithTrailingDelimiter(false);
    }

    private CSVFormat createCSVFormatWithTrailingDelimiter(boolean trailingDelimiter) throws Exception {
        // Access private final fields from csvFormatDefault via reflection
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
        autoFlushField.setAccessible(true);

        char delimiter = delimiterField.getChar(csvFormatDefault);
        Character quoteCharacter = (Character) quoteCharacterField.get(csvFormatDefault);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(csvFormatDefault);
        Character commentMarker = (Character) commentMarkerField.get(csvFormatDefault);
        Character escapeCharacter = (Character) escapeCharacterField.get(csvFormatDefault);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(csvFormatDefault);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(csvFormatDefault);
        String recordSeparator = (String) recordSeparatorField.get(csvFormatDefault);
        String nullString = (String) nullStringField.get(csvFormatDefault);
        Object[] headerComments = (Object[]) headerCommentsField.get(csvFormatDefault);
        String[] header = (String[]) headerField.get(csvFormatDefault);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(csvFormatDefault);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(csvFormatDefault);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(csvFormatDefault);
        boolean trim = trimField.getBoolean(csvFormatDefault);
        boolean autoFlush = autoFlushField.getBoolean(csvFormatDefault);

        // Use private constructor via reflection
        // Constructor signature:
        // CSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
        // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        // boolean ignoreEmptyLines, String recordSeparator, String nullString,
        // Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim,
        // boolean trailingDelimiter, boolean autoFlush)

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
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
                boolean.class,
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
                headerComments,
                header,
                skipHeaderRecord,
                allowMissingColumnNames,
                ignoreHeaderCase,
                trim,
                trailingDelimiter,
                autoFlush);
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_Default() {
        // DEFAULT has trailingDelimiter = false as per constructor call
        assertFalse(csvFormatDefault.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_WithTrailingTrue() {
        assertTrue(csvFormatWithTrailingTrue.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_WithTrailingFalse() {
        assertFalse(csvFormatWithTrailingFalse.getTrailingDelimiter());
    }
}