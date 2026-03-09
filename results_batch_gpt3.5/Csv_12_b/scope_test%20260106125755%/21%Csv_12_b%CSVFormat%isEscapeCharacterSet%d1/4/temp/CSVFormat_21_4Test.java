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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class CSVFormat_21_4Test {

    private CSVFormat setFinalFieldReturningNewInstance(CSVFormat target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field (works for JDK 8 and below)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // Since CSVFormat is immutable, create a new instance with the changed field value
        // We do this by creating a new CSVFormat instance with the same values as target,
        // but with the escapeCharacter replaced by the new value

        // Get all fields needed for constructor
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(target);

        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        Character quoteCharacter = (Character) quoteCharacterField.get(target);

        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        Object quoteMode = quoteModeField.get(target);

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(target);

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(target);

        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(target);

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(target);

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(target);

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullString = (String) nullStringField.get(target);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(target);

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(target);

        // Create new CSVFormat instance via constructor
        // Constructor signature:
        // CSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode, Character commentStart,
        //           Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
        //           String recordSeparator, String nullString, String[] header,
        //           boolean skipHeaderRecord, boolean allowMissingColumnNames)

        // We need to cast quoteMode to QuoteMode
        QuoteMode qm = (QuoteMode) quoteMode;

        // Use reflection to invoke private constructor
        java.lang.reflect.Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter,
                quoteCharacter,
                qm,
                commentMarker,
                (Character) value,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                header,
                skipHeaderRecord,
                allowMissingColumnNames
        );
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSetWhenEscapeCharacterIsNull() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        format = setFinalFieldReturningNewInstance(format, "escapeCharacter", null);

        assertFalse(format.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSetWhenEscapeCharacterIsSet() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        format = setFinalFieldReturningNewInstance(format, "escapeCharacter", Character.valueOf('\\'));

        assertTrue(format.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSetUsingWithEscapeCharMethod() {
        CSVFormat format = CSVFormat.newFormat(',').withEscape('\\');
        assertTrue(format.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSetUsingWithEscapeCharacterNull() {
        CSVFormat format = CSVFormat.newFormat(',').withEscape((Character) null);
        assertFalse(format.isEscapeCharacterSet());
    }
}