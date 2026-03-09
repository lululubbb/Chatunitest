package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

class CSVFormatHashCodeTest {

    @Test
    @Timeout(8000)
    void testHashCode_DefaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_DifferentDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';');
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_NullQuoteModeAndQuoteCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(null).withQuote(null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_NullCommentMarkerAndEscapeCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker((Character) null).withEscape((Character) null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_NullNullStringAndRecordSeparator() {
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null).withRecordSeparator((String) null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_HeaderArray() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("A", "B", "C");
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_AllBooleanFlagsTrue() {
        CSVFormat format = CSVFormat.DEFAULT
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(true)
                .withIgnoreEmptyLines(true)
                .withSkipHeaderRecord(true);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    // Utility method to compute expected hash code using reflection to access private fields
    private int computeExpectedHashCode(CSVFormat format) {
        try {
            final int prime = 31;
            int result = 1;

            char delimiter = getCharField(format, "delimiter");
            QuoteMode quoteMode = (QuoteMode) getField(format, "quoteMode");
            Character quoteCharacter = (Character) getField(format, "quoteCharacter");
            Character commentMarker = (Character) getField(format, "commentMarker");
            Character escapeCharacter = (Character) getField(format, "escapeCharacter");
            String nullString = (String) getField(format, "nullString");
            boolean ignoreSurroundingSpaces = getBooleanField(format, "ignoreSurroundingSpaces");
            boolean ignoreHeaderCase = getBooleanField(format, "ignoreHeaderCase");
            boolean ignoreEmptyLines = getBooleanField(format, "ignoreEmptyLines");
            boolean skipHeaderRecord = getBooleanField(format, "skipHeaderRecord");
            String recordSeparator = (String) getField(format, "recordSeparator");
            String[] header = (String[]) getField(format, "header");

            result = prime * result + delimiter;
            result = prime * result + (quoteMode == null ? 0 : quoteMode.hashCode());
            result = prime * result + (quoteCharacter == null ? 0 : quoteCharacter.hashCode());
            result = prime * result + (commentMarker == null ? 0 : commentMarker.hashCode());
            result = prime * result + (escapeCharacter == null ? 0 : escapeCharacter.hashCode());
            result = prime * result + (nullString == null ? 0 : nullString.hashCode());
            result = prime * result + (ignoreSurroundingSpaces ? 1231 : 1237);
            result = prime * result + (ignoreHeaderCase ? 1231 : 1237);
            result = prime * result + (ignoreEmptyLines ? 1231 : 1237);
            result = prime * result + (skipHeaderRecord ? 1231 : 1237);
            result = prime * result + (recordSeparator == null ? 0 : recordSeparator.hashCode());
            result = prime * result + Arrays.hashCode(header);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getField(Object obj, String fieldName) throws Exception {
        Field field = findField(obj.getClass(), fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    private char getCharField(Object obj, String fieldName) throws Exception {
        Field field = findField(obj.getClass(), fieldName);
        field.setAccessible(true);
        return field.getChar(obj);
    }

    private boolean getBooleanField(Object obj, String fieldName) throws Exception {
        Field field = findField(obj.getClass(), fieldName);
        field.setAccessible(true);
        return field.getBoolean(obj);
    }

    // Helper to find field in class hierarchy
    private Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
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