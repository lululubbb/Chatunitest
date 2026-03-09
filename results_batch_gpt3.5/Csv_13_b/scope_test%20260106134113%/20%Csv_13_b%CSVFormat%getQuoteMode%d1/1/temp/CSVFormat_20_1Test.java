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

class CSVFormat_20_1Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode mode = format.getQuoteMode();
        assertNotNull(mode);
        assertEquals(QuoteMode.MINIMAL, mode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        QuoteMode mode = format.getQuoteMode();
        assertNotNull(mode);
        assertEquals(QuoteMode.ALL, mode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithNullQuoteMode() throws Exception {
        // Create a CSVFormat instance with quoteMode = null using reflection
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat formatWithNullQuoteMode = createCSVFormatWithNullQuoteMode(format);

        QuoteMode mode = formatWithNullQuoteMode.getQuoteMode();
        assertNull(mode);
    }

    private CSVFormat createCSVFormatWithNullQuoteMode(CSVFormat base) throws Exception {
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
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");

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
        quoteModeField.setAccessible(true);

        char delimiter = (char) delimiterField.get(base);
        Character quoteCharacter = (Character) quoteCharacterField.get(base);
        Character commentMarker = (Character) commentMarkerField.get(base);
        Character escapeCharacter = (Character) escapeCharacterField.get(base);
        boolean ignoreSurroundingSpaces = (boolean) ignoreSurroundingSpacesField.get(base);
        boolean allowMissingColumnNames = (boolean) allowMissingColumnNamesField.get(base);
        boolean ignoreEmptyLines = (boolean) ignoreEmptyLinesField.get(base);
        String recordSeparator = (String) recordSeparatorField.get(base);
        String nullString = (String) nullStringField.get(base);
        String[] header = (String[]) headerField.get(base);
        String[] headerComments = (String[]) headerCommentsField.get(base);
        boolean skipHeaderRecord = (boolean) skipHeaderRecordField.get(base);
        boolean ignoreHeaderCase = (boolean) ignoreHeaderCaseField.get(base);

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter,
                quoteCharacter,
                null, // quoteMode = null
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                allowMissingColumnNames,
                recordSeparator,
                nullString,
                header,
                headerComments,
                skipHeaderRecord,
                ignoreEmptyLines,
                ignoreHeaderCase);
    }
}