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
import org.apache.commons.csv.CSVFormat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormat_22_1Test {

    private CSVFormat setTrailingDelimiter(CSVFormat format, boolean value) {
        try {
            Field trailingDelimiterField = CSVFormat.class.getDeclaredField("trailingDelimiter");
            trailingDelimiterField.setAccessible(true);

            // CSVFormat is immutable, so create a new instance with the same fields except trailingDelimiter
            // Use reflection to get constructor and create new instance
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

            char delimiter = delimiterField.getChar(format);
            Character quoteChar = (Character) quoteCharacterField.get(format);
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
            boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(format);
            boolean trim = trimField.getBoolean(format);
            boolean autoFlush = autoFlushField.getBoolean(format);

            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                    char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                    boolean.class, boolean.class, String.class, String.class,
                    Object[].class, String[].class, boolean.class, boolean.class,
                    boolean.class, boolean.class, boolean.class, boolean.class);

            constructor.setAccessible(true);

            return constructor.newInstance(
                    delimiter,
                    quoteChar,
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
                    value,
                    autoFlush
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_defaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_withTrailingDelimiterTrue() {
        CSVFormat format = setTrailingDelimiter(CSVFormat.DEFAULT, true);
        assertTrue(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_withTrailingDelimiterFalse() {
        CSVFormat format = setTrailingDelimiter(CSVFormat.DEFAULT, false);
        assertFalse(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_onVariousPresets() {
        assertFalse(CSVFormat.EXCEL.getTrailingDelimiter());
        assertFalse(CSVFormat.INFORMIX_UNLOAD.getTrailingDelimiter());
        assertFalse(CSVFormat.INFORMIX_UNLOAD_CSV.getTrailingDelimiter());
        assertFalse(CSVFormat.MYSQL.getTrailingDelimiter());
        assertFalse(CSVFormat.POSTGRESQL_CSV.getTrailingDelimiter());
        assertFalse(CSVFormat.POSTGRESQL_TEXT.getTrailingDelimiter());
        assertFalse(CSVFormat.RFC4180.getTrailingDelimiter());
        assertFalse(CSVFormat.TDF.getTrailingDelimiter());
    }
}