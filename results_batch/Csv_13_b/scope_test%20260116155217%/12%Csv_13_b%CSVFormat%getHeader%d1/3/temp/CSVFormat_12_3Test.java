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
import org.junit.jupiter.api.BeforeEach;
import java.lang.reflect.Field;

public class CSVFormat_12_3Test {

    private CSVFormat csvFormatWithHeader;
    private CSVFormat csvFormatWithoutHeader;

    @BeforeEach
    public void setUp() throws Exception {
        // Use withHeader to create a CSVFormat instance with header
        csvFormatWithHeader = CSVFormat.DEFAULT.withHeader("col1", "col2", "col3");

        // Use DEFAULT instance for without header
        csvFormatWithoutHeader = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsCloneNotSameArray() throws Exception {
        String[] header1 = csvFormatWithHeader.getHeader();
        String[] header2 = csvFormatWithHeader.getHeader();

        assertNotNull(header1, "Header should not be null");
        assertArrayEquals(new String[]{"col1", "col2", "col3"}, header1, "Header content mismatch");
        assertNotSame(header1, header2, "getHeader should return a clone, not the same array instance");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsNullWhenHeaderIsNull() throws Exception {
        // Create a new CSVFormat instance by reflection with header=null
        CSVFormat formatWithoutHeader = createCSVFormatWithHeaderField(null);

        String[] header = formatWithoutHeader.getHeader();
        assertNull(header, "Header should be null when internal header field is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsCloneWhenHeaderSetViaReflection() throws Exception {
        String[] originalHeader = new String[]{"a", "b"};
        CSVFormat format = createCSVFormatWithHeaderField(originalHeader);

        String[] returnedHeader = format.getHeader();
        assertArrayEquals(originalHeader, returnedHeader, "Returned header should match set header");
        assertNotSame(originalHeader, returnedHeader, "Returned header should be a clone, not the same array");
    }

    // Helper method to create a CSVFormat instance with a specific header array using reflection
    private CSVFormat createCSVFormatWithHeaderField(String[] headerValue) throws Exception {
        // Get the CSVFormat class
        Class<CSVFormat> clazz = CSVFormat.class;

        // Get the private constructor with all parameters
        // Constructor parameters:
        // (char delimiter, Character quoteChar, QuoteMode quoteMode,
        //  Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        //  boolean ignoreEmptyLines, String recordSeparator, String nullString,
        //  Object[] headerComments, String[] header, boolean skipHeaderRecord,
        //  boolean allowMissingColumnNames, boolean ignoreHeaderCase)
        java.lang.reflect.Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);

        // Use values from CSVFormat.DEFAULT for other params
        CSVFormat def = CSVFormat.DEFAULT;

        // Get values from DEFAULT instance by reflection
        Field delimiterField = clazz.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(def);

        Field quoteCharField = clazz.getDeclaredField("quoteCharacter");
        quoteCharField.setAccessible(true);
        Character quoteChar = (Character) quoteCharField.get(def);

        Field quoteModeField = clazz.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(def);

        Field commentMarkerField = clazz.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(def);

        Field escapeCharacterField = clazz.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeCharacter = (Character) escapeCharacterField.get(def);

        Field ignoreSurroundingSpacesField = clazz.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(def);

        Field ignoreEmptyLinesField = clazz.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(def);

        Field recordSeparatorField = clazz.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(def);

        Field nullStringField = clazz.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullString = (String) nullStringField.get(def);

        Field headerCommentsField = clazz.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Object[] headerComments = (Object[]) headerCommentsField.get(def);

        Field skipHeaderRecordField = clazz.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(def);

        Field allowMissingColumnNamesField = clazz.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(def);

        Field ignoreHeaderCaseField = clazz.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(def);

        // Defensive copy for headerComments if null to avoid NullPointerException in constructor
        Object[] headerCommentsNonNull = headerComments != null ? headerComments : new Object[0];

        return constructor.newInstance(
                delimiter,
                quoteChar,
                quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                headerCommentsNonNull,
                headerValue,
                skipHeaderRecord,
                allowMissingColumnNames,
                ignoreHeaderCase);
    }
}