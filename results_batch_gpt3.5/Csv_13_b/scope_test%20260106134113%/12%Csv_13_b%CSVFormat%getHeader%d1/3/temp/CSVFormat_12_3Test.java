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

public class CSVFormat_12_3Test {

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNull() throws Exception {
        // Create a new CSVFormat instance by cloning DEFAULT and setting header to null via reflection
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // Create a new CSVFormat instance with header set to null by copying all fields from DEFAULT but header=null
        CSVFormat csvFormatWithNullHeader = createCSVFormatWithHeader(null);

        String[] header = csvFormatWithNullHeader.getHeader();
        assertNull(header, "Header should be null when internal header field is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsClone() throws Exception {
        // Prepare header array
        String[] originalHeader = new String[] {"col1", "col2", "col3"};

        // Create a new CSVFormat instance with header set to originalHeader via withHeader method
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(originalHeader);

        String[] returnedHeader = csvFormat.getHeader();

        // Verify returned header is not the same instance (clone)
        assertNotSame(originalHeader, returnedHeader, "Returned header should be a clone, not the same instance");

        // Verify content equality
        assertArrayEquals(originalHeader, returnedHeader, "Returned header content should match original header");

        // Modify returnedHeader and verify originalHeader is not affected
        returnedHeader[0] = "modified";
        assertNotEquals(originalHeader[0], returnedHeader[0], "Modifying returned header should not affect original header");
    }

    private CSVFormat createCSVFormatWithHeader(String[] header) throws Exception {
        // Use reflection to access the private constructor
        Class<CSVFormat> clazz = CSVFormat.class;

        // Get all fields from DEFAULT instance
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        Field delimiterField = clazz.getDeclaredField("delimiter");
        Field quoteCharacterField = clazz.getDeclaredField("quoteCharacter");
        Field quoteModeField = clazz.getDeclaredField("quoteMode");
        Field commentMarkerField = clazz.getDeclaredField("commentMarker");
        Field escapeCharacterField = clazz.getDeclaredField("escapeCharacter");
        Field ignoreSurroundingSpacesField = clazz.getDeclaredField("ignoreSurroundingSpaces");
        Field allowMissingColumnNamesField = clazz.getDeclaredField("allowMissingColumnNames");
        Field ignoreEmptyLinesField = clazz.getDeclaredField("ignoreEmptyLines");
        Field recordSeparatorField = clazz.getDeclaredField("recordSeparator");
        Field nullStringField = clazz.getDeclaredField("nullString");
        Field headerCommentsField = clazz.getDeclaredField("headerComments");
        Field skipHeaderRecordField = clazz.getDeclaredField("skipHeaderRecord");
        Field ignoreHeaderCaseField = clazz.getDeclaredField("ignoreHeaderCase");

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
        headerCommentsField.setAccessible(true);
        skipHeaderRecordField.setAccessible(true);
        ignoreHeaderCaseField.setAccessible(true);

        char delimiter = delimiterField.getChar(defaultFormat);
        Character quoteCharacter = (Character) quoteCharacterField.get(defaultFormat);
        Object quoteMode = quoteModeField.get(defaultFormat);
        Character commentMarker = (Character) commentMarkerField.get(defaultFormat);
        Character escapeCharacter = (Character) escapeCharacterField.get(defaultFormat);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(defaultFormat);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(defaultFormat);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(defaultFormat);
        String recordSeparator = (String) recordSeparatorField.get(defaultFormat);
        String nullString = (String) nullStringField.get(defaultFormat);
        Object headerComments = headerCommentsField.get(defaultFormat);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(defaultFormat);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(defaultFormat);

        // Get constructor
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);

        constructor.setAccessible(true);

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
                (Object[]) headerComments,
                header,
                skipHeaderRecord,
                allowMissingColumnNames,
                ignoreHeaderCase);
    }
}