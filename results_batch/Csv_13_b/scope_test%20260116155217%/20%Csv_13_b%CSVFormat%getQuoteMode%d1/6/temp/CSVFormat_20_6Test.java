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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormat_20_6Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode quoteMode = format.getQuoteMode();
        assertNull(quoteMode, "Default CSVFormat should have null QuoteMode");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_RFC4180() {
        CSVFormat format = CSVFormat.RFC4180;
        QuoteMode quoteMode = format.getQuoteMode();
        assertNull(quoteMode, "RFC4180 CSVFormat should have null QuoteMode");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Excel() {
        CSVFormat format = CSVFormat.EXCEL;
        QuoteMode quoteMode = format.getQuoteMode();
        assertNull(quoteMode, "EXCEL CSVFormat should have null QuoteMode");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_TDF() {
        CSVFormat format = CSVFormat.TDF;
        QuoteMode quoteMode = format.getQuoteMode();
        assertNull(quoteMode, "TDF CSVFormat should have null QuoteMode");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_MySQL() {
        CSVFormat format = CSVFormat.MYSQL;
        QuoteMode quoteMode = format.getQuoteMode();
        assertNull(quoteMode, "MYSQL CSVFormat should have null QuoteMode");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_CustomQuoteMode() throws Exception {
        QuoteMode customQuoteMode = QuoteMode.ALL;

        CSVFormat formatDefault = CSVFormat.DEFAULT;

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(formatDefault);

        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharField.setAccessible(true);
        Character quoteChar = (Character) quoteCharField.get(formatDefault);

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(formatDefault);

        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeCharacter = (Character) escapeCharacterField.get(formatDefault);

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(formatDefault);

        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(formatDefault);

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(formatDefault);

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(formatDefault);

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullString = (String) nullStringField.get(formatDefault);

        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Object[] headerComments = (Object[]) headerCommentsField.get(formatDefault);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(formatDefault);

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(formatDefault);

        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(formatDefault);

        CSVFormat format = constructor.newInstance(
                delimiter, quoteChar, customQuoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, allowMissingColumnNames, recordSeparator,
                nullString, headerComments, header, skipHeaderRecord,
                ignoreEmptyLines, ignoreHeaderCase);

        assertEquals(customQuoteMode, format.getQuoteMode(), "Custom QuoteMode should be returned");
    }

}