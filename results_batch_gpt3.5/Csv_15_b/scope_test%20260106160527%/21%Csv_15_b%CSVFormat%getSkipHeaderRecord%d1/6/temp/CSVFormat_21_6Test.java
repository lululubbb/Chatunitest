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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormat_21_6Test {

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_DefaultFalse() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_TrueViaWithSkipHeaderRecord() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertTrue(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_FalseViaWithSkipHeaderRecord() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_ReflectionAccess() throws Exception {
        // Access all necessary fields
        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);

        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);

        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);

        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);

        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);

        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);

        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);

        Field trimField = CSVFormat.class.getDeclaredField("trim");
        trimField.setAccessible(true);

        Field trailingDelimiterField = CSVFormat.class.getDeclaredField("trailingDelimiter");
        trailingDelimiterField.setAccessible(true);

        Field autoFlushField = CSVFormat.class.getDeclaredField("autoFlush");
        autoFlushField.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Create new CSVFormat instance via constructor using reflection with skipHeaderRecord = true
        CSVFormat formatWithSkipTrue = constructor.newInstance(
                delimiterField.getChar(defaultFormat),
                (Character) quoteCharacterField.get(defaultFormat),
                (QuoteMode) quoteModeField.get(defaultFormat),
                (Character) commentMarkerField.get(defaultFormat),
                (Character) escapeCharacterField.get(defaultFormat),
                ignoreSurroundingSpacesField.getBoolean(defaultFormat),
                ignoreEmptyLinesField.getBoolean(defaultFormat),
                (String) recordSeparatorField.get(defaultFormat),
                (String) nullStringField.get(defaultFormat),
                (Object[]) headerCommentsField.get(defaultFormat),
                (String[]) headerField.get(defaultFormat),
                true, // skipHeaderRecord = true
                allowMissingColumnNamesField.getBoolean(defaultFormat),
                ignoreHeaderCaseField.getBoolean(defaultFormat),
                trimField.getBoolean(defaultFormat),
                trailingDelimiterField.getBoolean(defaultFormat),
                autoFlushField.getBoolean(defaultFormat)
        );

        assertTrue(formatWithSkipTrue.getSkipHeaderRecord());

        // Create new CSVFormat instance via constructor using reflection with skipHeaderRecord = false
        CSVFormat formatWithSkipFalse = constructor.newInstance(
                delimiterField.getChar(defaultFormat),
                (Character) quoteCharacterField.get(defaultFormat),
                (QuoteMode) quoteModeField.get(defaultFormat),
                (Character) commentMarkerField.get(defaultFormat),
                (Character) escapeCharacterField.get(defaultFormat),
                ignoreSurroundingSpacesField.getBoolean(defaultFormat),
                ignoreEmptyLinesField.getBoolean(defaultFormat),
                (String) recordSeparatorField.get(defaultFormat),
                (String) nullStringField.get(defaultFormat),
                (Object[]) headerCommentsField.get(defaultFormat),
                (String[]) headerField.get(defaultFormat),
                false, // skipHeaderRecord = false
                allowMissingColumnNamesField.getBoolean(defaultFormat),
                ignoreHeaderCaseField.getBoolean(defaultFormat),
                trimField.getBoolean(defaultFormat),
                trailingDelimiterField.getBoolean(defaultFormat),
                autoFlushField.getBoolean(defaultFormat)
        );

        assertFalse(formatWithSkipFalse.getSkipHeaderRecord());
    }
}