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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;

class CSVFormat_20_2Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_default() throws Exception {
        // Because CSVFormat.DEFAULT is static final, we must remove final modifier to change the field
        Field defaultField = CSVFormat.class.getDeclaredField("DEFAULT");
        defaultField.setAccessible(true);

        // Remove final modifier from the DEFAULT field to allow modification if needed
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(defaultField, defaultField.getModifiers() & ~Modifier.FINAL);

        // Get original DEFAULT instance
        CSVFormat originalDefault = (CSVFormat) defaultField.get(null);

        // Use reflection to get all fields needed to construct new CSVFormat
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
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

        char delimiter = (char) delimiterField.get(originalDefault);
        Character quoteCharacter = (Character) quoteCharacterField.get(originalDefault);
        // Set quoteMode to null explicitly
        QuoteMode quoteMode = null;
        Character commentMarker = (Character) commentMarkerField.get(originalDefault);
        Character escapeCharacter = (Character) escapeCharacterField.get(originalDefault);
        boolean ignoreSurroundingSpaces = (boolean) ignoreSurroundingSpacesField.get(originalDefault);
        boolean allowMissingColumnNames = (boolean) allowMissingColumnNamesField.get(originalDefault);
        boolean ignoreEmptyLines = (boolean) ignoreEmptyLinesField.get(originalDefault);
        String recordSeparator = (String) recordSeparatorField.get(originalDefault);
        String nullString = (String) nullStringField.get(originalDefault);
        Object[] headerComments = (Object[]) headerCommentsField.get(originalDefault);
        String[] header = (String[]) headerField.get(originalDefault);
        boolean skipHeaderRecord = (boolean) skipHeaderRecordField.get(originalDefault);
        boolean ignoreHeaderCase = (boolean) ignoreHeaderCaseField.get(originalDefault);

        // Access private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
                delimiter, quoteCharacter, quoteMode,
                commentMarker, escapeCharacter, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord,
                allowMissingColumnNames, ignoreHeaderCase);

        QuoteMode mode = format.getQuoteMode();
        assertNull(mode, "Default CSVFormat quoteMode should be null");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_customQuoteMode() {
        QuoteMode customMode = QuoteMode.ALL;
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(customMode);
        QuoteMode mode = format.getQuoteMode();
        assertEquals(customMode, mode, "QuoteMode should be the custom mode set");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_nullQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(null);
        QuoteMode mode = format.getQuoteMode();
        assertNull(mode, "QuoteMode should be null when explicitly set to null");
    }
}