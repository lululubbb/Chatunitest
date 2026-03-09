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

class CSVFormat_27_5Test {

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSetWhenEscapeCharacterIsNull() throws Exception {
        // Create a new CSVFormat instance with escapeCharacter set to null using reflection
        CSVFormat formatWithNullEscape = createCSVFormatWithEscape(null);

        assertFalse(formatWithNullEscape.isEscapeCharacterSet(), "Escape character should not be set when null");
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSetWhenEscapeCharacterIsSet() throws Exception {
        // Create a new CSVFormat instance with escapeCharacter set to '\\' using reflection
        CSVFormat formatWithEscape = createCSVFormatWithEscape('\\');

        assertTrue(formatWithEscape.isEscapeCharacterSet(), "Escape character should be set when non-null");
    }

    private CSVFormat createCSVFormatWithEscape(Character escape) throws Exception {
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
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
        quoteCharField.setAccessible(true);
        quoteModeField.setAccessible(true);
        commentMarkerField.setAccessible(true);
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

        char delimiter = delimiterField.getChar(defaultFormat);
        Character quoteChar = (Character) quoteCharField.get(defaultFormat);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(defaultFormat);
        Character commentStart = (Character) commentMarkerField.get(defaultFormat);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(defaultFormat);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(defaultFormat);
        String recordSeparator = (String) recordSeparatorField.get(defaultFormat);
        String nullString = (String) nullStringField.get(defaultFormat);
        Object[] headerComments = (Object[]) headerCommentsField.get(defaultFormat);
        String[] header = (String[]) headerField.get(defaultFormat);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(defaultFormat);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(defaultFormat);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(defaultFormat);
        boolean trim = trimField.getBoolean(defaultFormat);
        boolean trailingDelimiter = trailingDelimiterField.getBoolean(defaultFormat);
        boolean autoFlush = autoFlushField.getBoolean(defaultFormat);

        java.lang.reflect.Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter,
                quoteChar,
                quoteMode,
                commentStart,
                escape,
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
                autoFlush
        );
    }
}