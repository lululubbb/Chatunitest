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

class CSVFormatHashCodeTest {

    @Test
    @Timeout(8000)
    void testHashCode_allFieldsDefault() {
        CSVFormat format = CSVFormat.DEFAULT;
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_differentDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';');
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_nullQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_nullQuoteCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote((Character) null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_nullCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker((Character) null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_nullEscapeCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_nullStringSet() {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("nullValue");
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_booleanFlags() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(true)
                .withIgnoreEmptyLines(true)
                .withSkipHeaderRecord(true);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_recordSeparatorNull() {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator((String) null);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_headerArray() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("a", "b", "c");
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    private int computeExpectedHashCode(CSVFormat format) {
        try {
            final int prime = 31;
            int result = 1;
            Class<?> clazz = CSVFormat.class;

            Field delimiterField = clazz.getDeclaredField("delimiter");
            delimiterField.setAccessible(true);
            char delimiter = delimiterField.getChar(format);
            result = prime * result + delimiter;

            Field quoteModeField = clazz.getDeclaredField("quoteMode");
            quoteModeField.setAccessible(true);
            Object quoteMode = quoteModeField.get(format);
            result = prime * result + (quoteMode == null ? 0 : quoteMode.hashCode());

            Field quoteCharacterField = clazz.getDeclaredField("quoteCharacter");
            quoteCharacterField.setAccessible(true);
            Object quoteCharacter = quoteCharacterField.get(format);
            result = prime * result + (quoteCharacter == null ? 0 : quoteCharacter.hashCode());

            Field commentMarkerField = clazz.getDeclaredField("commentMarker");
            commentMarkerField.setAccessible(true);
            Object commentMarker = commentMarkerField.get(format);
            result = prime * result + (commentMarker == null ? 0 : commentMarker.hashCode());

            Field escapeCharacterField = clazz.getDeclaredField("escapeCharacter");
            escapeCharacterField.setAccessible(true);
            Object escapeCharacter = escapeCharacterField.get(format);
            result = prime * result + (escapeCharacter == null ? 0 : escapeCharacter.hashCode());

            Field nullStringField = clazz.getDeclaredField("nullString");
            nullStringField.setAccessible(true);
            Object nullString = nullStringField.get(format);
            result = prime * result + (nullString == null ? 0 : nullString.hashCode());

            Field ignoreSurroundingSpacesField = clazz.getDeclaredField("ignoreSurroundingSpaces");
            ignoreSurroundingSpacesField.setAccessible(true);
            boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(format);
            result = prime * result + (ignoreSurroundingSpaces ? 1231 : 1237);

            Field ignoreHeaderCaseField = clazz.getDeclaredField("ignoreHeaderCase");
            ignoreHeaderCaseField.setAccessible(true);
            boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(format);
            result = prime * result + (ignoreHeaderCase ? 1231 : 1237);

            Field ignoreEmptyLinesField = clazz.getDeclaredField("ignoreEmptyLines");
            ignoreEmptyLinesField.setAccessible(true);
            boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(format);
            result = prime * result + (ignoreEmptyLines ? 1231 : 1237);

            Field skipHeaderRecordField = clazz.getDeclaredField("skipHeaderRecord");
            skipHeaderRecordField.setAccessible(true);
            boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(format);
            result = prime * result + (skipHeaderRecord ? 1231 : 1237);

            Field recordSeparatorField = clazz.getDeclaredField("recordSeparator");
            recordSeparatorField.setAccessible(true);
            Object recordSeparator = recordSeparatorField.get(format);
            result = prime * result + (recordSeparator == null ? 0 : recordSeparator.hashCode());

            Field headerField = clazz.getDeclaredField("header");
            headerField.setAccessible(true);
            Object headerObj = headerField.get(format);
            String[] header = (String[]) headerObj;
            result = prime * result + Arrays.hashCode(header);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}