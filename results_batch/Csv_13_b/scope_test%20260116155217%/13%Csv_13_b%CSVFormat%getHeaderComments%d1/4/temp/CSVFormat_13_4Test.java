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

import java.lang.reflect.Field;

public class CSVFormat_13_4Test {

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsCloneWhenNotNull() throws Exception {
        // Create a new CSVFormat instance by cloning DEFAULT and setting headerComments via reflection
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to create a new CSVFormat instance with headerComments set
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);

        // Create a new CSVFormat instance with headerComments set by copying all fields from DEFAULT
        // and setting headerComments to a specific array.
        CSVFormat newFormat = createCSVFormatWithHeaderComments(new String[]{"comment1", "comment2"});

        String[] originalComments = newFormat.getHeaderComments();
        assertNotNull(originalComments);

        String[] result = newFormat.getHeaderComments();
        assertNotNull(result);
        assertArrayEquals(originalComments, result);

        // Verify that returned array is a clone, not the same instance
        assertNotSame(originalComments, result);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsNullWhenNull() throws Exception {
        // Create a new CSVFormat instance with headerComments set to null via reflection
        CSVFormat newFormat = createCSVFormatWithHeaderComments(null);

        String[] result = newFormat.getHeaderComments();
        assertNull(result);
    }

    private CSVFormat createCSVFormatWithHeaderComments(String[] headerComments) throws Exception {
        // Get all fields from CSVFormat to construct a new instance via reflection
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
        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");

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
        skipHeaderRecordField.setAccessible(true);
        ignoreHeaderCaseField.setAccessible(true);
        headerCommentsField.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        // Use the private constructor via reflection
        java.lang.reflect.Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                String[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiterField.getChar(defaultFormat),
                (Character) quoteCharacterField.get(defaultFormat),
                (QuoteMode) quoteModeField.get(defaultFormat),
                (Character) commentMarkerField.get(defaultFormat),
                (Character) escapeCharacterField.get(defaultFormat),
                ignoreSurroundingSpacesField.getBoolean(defaultFormat),
                ignoreEmptyLinesField.getBoolean(defaultFormat),
                (String) recordSeparatorField.get(defaultFormat),
                (String) nullStringField.get(defaultFormat),
                headerComments,
                (String[]) headerField.get(defaultFormat),
                skipHeaderRecordField.getBoolean(defaultFormat),
                allowMissingColumnNamesField.getBoolean(defaultFormat),
                ignoreHeaderCaseField.getBoolean(defaultFormat)
        );
    }
}