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

class CSVFormat_17_2Test {

    @Test
    @Timeout(8000)
    void testGetNullString_DefaultInstance() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // CSVFormat is immutable, so create a new instance with reflection to set nullString to null forcibly
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        // Create a new CSVFormat instance copying all fields from DEFAULT except nullString set to null
        Field[] fields = CSVFormat.class.getDeclaredFields();
        Object[] constructorArgs = new Object[17];
        // The constructor parameters order from the source:
        // (char delimiter, Character quoteChar, QuoteMode quoteMode,
        // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        // boolean ignoreEmptyLines, String recordSeparator, String nullString,
        // Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim,
        // boolean trailingDelimiter, boolean autoFlush)

        // Get all needed fields
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
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
        headerCommentsField.setAccessible(true);
        headerField.setAccessible(true);
        skipHeaderRecordField.setAccessible(true);
        allowMissingColumnNamesField.setAccessible(true);
        ignoreHeaderCaseField.setAccessible(true);
        trimField.setAccessible(true);
        trailingDelimiterField.setAccessible(true);
        autoFlushField.setAccessible(true);

        constructorArgs[0] = delimiterField.get(format);
        constructorArgs[1] = quoteCharacterField.get(format);
        constructorArgs[2] = quoteModeField.get(format);
        constructorArgs[3] = commentMarkerField.get(format);
        constructorArgs[4] = escapeCharacterField.get(format);
        constructorArgs[5] = ignoreSurroundingSpacesField.get(format);
        constructorArgs[6] = ignoreEmptyLinesField.get(format);
        constructorArgs[7] = recordSeparatorField.get(format);
        constructorArgs[8] = null; // forcibly set nullString to null
        constructorArgs[9] = headerCommentsField.get(format);
        constructorArgs[10] = headerField.get(format);
        constructorArgs[11] = skipHeaderRecordField.get(format);
        constructorArgs[12] = allowMissingColumnNamesField.get(format);
        constructorArgs[13] = ignoreHeaderCaseField.get(format);
        constructorArgs[14] = trimField.get(format);
        constructorArgs[15] = trailingDelimiterField.get(format);
        constructorArgs[16] = autoFlushField.get(format);

        // Get the constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);
        CSVFormat newFormat = constructor.newInstance(constructorArgs);

        assertNull(newFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullStringSet() {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");
        assertEquals("NULL", format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithEmptyString() {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("");
        assertEquals("", format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithWhitespaceString() {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("   ");
        assertEquals("   ", format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_FromPredefinedFormats() {
        assertNull(CSVFormat.DEFAULT.getNullString());
        assertEquals("\\N", CSVFormat.MYSQL.getNullString());
        assertEquals("", CSVFormat.POSTGRESQL_CSV.getNullString());
        assertEquals("\\N", CSVFormat.POSTGRESQL_TEXT.getNullString());
    }
}