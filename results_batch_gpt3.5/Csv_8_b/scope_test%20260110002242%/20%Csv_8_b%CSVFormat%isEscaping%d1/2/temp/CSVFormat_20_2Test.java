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

class CSVFormat_20_2Test {

    @Test
    @Timeout(8000)
    void testIsEscaping_whenEscapeIsNull_shouldReturnFalse() throws Exception {
        // Use DEFAULT which has escape == null
        CSVFormat format = CSVFormat.DEFAULT;

        // Remove final modifier and set escape to null via reflection
        Field escapeField = CSVFormat.class.getDeclaredField("escape");
        escapeField.setAccessible(true);

        // Remove final modifier (Java 12+ requires this workaround)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(escapeField, escapeField.getModifiers() & ~Modifier.FINAL);

        // Create a new CSVFormat instance with escape == null because escape is final and private
        // We cannot modify the existing instance's final field reliably, so create a new one via reflection
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = (char) delimiterField.get(format);

        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);
        Character quoteChar = (Character) quoteCharField.get(format);

        Field quotePolicyField = CSVFormat.class.getDeclaredField("quotePolicy");
        quotePolicyField.setAccessible(true);
        Object quotePolicy = quotePolicyField.get(format);

        Field commentStartField = CSVFormat.class.getDeclaredField("commentStart");
        commentStartField.setAccessible(true);
        Character commentStart = (Character) commentStartField.get(format);

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(format);

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(format);

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(format);

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullString = (String) nullStringField.get(format);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(format);

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(format);

        // Instantiate new CSVFormat with escape == null
        CSVFormat newFormat = (CSVFormat) CSVFormat.class
                .getDeclaredConstructor(char.class, Character.class, quotePolicy.getClass(), Character.class, Character.class,
                        boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class)
                .newInstance(delimiter, quoteChar, quotePolicy, commentStart, null,
                        ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);

        assertFalse(newFormat.isEscaping());
    }

    @Test
    @Timeout(8000)
    void testIsEscaping_whenEscapeIsNonNull_shouldReturnTrue() {
        // Create CSVFormat with escape character set
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        assertTrue(format.isEscaping());
    }

    @Test
    @Timeout(8000)
    void testIsEscaping_whenEscapeIsNonNull_CharacterObject_shouldReturnTrue() {
        // Create CSVFormat with escape Character object set
        CSVFormat format = CSVFormat.DEFAULT.withEscape(Character.valueOf('\\'));
        assertTrue(format.isEscaping());
    }
}