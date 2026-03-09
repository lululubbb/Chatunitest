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

class CSVFormat_19_4Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.MINIMAL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithQuoteModeSet() throws Exception {
        // Use reflection to create a CSVFormat instance with QuoteMode.ALL
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat format = setQuoteMode(baseFormat, QuoteMode.ALL);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.ALL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithNullQuoteMode() throws Exception {
        // Use reflection to create a CSVFormat instance with null QuoteMode
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat format = setQuoteMode(baseFormat, null);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNull(quoteMode);
    }

    private CSVFormat setQuoteMode(CSVFormat baseFormat, QuoteMode quoteMode) throws Exception {
        Class<?> cls = CSVFormat.class;

        Field delimiterField = cls.getDeclaredField("delimiter");
        Field quoteCharacterField = cls.getDeclaredField("quoteCharacter");
        Field commentMarkerField = cls.getDeclaredField("commentMarker");
        Field escapeCharacterField = cls.getDeclaredField("escapeCharacter");
        Field ignoreSurroundingSpacesField = cls.getDeclaredField("ignoreSurroundingSpaces");
        Field ignoreEmptyLinesField = cls.getDeclaredField("ignoreEmptyLines");
        Field recordSeparatorField = cls.getDeclaredField("recordSeparator");
        Field nullStringField = cls.getDeclaredField("nullString");
        Field headerCommentsField = cls.getDeclaredField("headerComments");
        Field headerField = cls.getDeclaredField("header");
        Field skipHeaderRecordField = cls.getDeclaredField("skipHeaderRecord");
        Field allowMissingColumnNamesField = cls.getDeclaredField("allowMissingColumnNames");
        Field ignoreHeaderCaseField = cls.getDeclaredField("ignoreHeaderCase");
        Field trimField = cls.getDeclaredField("trim");
        Field trailingDelimiterField = cls.getDeclaredField("trailingDelimiter");

        delimiterField.setAccessible(true);
        quoteCharacterField.setAccessible(true);
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

        char delimiter = delimiterField.getChar(baseFormat);
        Character quoteCharacter = (Character) quoteCharacterField.get(baseFormat);
        Character commentMarker = (Character) commentMarkerField.get(baseFormat);
        Character escapeCharacter = (Character) escapeCharacterField.get(baseFormat);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(baseFormat);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(baseFormat);
        String recordSeparator = (String) recordSeparatorField.get(baseFormat);
        String nullString = (String) nullStringField.get(baseFormat);
        Object[] headerComments = (Object[]) headerCommentsField.get(baseFormat);
        String[] header = (String[]) headerField.get(baseFormat);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(baseFormat);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(baseFormat);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(baseFormat);
        boolean trim = trimField.getBoolean(baseFormat);
        boolean trailingDelimiter = trailingDelimiterField.getBoolean(baseFormat);

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        return constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter);
    }

}