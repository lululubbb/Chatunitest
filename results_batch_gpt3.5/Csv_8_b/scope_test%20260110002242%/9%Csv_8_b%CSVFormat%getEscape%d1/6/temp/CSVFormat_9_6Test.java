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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.Quote;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormat_9_6Test {

    @Test
    @Timeout(8000)
    void testGetEscapeWhenEscapeIsNull() throws Exception {
        // Use reflection to create a new CSVFormat instance with escape = null
        CSVFormat format = createCSVFormatWithEscape(null);
        assertNull(format.getEscape());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeWhenEscapeIsSet() throws Exception {
        Character escapeChar = '\\';
        CSVFormat format = createCSVFormatWithEscape(escapeChar);
        assertEquals(escapeChar, format.getEscape());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeDefault() {
        // DEFAULT escape is null according to constructor call in DEFAULT
        assertNull(CSVFormat.DEFAULT.getEscape());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeMYSQL() {
        // MYSQL sets escape to BACKSLASH
        Character expected = '\\';
        assertEquals(expected, CSVFormat.MYSQL.getEscape());
    }

    // Helper method to create CSVFormat with given escape character using reflection
    private CSVFormat createCSVFormatWithEscape(Character escape) throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;

        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        Field delimiterField = clazz.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(defaultFormat);

        Field quoteCharField = clazz.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);
        Character quoteChar = (Character) quoteCharField.get(defaultFormat);

        Field quotePolicyField = clazz.getDeclaredField("quotePolicy");
        quotePolicyField.setAccessible(true);
        Quote quotePolicy = (Quote) quotePolicyField.get(defaultFormat);

        Field commentStartField = clazz.getDeclaredField("commentStart");
        commentStartField.setAccessible(true);
        Character commentStart = (Character) commentStartField.get(defaultFormat);

        Field ignoreSurroundingSpacesField = clazz.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(defaultFormat);

        Field ignoreEmptyLinesField = clazz.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(defaultFormat);

        Field recordSeparatorField = clazz.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(defaultFormat);

        Field nullStringField = clazz.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullString = (String) nullStringField.get(defaultFormat);

        Field headerField = clazz.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(defaultFormat);

        Field skipHeaderRecordField = clazz.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(defaultFormat);

        return constructor.newInstance(
                delimiter,
                quoteChar,
                quotePolicy,
                commentStart,
                escape,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                header,
                skipHeaderRecord
        );
    }
}