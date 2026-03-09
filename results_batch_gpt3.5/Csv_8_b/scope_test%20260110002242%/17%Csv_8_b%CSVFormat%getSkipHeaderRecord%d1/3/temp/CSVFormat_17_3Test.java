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
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class CSVFormat_17_3Test {

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_defaultFalse() throws Exception {
        // Using DEFAULT instance where skipHeaderRecord is false
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_trueViaWithSkipHeaderRecord() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertTrue(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_reflectionSetTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Get all fields needed for constructor
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);
        Field quotePolicyField = CSVFormat.class.getDeclaredField("quotePolicy");
        quotePolicyField.setAccessible(true);
        Field commentStartField = CSVFormat.class.getDeclaredField("commentStart");
        commentStartField.setAccessible(true);
        Field escapeField = CSVFormat.class.getDeclaredField("escape");
        escapeField.setAccessible(true);
        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);

        char delimiter = delimiterField.getChar(format);
        Character quoteChar = (Character) quoteCharField.get(format);
        Object quotePolicy = quotePolicyField.get(format);
        Character commentStart = (Character) commentStartField.get(format);
        Character escape = (Character) escapeField.get(format);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(format);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(format);
        String recordSeparator = (String) recordSeparatorField.get(format);
        String nullString = (String) nullStringField.get(format);
        String[] header = (String[]) headerField.get(format);

        // Create new instance via private constructor with skipHeaderRecord = true
        java.lang.reflect.Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Object.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat newFormat = constructor.newInstance(
                delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator,
                nullString, header, true);

        assertTrue(newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_reflectionSetFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);

        // Get all fields needed for constructor
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);
        Field quotePolicyField = CSVFormat.class.getDeclaredField("quotePolicy");
        quotePolicyField.setAccessible(true);
        Field commentStartField = CSVFormat.class.getDeclaredField("commentStart");
        commentStartField.setAccessible(true);
        Field escapeField = CSVFormat.class.getDeclaredField("escape");
        escapeField.setAccessible(true);
        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);

        char delimiter = delimiterField.getChar(format);
        Character quoteChar = (Character) quoteCharField.get(format);
        Object quotePolicy = quotePolicyField.get(format);
        Character commentStart = (Character) commentStartField.get(format);
        Character escape = (Character) escapeField.get(format);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(format);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(format);
        String recordSeparator = (String) recordSeparatorField.get(format);
        String nullString = (String) nullStringField.get(format);
        String[] header = (String[]) headerField.get(format);

        // Create new instance via private constructor with skipHeaderRecord = false
        java.lang.reflect.Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Object.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat newFormat = constructor.newInstance(
                delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator,
                nullString, header, false);

        assertFalse(newFormat.getSkipHeaderRecord());
    }
}