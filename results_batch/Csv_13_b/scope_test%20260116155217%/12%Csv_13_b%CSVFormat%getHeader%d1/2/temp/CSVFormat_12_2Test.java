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

public class CSVFormat_12_2Test {

    @Test
    @Timeout(8000)
    public void testGetHeaderWhenHeaderIsNull() throws Exception {
        // Create a new CSVFormat instance with header set to null
        CSVFormat csvFormatWithNullHeader = createCSVFormatWithHeader(null);

        String[] header = csvFormatWithNullHeader.getHeader();
        assertNull(header, "Header should be null when header field is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderWhenHeaderIsNonNull() throws Exception {
        String[] originalHeader = new String[] {"col1", "col2", "col3"};
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(originalHeader);

        String[] returnedHeader = csvFormat.getHeader();

        // Returned header should be a clone, not the same reference
        assertNotNull(returnedHeader, "Header should not be null when header field is set");
        assertArrayEquals(originalHeader, returnedHeader, "Returned header should equal original header");
        assertNotSame(originalHeader, returnedHeader, "Returned header should be a clone, not the same reference");

        // Modify returnedHeader and verify originalHeader is unchanged
        returnedHeader[0] = "modified";
        assertNotEquals(originalHeader[0], returnedHeader[0], "Original header should not be affected by changes to returned header");
    }

    private CSVFormat createCSVFormatWithHeader(String[] header) throws Exception {
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
        headerCommentsField.setAccessible(true);
        skipHeaderRecordField.setAccessible(true);
        ignoreHeaderCaseField.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

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

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);

        constructor.setAccessible(true);

        Object[] headerCommentsArray = null;
        if (headerComments instanceof Object[]) {
            headerCommentsArray = (Object[]) headerComments;
        }

        return constructor.newInstance(
                delimiter,
                quoteCharacter,
                (QuoteMode) quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                allowMissingColumnNames,  // FIXED: swapped ignoreEmptyLines and allowMissingColumnNames here
                recordSeparator,
                nullString,
                headerCommentsArray,
                header,
                skipHeaderRecord,
                ignoreEmptyLines,          // FIXED: swapped allowMissingColumnNames and ignoreEmptyLines here
                ignoreHeaderCase
        );
    }
}