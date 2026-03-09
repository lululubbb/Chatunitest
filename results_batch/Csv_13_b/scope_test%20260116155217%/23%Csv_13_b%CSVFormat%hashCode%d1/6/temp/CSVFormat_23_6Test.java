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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.Arrays;

public class CSVFormat_23_6Test {

    @Test
    @Timeout(8000)
    public void testHashCode_allFieldsDefault() {
        CSVFormat format = CSVFormat.DEFAULT;
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';');
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_nullQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_nullQuoteCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote((Character) null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_nullCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker((Character) null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_nullEscapeCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_nullNullString() {
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_ignoreSurroundingSpacesTrueFalse() {
        CSVFormat formatTrue = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        CSVFormat formatFalse = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);
        assertNotEquals(formatTrue.hashCode(), formatFalse.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_ignoreHeaderCaseTrueFalse() {
        CSVFormat formatTrue = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
        CSVFormat formatFalse = CSVFormat.DEFAULT.withIgnoreHeaderCase(false);
        assertNotEquals(formatTrue.hashCode(), formatFalse.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_ignoreEmptyLinesTrueFalse() {
        CSVFormat formatTrue = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        CSVFormat formatFalse = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        assertNotEquals(formatTrue.hashCode(), formatFalse.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_skipHeaderRecordTrueFalse() {
        CSVFormat formatTrue = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat formatFalse = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        assertNotEquals(formatTrue.hashCode(), formatFalse.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_nullRecordSeparator() {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator((String) null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_headerArray() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("a", "b", "c");
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    private int computeExpectedHashCode(CSVFormat format) {
        try {
            final int prime = 31;
            int result = 1;

            char delimiter = (char) getField(format, "delimiter");
            QuoteMode quoteMode = (QuoteMode) getField(format, "quoteMode");
            Character quoteCharacter = (Character) getField(format, "quoteCharacter");
            Character commentMarker = (Character) getField(format, "commentMarker");
            Character escapeCharacter = (Character) getField(format, "escapeCharacter");
            String nullString = (String) getField(format, "nullString");
            boolean ignoreSurroundingSpaces = (boolean) getField(format, "ignoreSurroundingSpaces");
            boolean ignoreHeaderCase = (boolean) getField(format, "ignoreHeaderCase");
            boolean ignoreEmptyLines = (boolean) getField(format, "ignoreEmptyLines");
            boolean skipHeaderRecord = (boolean) getField(format, "skipHeaderRecord");
            String recordSeparator = (String) getField(format, "recordSeparator");
            String[] header = (String[]) getField(format, "header");

            result = prime * result + delimiter;
            result = prime * result + ((quoteMode == null) ? 0 : quoteMode.hashCode());
            result = prime * result + ((quoteCharacter == null) ? 0 : quoteCharacter.hashCode());
            result = prime * result + ((commentMarker == null) ? 0 : commentMarker.hashCode());
            result = prime * result + ((escapeCharacter == null) ? 0 : escapeCharacter.hashCode());
            result = prime * result + ((nullString == null) ? 0 : nullString.hashCode());
            result = prime * result + (ignoreSurroundingSpaces ? 1231 : 1237);
            result = prime * result + (ignoreHeaderCase ? 1231 : 1237);
            result = prime * result + (ignoreEmptyLines ? 1231 : 1237);
            result = prime * result + (skipHeaderRecord ? 1231 : 1237);
            result = prime * result + ((recordSeparator == null) ? 0 : recordSeparator.hashCode());
            result = prime * result + Arrays.hashCode(header);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getField(Object obj, String fieldName) throws Exception {
        Field field = null;
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        if (field == null) {
            throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy");
        }
        field.setAccessible(true);
        return field.get(obj);
    }
}