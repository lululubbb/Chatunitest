package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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

class CSVFormat_13_6Test {

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsWhenNull() throws Exception {
        // Create a new CSVFormat instance with headerComments set to null using reflection
        CSVFormat newFormat = createCSVFormatWithHeaderComments(null);

        String[] result = newFormat.getHeaderComments();
        assertNull(result, "Expected null when headerComments is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsClone() throws Exception {
        String[] comments = new String[]{"comment1", "comment2"};

        // Create a new CSVFormat instance with headerComments set to comments using reflection
        CSVFormat format = createCSVFormatWithHeaderComments(comments);

        String[] result = format.getHeaderComments();

        assertNotNull(result, "Expected non-null header comments");
        assertArrayEquals(comments, result, "Returned header comments should match original");
        assertNotSame(comments, result, "Returned array should be a clone, not the same reference");

        // Modify returned array and check original is not affected
        result[0] = "modified";

        String[] originalAfterModification = format.getHeaderComments();
        assertEquals("comment1", originalAfterModification[0], "Original headerComments should not be affected by modifications on returned array");
    }

    // Helper method to create a CSVFormat instance with a given headerComments array using reflection
    private CSVFormat createCSVFormatWithHeaderComments(String[] headerComments) throws Exception {
        // Access the static DEFAULT field
        Field defaultField = CSVFormat.class.getDeclaredField("DEFAULT");
        defaultField.setAccessible(true);
        CSVFormat defaultInstance = (CSVFormat) defaultField.get(null);

        // Access all required fields from the defaultInstance
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        Field headerField = CSVFormat.class.getDeclaredField("header");
        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        Field trimField = CSVFormat.class.getDeclaredField("trim");
        Field trailingDelimiterField = CSVFormat.class.getDeclaredField("trailingDelimiter");
        Field autoFlushField = CSVFormat.class.getDeclaredField("autoFlush");

        delimiterField.setAccessible(true);
        quoteCharacterField.setAccessible(true);
        quoteModeField.setAccessible(true);
        commentMarkerField.setAccessible(true);
        escapeCharacterField.setAccessible(true);
        ignoreSurroundingSpacesField.setAccessible(true);
        ignoreEmptyLinesField.setAccessible(true);
        recordSeparatorField.setAccessible(true);
        nullStringField.setAccessible(true);
        headerCommentsField.setAccessible(true);
        headerField.setAccessible(true);
        skipHeaderRecordField.setAccessible(true);
        allowMissingColumnNamesField.setAccessible(true);
        ignoreHeaderCaseField.setAccessible(true);
        trimField.setAccessible(true);
        trailingDelimiterField.setAccessible(true);
        autoFlushField.setAccessible(true);

        char delimiter = delimiterField.getChar(defaultInstance);
        Character quoteCharacter = (Character) quoteCharacterField.get(defaultInstance);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(defaultInstance);
        Character commentMarker = (Character) commentMarkerField.get(defaultInstance);
        Character escapeCharacter = (Character) escapeCharacterField.get(defaultInstance);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(defaultInstance);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(defaultInstance);
        String recordSeparator = (String) recordSeparatorField.get(defaultInstance);
        String nullString = (String) nullStringField.get(defaultInstance);
        // Use the provided headerComments parameter here instead of the defaultInstance's field
        // String[] headerCommentsValue = (String[]) headerCommentsField.get(defaultInstance);
        String[] header = (String[]) headerField.get(defaultInstance);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(defaultInstance);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(defaultInstance);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(defaultInstance);
        boolean trim = trimField.getBoolean(defaultInstance);
        boolean trailingDelimiter = trailingDelimiterField.getBoolean(defaultInstance);
        boolean autoFlush = autoFlushField.getBoolean(defaultInstance);

        // Get the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);

        constructor.setAccessible(true);

        // headerComments parameter is Object[], cast String[] to Object[]
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
                skipHeaderRecord,
                allowMissingColumnNames,
                ignoreHeaderCase,
                trim,
                trailingDelimiter,
                autoFlush
        );
    }
}