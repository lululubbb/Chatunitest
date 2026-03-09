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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

public class CSVFormat_21_6Test {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatSkipTrue;
    private CSVFormat csvFormatSkipFalse;

    @BeforeEach
    public void setUp() throws Exception {
        csvFormatDefault = CSVFormat.DEFAULT;

        // Create CSVFormat instances with skipHeaderRecord true and false using reflection
        csvFormatSkipTrue = createCSVFormatWithSkipHeaderRecord(true);
        csvFormatSkipFalse = createCSVFormatWithSkipHeaderRecord(false);
    }

    private CSVFormat createCSVFormatWithSkipHeaderRecord(boolean skip) throws Exception {
        Class<?> clazz = CSVFormat.class;
        Constructor<?> ctor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class,
                boolean.class);
        ctor.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        Field delimiterField = clazz.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(defaultFormat);

        Field quoteCharacterField = clazz.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        Character quoteChar = (Character) quoteCharacterField.get(defaultFormat);

        Field quoteModeField = clazz.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(defaultFormat);

        Field commentMarkerField = clazz.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(defaultFormat);

        Field escapeCharacterField = clazz.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeChar = (Character) escapeCharacterField.get(defaultFormat);

        Field ignoreSurroundingSpacesField = clazz.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(defaultFormat);

        Field ignoreEmptyLinesField = clazz.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(defaultFormat);

        Field recordSeparatorField = clazz.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(defaultFormat);

        Field nullStringField = clazz.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullString = (String) nullStringField.get(defaultFormat);

        Field headerCommentsField = clazz.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Object[] headerComments = (Object[]) headerCommentsField.get(defaultFormat);

        Field headerField = clazz.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(defaultFormat);

        Field allowMissingColumnNamesField = clazz.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(defaultFormat);

        Field ignoreHeaderCaseField = clazz.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(defaultFormat);

        Field trimField = clazz.getDeclaredField("trim");
        trimField.setAccessible(true);
        boolean trim = trimField.getBoolean(defaultFormat);

        Field trailingDelimiterField = clazz.getDeclaredField("trailingDelimiter");
        trailingDelimiterField.setAccessible(true);
        boolean trailingDelimiter = trailingDelimiterField.getBoolean(defaultFormat);

        return (CSVFormat) ctor.newInstance(delimiter, quoteChar, quoteMode, commentMarker, escapeChar,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skip, allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetSkipHeaderRecord_Default() {
        assertFalse(csvFormatDefault.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testGetSkipHeaderRecord_True() {
        assertTrue(csvFormatSkipTrue.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testGetSkipHeaderRecord_False() {
        assertFalse(csvFormatSkipFalse.getSkipHeaderRecord());
    }
}