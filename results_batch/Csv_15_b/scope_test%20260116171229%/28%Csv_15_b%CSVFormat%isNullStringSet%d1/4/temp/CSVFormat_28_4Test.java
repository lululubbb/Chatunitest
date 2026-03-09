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

import java.lang.reflect.Field;

public class CSVFormat_28_4Test {

    @Test
    @Timeout(8000)
    public void testIsNullStringSet_nullStringNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to create a new CSVFormat instance with null nullString
        CSVFormat newFormat = createCSVFormatWithNullString(null);

        boolean result = newFormat.isNullStringSet();
        assertFalse(result, "Expected isNullStringSet() to return false when nullString is null");
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSet_nullStringSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to create a new CSVFormat instance with non-null nullString
        CSVFormat newFormat = createCSVFormatWithNullString("NULL");

        boolean result = newFormat.isNullStringSet();
        assertTrue(result, "Expected isNullStringSet() to return true when nullString is non-null");
    }

    private CSVFormat createCSVFormatWithNullString(String nullString) throws Exception {
        // Access the private constructor of CSVFormat
        Class<CSVFormat> clazz = CSVFormat.class;

        Field delimiterField = clazz.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = (char) delimiterField.get(CSVFormat.DEFAULT);

        Field quoteCharField = clazz.getDeclaredField("quoteCharacter");
        quoteCharField.setAccessible(true);
        Character quoteChar = (Character) quoteCharField.get(CSVFormat.DEFAULT);

        Field quoteModeField = clazz.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        Object quoteMode = quoteModeField.get(CSVFormat.DEFAULT);

        Field commentMarkerField = clazz.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(CSVFormat.DEFAULT);

        Field escapeCharacterField = clazz.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeCharacter = (Character) escapeCharacterField.get(CSVFormat.DEFAULT);

        Field ignoreSurroundingSpacesField = clazz.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = (boolean) ignoreSurroundingSpacesField.get(CSVFormat.DEFAULT);

        Field ignoreEmptyLinesField = clazz.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = (boolean) ignoreEmptyLinesField.get(CSVFormat.DEFAULT);

        Field recordSeparatorField = clazz.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(CSVFormat.DEFAULT);

        Field headerCommentsField = clazz.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Object[] headerComments = (Object[]) headerCommentsField.get(CSVFormat.DEFAULT);

        Field headerField = clazz.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(CSVFormat.DEFAULT);

        Field skipHeaderRecordField = clazz.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = (boolean) skipHeaderRecordField.get(CSVFormat.DEFAULT);

        Field allowMissingColumnNamesField = clazz.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = (boolean) allowMissingColumnNamesField.get(CSVFormat.DEFAULT);

        Field ignoreHeaderCaseField = clazz.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = (boolean) ignoreHeaderCaseField.get(CSVFormat.DEFAULT);

        Field trimField = clazz.getDeclaredField("trim");
        trimField.setAccessible(true);
        boolean trim = (boolean) trimField.get(CSVFormat.DEFAULT);

        Field trailingDelimiterField = clazz.getDeclaredField("trailingDelimiter");
        trailingDelimiterField.setAccessible(true);
        boolean trailingDelimiter = (boolean) trailingDelimiterField.get(CSVFormat.DEFAULT);

        Field autoFlushField = clazz.getDeclaredField("autoFlush");
        autoFlushField.setAccessible(true);
        boolean autoFlush = (boolean) autoFlushField.get(CSVFormat.DEFAULT);

        // Find the constructor and create new instance
        java.lang.reflect.Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class,
                boolean.class, boolean.class, boolean.class, boolean.class);

        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter, quoteChar, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter, autoFlush);
    }
}