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

class CSVFormat_22_3Test {

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_DefaultFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to create a new instance with skipHeaderRecord = false
        CSVFormat modifiedFormat = setSkipHeaderRecord(format, false);
        assertFalse(modifiedFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_WithSkipHeaderRecordTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertTrue(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_WithSkipHeaderRecordFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        assertFalse(format.getSkipHeaderRecord());
    }

    private CSVFormat setSkipHeaderRecord(CSVFormat format, boolean value) throws Exception {
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        Field headerField = CSVFormat.class.getDeclaredField("header");
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");

        delimiterField.setAccessible(true);
        quoteCharacterField.setAccessible(true);
        quoteModeField.setAccessible(true);
        commentMarkerField.setAccessible(true);
        escapeCharacterField.setAccessible(true);
        ignoreSurroundingSpacesField.setAccessible(true);
        allowMissingColumnNamesField.setAccessible(true);
        ignoreEmptyLinesField.setAccessible(true);
        recordSeparatorField.setAccessible(true);
        nullStringField.setAccessible(true);
        headerField.setAccessible(true);
        headerCommentsField.setAccessible(true);
        skipHeaderRecordField.setAccessible(true);
        ignoreHeaderCaseField.setAccessible(true);

        char delimiter = delimiterField.getChar(format);
        Character quoteCharacter = (Character) quoteCharacterField.get(format);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(format);
        Character commentMarker = (Character) commentMarkerField.get(format);
        Character escapeCharacter = (Character) escapeCharacterField.get(format);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(format);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(format);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(format);
        String recordSeparator = (String) recordSeparatorField.get(format);
        String nullString = (String) nullStringField.get(format);
        String[] header = (String[]) headerField.get(format);
        String[] headerComments = (String[]) headerCommentsField.get(format);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(format);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(format);

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        Object[] headerCommentsObj = headerComments;

        return constructor.newInstance(
                delimiter,
                quoteCharacter,
                quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                headerCommentsObj,
                header,
                value,
                allowMissingColumnNames,
                ignoreHeaderCase);
    }
}