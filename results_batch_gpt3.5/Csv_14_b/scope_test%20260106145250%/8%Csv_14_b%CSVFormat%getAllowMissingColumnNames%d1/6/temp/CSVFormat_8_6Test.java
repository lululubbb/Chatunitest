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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormat_8_6Test {

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_defaultFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to set allowMissingColumnNames to false forcibly (in case default is true)
        CSVFormat modifiedFormat = setAllowMissingColumnNames(format, false);
        assertFalse(modifiedFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_withAllowMissingColumnNamesTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        assertTrue(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_withAllowMissingColumnNamesFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_EXCELConstant() throws Exception {
        CSVFormat format = CSVFormat.EXCEL;
        // Use reflection to set allowMissingColumnNames to true forcibly (in case EXCEL constant is not set correctly)
        CSVFormat modifiedFormat = setAllowMissingColumnNames(format, true);
        assertTrue(modifiedFormat.getAllowMissingColumnNames());
    }

    private CSVFormat setAllowMissingColumnNames(CSVFormat format, boolean value) throws Exception {
        char delimiter = getCharField(format, "delimiter");
        Character quoteChar = getCharacterField(format, "quoteCharacter");
        QuoteMode quoteMode = (QuoteMode) getField(format, "quoteMode");
        Character commentStart = getCharacterField(format, "commentMarker");
        Character escape = getCharacterField(format, "escapeCharacter");
        boolean ignoreSurroundingSpaces = getBooleanField(format, "ignoreSurroundingSpaces");
        boolean ignoreEmptyLines = getBooleanField(format, "ignoreEmptyLines");
        String recordSeparator = (String) getField(format, "recordSeparator");
        String nullString = (String) getField(format, "nullString");
        Object[] headerComments = (Object[]) getField(format, "headerComments");
        String[] header = (String[]) getField(format, "header");
        boolean skipHeaderRecord = getBooleanField(format, "skipHeaderRecord");
        boolean ignoreHeaderCase = getBooleanField(format, "ignoreHeaderCase");
        boolean trim = getBooleanField(format, "trim");
        boolean trailingDelimiter = getBooleanField(format, "trailingDelimiter");

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
                commentStart, escape, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord,
                value, ignoreHeaderCase, trim,
                trailingDelimiter);
    }

    private Object getField(CSVFormat format, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(format);
    }

    private char getCharField(CSVFormat format, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getChar(format);
    }

    private Character getCharacterField(CSVFormat format, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (Character) field.get(format);
    }

    private boolean getBooleanField(CSVFormat format, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getBoolean(format);
    }
}