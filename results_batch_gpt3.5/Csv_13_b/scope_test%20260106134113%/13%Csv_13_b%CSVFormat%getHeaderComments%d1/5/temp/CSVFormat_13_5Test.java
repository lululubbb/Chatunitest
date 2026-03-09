package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
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

public class CSVFormat_13_5Test {

    @Test
    @Timeout(8000)
    void testGetHeaderComments_NullHeaderComments() throws Exception {
        CSVFormat format = createCSVFormatWithHeaderComments(null);
        String[] result = format.getHeaderComments();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderComments_NonNullHeaderComments() throws Exception {
        String[] comments = new String[] {"comment1", "comment2"};
        CSVFormat format = createCSVFormatWithHeaderComments(comments);
        String[] result = format.getHeaderComments();
        assertNotNull(result);
        assertArrayEquals(comments, result);
        assertNotSame(comments, result); // ensure clone returned
    }

    private CSVFormat createCSVFormatWithHeaderComments(String[] headerComments) throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;

        Class<?> quoteModeClass = Class.forName("org.apache.commons.csv.CSVFormat$QuoteMode");

        Constructor<CSVFormat> ctor = clazz.getDeclaredConstructor(
                char.class, Character.class, quoteModeClass,
                Character.class, Character.class, boolean.class, boolean.class,
                String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        ctor.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        Field delimiterField = clazz.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(defaultFormat);

        Field quoteCharacterField = clazz.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        Character quoteCharacter = (Character) quoteCharacterField.get(defaultFormat);

        Field quoteModeField = clazz.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        Object quoteMode = quoteModeField.get(defaultFormat);

        Field commentMarkerField = clazz.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(defaultFormat);

        Field escapeCharacterField = clazz.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeCharacter = (Character) escapeCharacterField.get(defaultFormat);

        Field ignoreSurroundingSpacesField = clazz.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(defaultFormat);

        Field allowMissingColumnNamesField = clazz.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(defaultFormat);

        Field recordSeparatorField = clazz.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(defaultFormat);

        Field nullStringField = clazz.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullString = (String) nullStringField.get(defaultFormat);

        Object[] headerCommentsObj = headerComments == null ? null : headerComments.clone();

        Field headerField = clazz.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(defaultFormat);

        Field skipHeaderRecordField = clazz.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(defaultFormat);

        Field ignoreEmptyLinesField = clazz.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(defaultFormat);

        Field ignoreHeaderCaseField = clazz.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(defaultFormat);

        return ctor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, allowMissingColumnNames, ignoreEmptyLines, recordSeparator,
                nullString, headerCommentsObj, header, skipHeaderRecord, ignoreEmptyLines, ignoreHeaderCase);
    }
}