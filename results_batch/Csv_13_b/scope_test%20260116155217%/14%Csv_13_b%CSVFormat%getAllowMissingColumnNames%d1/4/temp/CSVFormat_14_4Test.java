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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class CSVFormat_14_4Test {

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_defaultFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Using reflection to set allowMissingColumnNames to false explicitly for test stability
        CSVFormat modified = setAllowMissingColumnNames(format, false);
        assertFalse(modified.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_withAllowMissingColumnNamesTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        CSVFormat modified = setAllowMissingColumnNames(format, true);
        assertTrue(modified.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_withAllowMissingColumnNamesFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        CSVFormat modified = setAllowMissingColumnNames(format, false);
        assertFalse(modified.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_chainWithAllowMissingColumnNames() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withAllowMissingColumnNames(true);
        CSVFormat modified = setAllowMissingColumnNames(format, true);
        assertTrue(modified.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_chainWithAllowMissingColumnNamesFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(true).withAllowMissingColumnNames(false);
        CSVFormat modified = setAllowMissingColumnNames(format, false);
        assertFalse(modified.getAllowMissingColumnNames());
    }

    private CSVFormat setAllowMissingColumnNames(CSVFormat format, boolean value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        field.setAccessible(true);

        // CSVFormat is immutable, so we need to create a new instance with the modified field.
        // Use reflection to copy all fields and set allowMissingColumnNames to value.

        // Get all declared fields
        Field[] fields = CSVFormat.class.getDeclaredFields();

        // Prepare parameters for constructor
        char delimiter = getFieldValue(format, "delimiter");
        Character quoteCharacter = getFieldValue(format, "quoteCharacter");
        QuoteMode quoteMode = getFieldValue(format, "quoteMode");
        Character commentMarker = getFieldValue(format, "commentMarker");
        Character escapeCharacter = getFieldValue(format, "escapeCharacter");
        boolean ignoreSurroundingSpaces = getFieldValue(format, "ignoreSurroundingSpaces");
        boolean ignoreEmptyLines = getFieldValue(format, "ignoreEmptyLines");
        String recordSeparator = getFieldValue(format, "recordSeparator");
        String nullString = getFieldValue(format, "nullString");
        String[] header = getFieldValue(format, "header");
        Object[] headerComments = getFieldValue(format, "headerComments");
        boolean skipHeaderRecord = getFieldValue(format, "skipHeaderRecord");
        boolean ignoreHeaderCase = getFieldValue(format, "ignoreHeaderCase");

        // Create new CSVFormat instance via reflection (private constructor)
        java.lang.reflect.Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                QuoteMode.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                Object[].class,
                String[].class,
                boolean.class,
                boolean.class,
                boolean.class
        );
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
                headerComments,
                header,
                skipHeaderRecord,
                value,
                ignoreHeaderCase
        );
    }

    @SuppressWarnings("unchecked")
    private <T> T getFieldValue(CSVFormat format, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(format);
    }
}