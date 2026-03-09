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

public class CSVFormat_13_6Test {

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsCloneOfArray() throws Exception {
        // Create CSVFormat instance with headerComments set via withHeaderComments method
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeaderComments("comment1", "comment2");

        // Use reflection to get the private field headerComments
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);

        // Get the actual headerComments array from the instance
        Object headerCommentsObj = headerCommentsField.get(csvFormat);
        String[] comments = null;
        if (headerCommentsObj instanceof Object[]) {
            Object[] objArray = (Object[]) headerCommentsObj;
            comments = new String[objArray.length];
            for (int i = 0; i < objArray.length; i++) {
                comments[i] = objArray[i] != null ? objArray[i].toString() : null;
            }
        }

        String[] returnedComments = csvFormat.getHeaderComments();

        // The returned array should be equal to the original but not the same instance
        assertArrayEquals(comments, returnedComments);
        assertNotSame(comments, returnedComments);

        // Modify returned array should not affect original
        returnedComments[0] = "modified";

        // Re-fetch headerComments field (should not be affected)
        Object headerCommentsObjAfter = headerCommentsField.get(csvFormat);
        String[] afterModification = null;
        if (headerCommentsObjAfter instanceof Object[]) {
            Object[] objArray = (Object[]) headerCommentsObjAfter;
            afterModification = new String[objArray.length];
            for (int i = 0; i < objArray.length; i++) {
                afterModification[i] = objArray[i] != null ? objArray[i].toString() : null;
            }
        }

        assertEquals("comment1", afterModification[0]);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsNullWhenHeaderCommentsIsNull() throws Exception {
        // Use reflection to get all fields needed for constructor
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

        // Extract values from DEFAULT instance
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        char delimiter = delimiterField.getChar(defaultFormat);
        Character quoteCharacter = (Character) quoteCharacterField.get(defaultFormat);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(defaultFormat);
        Character commentMarker = (Character) commentMarkerField.get(defaultFormat);
        Character escapeCharacter = (Character) escapeCharacterField.get(defaultFormat);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(defaultFormat);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(defaultFormat);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(defaultFormat);
        String recordSeparator = (String) recordSeparatorField.get(defaultFormat);
        String nullString = (String) nullStringField.get(defaultFormat);
        String[] header = (String[]) headerField.get(defaultFormat);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(defaultFormat);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(defaultFormat);

        // Get constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);

        constructor.setAccessible(true);

        // Create new instance with headerComments null
        CSVFormat csvFormatWithNullHeaderComments = constructor.newInstance(
                delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                null, header, skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);

        String[] returnedComments = csvFormatWithNullHeaderComments.getHeaderComments();

        assertNull(returnedComments);
    }
}