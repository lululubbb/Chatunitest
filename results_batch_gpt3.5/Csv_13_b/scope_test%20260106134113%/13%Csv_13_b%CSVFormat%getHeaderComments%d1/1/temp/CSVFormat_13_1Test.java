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

public class CSVFormat_13_1Test {

    @Test
    @Timeout(8000)
    public void testGetHeaderComments_NullHeaderComments() throws Exception {
        CSVFormat format = createCSVFormatWithHeaderComments(null);
        String[] result = format.getHeaderComments();
        assertNull(result, "Expected null when headerComments field is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderComments_NonNullHeaderComments() throws Exception {
        String[] original = new String[] { "comment1", "comment2" };
        CSVFormat format = createCSVFormatWithHeaderComments(original);
        String[] result = format.getHeaderComments();
        assertNotNull(result, "Expected non-null array");
        assertArrayEquals(original, result, "Expected array contents to match");
        assertNotSame(original, result, "Expected cloned array, not the same instance");
    }

    private CSVFormat createCSVFormatWithHeaderComments(String[] headerComments) throws Exception {
        // Create a new CSVFormat instance by copying DEFAULT and replacing headerComments via reflection
        CSVFormat original = CSVFormat.DEFAULT;

        // Prepare parameters for constructor
        char delimiter = getFieldValue(original, "delimiter");
        Character quoteCharacter = getFieldValue(original, "quoteCharacter");
        QuoteMode quoteMode = getFieldValue(original, "quoteMode");
        Character commentMarker = getFieldValue(original, "commentMarker");
        Character escapeCharacter = getFieldValue(original, "escapeCharacter");
        boolean ignoreSurroundingSpaces = getFieldValue(original, "ignoreSurroundingSpaces");
        boolean ignoreEmptyLines = getFieldValue(original, "ignoreEmptyLines");
        String recordSeparator = getFieldValue(original, "recordSeparator");
        String nullString = getFieldValue(original, "nullString");
        String[] header = getFieldValue(original, "header");
        boolean skipHeaderRecord = getFieldValue(original, "skipHeaderRecord");
        boolean allowMissingColumnNames = getFieldValue(original, "allowMissingColumnNames");
        boolean ignoreHeaderCase = getFieldValue(original, "ignoreHeaderCase");

        // The private constructor signature:
        // CSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
        // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        // boolean ignoreEmptyLines, String recordSeparator, String nullString,
        // Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase)

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        Object[] headerCommentsObj = headerComments == null ? null : headerComments.clone();

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
                ignoreHeaderCase);
    }

    @SuppressWarnings("unchecked")
    private <T> T getFieldValue(Object obj, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(obj);
    }
}