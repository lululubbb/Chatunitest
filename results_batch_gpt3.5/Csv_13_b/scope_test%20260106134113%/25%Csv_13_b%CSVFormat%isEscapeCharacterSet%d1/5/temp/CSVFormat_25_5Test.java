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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class CSVFormat_25_5Test {

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_whenEscapeCharacterIsNull() throws Exception {
        CSVFormat format = createCSVFormatWithEscapeCharacter(null);
        assertFalse(format.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_whenEscapeCharacterIsSet() throws Exception {
        Character escapeChar = '\\';
        CSVFormat format = createCSVFormatWithEscapeCharacter(escapeChar);
        assertTrue(format.isEscapeCharacterSet());
    }

    // Helper method to create CSVFormat instance with given escapeCharacter using reflection
    private CSVFormat createCSVFormatWithEscapeCharacter(Character escapeCharacter) throws Exception {
        // Use the public DEFAULT instance as base
        CSVFormat format = CSVFormat.DEFAULT;

        // CSVFormat has private final fields, so we create a new instance via reflection
        // Get constructor with all parameters
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class,
                QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);

        constructor.setAccessible(true);

        // Use values from DEFAULT instance for all fields except escapeCharacter
        Character quoteCharacter = getField(format, "quoteCharacter");
        QuoteMode quoteMode = getField(format, "quoteMode");
        Character commentMarker = getField(format, "commentMarker");
        boolean ignoreSurroundingSpaces = getField(format, "ignoreSurroundingSpaces");
        boolean allowMissingColumnNames = getField(format, "allowMissingColumnNames");
        boolean ignoreEmptyLines = getField(format, "ignoreEmptyLines");
        String recordSeparator = getField(format, "recordSeparator");
        String nullString = getField(format, "nullString");
        Object[] headerComments = getField(format, "headerComments");
        String[] header = getField(format, "header");
        boolean skipHeaderRecord = getField(format, "skipHeaderRecord");
        boolean ignoreHeaderCase = getField(format, "ignoreHeaderCase");
        char delimiter = getField(format, "delimiter");

        return constructor.newInstance(
                delimiter,
                quoteCharacter,
                quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                allowMissingColumnNames,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                headerComments,
                header,
                skipHeaderRecord,
                allowMissingColumnNames,
                ignoreHeaderCase
        );
    }

    @SuppressWarnings("unchecked")
    private <T> T getField(CSVFormat format, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(format);
    }
}