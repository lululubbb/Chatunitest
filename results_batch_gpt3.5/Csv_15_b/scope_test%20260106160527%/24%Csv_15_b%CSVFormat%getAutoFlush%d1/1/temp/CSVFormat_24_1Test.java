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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormat_24_1Test {

    @Test
    @Timeout(8000)
    void testGetAutoFlush_DefaultFalse() throws Exception {
        CSVFormat format = getCSVFormatWithAutoFlush(false);
        boolean autoFlush = format.getAutoFlush();
        assertFalse(autoFlush);
    }

    @Test
    @Timeout(8000)
    void testGetAutoFlush_True() throws Exception {
        CSVFormat format = getCSVFormatWithAutoFlush(true);
        boolean autoFlush = format.getAutoFlush();
        assertTrue(autoFlush);
    }

    private CSVFormat getCSVFormatWithAutoFlush(boolean value) throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Try to use withAutoFlush method if present
        try {
            return format.withAutoFlush(value);
        } catch (NoSuchMethodError | UnsupportedOperationException e) {
            // fallback to reflection-based constructor invocation

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
                    boolean.class
            );
            constructor.setAccessible(true);

            Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
            delimiterField.setAccessible(true);
            char delimiter = delimiterField.getChar(format);

            Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
            quoteCharField.setAccessible(true);
            Character quoteChar = (Character) quoteCharField.get(format);

            Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
            quoteModeField.setAccessible(true);
            QuoteMode quoteMode = (QuoteMode) quoteModeField.get(format);

            Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
            commentMarkerField.setAccessible(true);
            Character commentMarker = (Character) commentMarkerField.get(format);

            Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
            escapeCharacterField.setAccessible(true);
            Character escapeCharacter = (Character) escapeCharacterField.get(format);

            Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
            ignoreSurroundingSpacesField.setAccessible(true);
            boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(format);

            Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
            ignoreEmptyLinesField.setAccessible(true);
            boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(format);

            Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
            recordSeparatorField.setAccessible(true);
            String recordSeparator = (String) recordSeparatorField.get(format);

            Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
            nullStringField.setAccessible(true);
            String nullString = (String) nullStringField.get(format);

            Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
            headerCommentsField.setAccessible(true);
            Object[] headerComments = (Object[]) headerCommentsField.get(format);

            Field headerField = CSVFormat.class.getDeclaredField("header");
            headerField.setAccessible(true);
            String[] header = (String[]) headerField.get(format);

            Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
            skipHeaderRecordField.setAccessible(true);
            boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(format);

            Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
            allowMissingColumnNamesField.setAccessible(true);
            boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(format);

            Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
            ignoreHeaderCaseField.setAccessible(true);
            boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(format);

            Field trimField = CSVFormat.class.getDeclaredField("trim");
            trimField.setAccessible(true);
            boolean trim = trimField.getBoolean(format);

            Field trailingDelimiterField = CSVFormat.class.getDeclaredField("trailingDelimiter");
            trailingDelimiterField.setAccessible(true);
            boolean trailingDelimiter = trailingDelimiterField.getBoolean(format);

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
                    trailingDelimiter,
                    value
            );
        }
    }
}