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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.Quote;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

class CSVFormatHashCodeTest {

    @Test
    @Timeout(8000)
    void testHashCode_defaults() {
        CSVFormat format = CSVFormat.DEFAULT;
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_withAllFieldsNonNull() throws Exception {
        CSVFormat format = createCSVFormatWithAllFields();
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_withNullFields() throws Exception {
        CSVFormat format = createCSVFormatWithNullFields();
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_booleanBranches() throws Exception {
        CSVFormat formatTrue = createCSVFormatWithBooleans(true, true, true);
        CSVFormat formatFalse = createCSVFormatWithBooleans(false, false, false);
        assertNotEquals(formatTrue.hashCode(), formatFalse.hashCode());
    }

    private CSVFormat createCSVFormatWithAllFields() throws Exception {
        CSVFormat format = CSVFormat.newFormat(';')
                .withQuoteChar('\'')
                .withQuotePolicy(Quote.ALL)
                .withCommentStart('#')
                .withEscape('\\')
                .withNullString("NULL")
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withHeader("h1", "h2")
                .withSkipHeaderRecord(true);

        // newFormat(char) returns a CSVFormat with quotePolicy null, so set via reflection
        setPrivateField(format, "quotePolicy", Quote.ALL);
        return format;
    }

    private CSVFormat createCSVFormatWithNullFields() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        // forcibly set all nullable fields to null and booleans to false to test null branches
        setPrivateField(format, "quotePolicy", null);
        setPrivateField(format, "quoteChar", null);
        setPrivateField(format, "commentStart", null);
        setPrivateField(format, "escape", null);
        setPrivateField(format, "nullString", null);
        setPrivateField(format, "recordSeparator", null);
        setPrivateField(format, "header", null);
        setPrivateField(format, "ignoreSurroundingSpaces", false);
        setPrivateField(format, "ignoreEmptyLines", false);
        setPrivateField(format, "skipHeaderRecord", false);
        return format;
    }

    private CSVFormat createCSVFormatWithBooleans(boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines, boolean skipHeaderRecord) throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        setPrivateField(format, "ignoreSurroundingSpaces", ignoreSurroundingSpaces);
        setPrivateField(format, "ignoreEmptyLines", ignoreEmptyLines);
        setPrivateField(format, "skipHeaderRecord", skipHeaderRecord);
        return format;
    }

    private int computeExpectedHashCode(CSVFormat format) {
        final int prime = 31;
        int result = 1;

        char delimiter = getPrivateField(format, "delimiter");
        Quote quotePolicy = getPrivateField(format, "quotePolicy");
        Character quoteChar = getPrivateField(format, "quoteChar");
        Character commentStart = getPrivateField(format, "commentStart");
        Character escape = getPrivateField(format, "escape");
        String nullString = getPrivateField(format, "nullString");
        boolean ignoreSurroundingSpaces = getPrivateField(format, "ignoreSurroundingSpaces");
        boolean ignoreEmptyLines = getPrivateField(format, "ignoreEmptyLines");
        boolean skipHeaderRecord = getPrivateField(format, "skipHeaderRecord");
        String recordSeparator = getPrivateField(format, "recordSeparator");
        String[] header = getPrivateField(format, "header");

        result = prime * result + delimiter;
        result = prime * result + (quotePolicy == null ? 0 : quotePolicy.hashCode());
        result = prime * result + (quoteChar == null ? 0 : quoteChar.hashCode());
        result = prime * result + (commentStart == null ? 0 : commentStart.hashCode());
        result = prime * result + (escape == null ? 0 : escape.hashCode());
        result = prime * result + (nullString == null ? 0 : nullString.hashCode());
        result = prime * result + (ignoreSurroundingSpaces ? 1231 : 1237);
        result = prime * result + (ignoreEmptyLines ? 1231 : 1237);
        result = prime * result + (skipHeaderRecord ? 1231 : 1237);
        result = prime * result + (recordSeparator == null ? 0 : recordSeparator.hashCode());
        result = prime * result + Arrays.hashCode(header);

        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object obj, String fieldName) {
        try {
            Field f = getFieldFromClassHierarchy(obj.getClass(), fieldName);
            f.setAccessible(true);
            return (T) f.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setPrivateField(Object obj, String fieldName, Object value) {
        try {
            Field f = getFieldFromClassHierarchy(obj.getClass(), fieldName);
            f.setAccessible(true);

            // Remove final modifier if present
            try {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
                // Ignore if unable to remove final modifier (Java 12+)
            }

            f.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy of " + clazz.getName());
    }
}