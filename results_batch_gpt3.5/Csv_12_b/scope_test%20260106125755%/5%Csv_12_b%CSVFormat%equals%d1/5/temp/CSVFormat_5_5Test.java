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
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Arrays;

public class CSVFormat_5_5Test {

    @Test
    @Timeout(8000)
    public void testEquals_SameObject_ReturnsTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    public void testEquals_NullObject_ReturnsFalse() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentClass_ReturnsFalse() {
        CSVFormat format = CSVFormat.DEFAULT;
        Object other = new Object();
        assertFalse(format.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiter_ReturnsFalse() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("delimiter", (char)(format1.getDelimiter() + 1));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentQuoteMode_ReturnsFalse() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        QuoteMode differentQuoteMode = format1.getQuoteMode() == QuoteMode.ALL ? QuoteMode.MINIMAL : QuoteMode.ALL;
        CSVFormat format2 = createCSVFormatWithField("quoteMode", differentQuoteMode);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_QuoteCharacterMismatch_ReturnsFalse() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("quoteCharacter", differentCharacter(format1.getQuoteCharacter()));
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormatWithField("quoteCharacter", null);
        CSVFormat format4 = createCSVFormatWithField("quoteCharacter", 'Q');
        assertFalse(format3.equals(format4));
        assertFalse(format4.equals(format3));
    }

    @Test
    @Timeout(8000)
    public void testEquals_CommentMarkerMismatch_ReturnsFalse() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("commentMarker", differentCharacter(format1.getCommentMarker()));
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormatWithField("commentMarker", null);
        CSVFormat format4 = createCSVFormatWithField("commentMarker", 'C');
        assertFalse(format3.equals(format4));
        assertFalse(format4.equals(format3));
    }

    @Test
    @Timeout(8000)
    public void testEquals_EscapeCharacterMismatch_ReturnsFalse() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("escapeCharacter", differentCharacter(format1.getEscapeCharacter()));
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormatWithField("escapeCharacter", null);
        CSVFormat format4 = createCSVFormatWithField("escapeCharacter", 'E');
        assertFalse(format3.equals(format4));
        assertFalse(format4.equals(format3));
    }

    @Test
    @Timeout(8000)
    public void testEquals_NullStringMismatch_ReturnsFalse() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormatWithField("nullString", "differentNullString");
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormatWithField("nullString", null);
        CSVFormat format4 = createCSVFormatWithField("nullString", "null");
        assertFalse(format3.equals(format4));
        assertFalse(format4.equals(format3));
    }

    @Test
    @Timeout(8000)
    public void testEquals_HeaderMismatch_ReturnsFalse() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT.withHeader("a", "b");
        CSVFormat format2 = CSVFormat.DEFAULT.withHeader("a", "c");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_IgnoreSurroundingSpacesMismatch_ReturnsFalse() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        CSVFormat format2 = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_IgnoreEmptyLinesMismatch_ReturnsFalse() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        CSVFormat format2 = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_SkipHeaderRecordMismatch_ReturnsFalse() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat format2 = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_RecordSeparatorMismatch_ReturnsFalse() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT.withRecordSeparator("\n");
        CSVFormat format2 = CSVFormat.DEFAULT.withRecordSeparator("\r");
        assertFalse(format1.equals(format2));

        CSVFormat format3 = CSVFormat.DEFAULT.withRecordSeparator(null);
        CSVFormat format4 = CSVFormat.DEFAULT.withRecordSeparator("\r\n");
        assertFalse(format3.equals(format4));
        assertFalse(format4.equals(format3));
    }

    @Test
    @Timeout(8000)
    public void testEquals_AllFieldsEqual_ReturnsTrue() {
        CSVFormat format1 = CSVFormat.DEFAULT.withHeader("a", "b").withIgnoreEmptyLines(true)
                .withSkipHeaderRecord(false).withNullString("null").withCommentMarker('#')
                .withEscape('\\').withQuoteMode(QuoteMode.ALL).withQuote('\'').withRecordSeparator("\r\n")
                .withIgnoreSurroundingSpaces(true);

        CSVFormat format2 = CSVFormat.DEFAULT.withHeader("a", "b").withIgnoreEmptyLines(true)
                .withSkipHeaderRecord(false).withNullString("null").withCommentMarker('#')
                .withEscape('\\').withQuoteMode(QuoteMode.ALL).withQuote('\'').withRecordSeparator("\r\n")
                .withIgnoreSurroundingSpaces(true);

        assertTrue(format1.equals(format2));
        assertTrue(format2.equals(format1));
    }

    // Helper method to create a CSVFormat instance with one field modified using reflection
    private CSVFormat createCSVFormatWithField(String fieldName, Object value) throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Class<?> clazz = CSVFormat.class;
        // Use constructor to create a new instance copying all fields from original
        CSVFormat copy = new CSVFormat(
                getCharField(original, "delimiter"),
                getCharacterField(original, "quoteCharacter"),
                (QuoteMode) getField(original, "quoteMode"),
                getCharacterField(original, "commentMarker"),
                getCharacterField(original, "escapeCharacter"),
                (boolean) getField(original, "ignoreSurroundingSpaces"),
                (boolean) getField(original, "ignoreEmptyLines"),
                (String) getField(original, "recordSeparator"),
                (String) getField(original, "nullString"),
                (String[]) getField(original, "header"),
                (boolean) getField(original, "skipHeaderRecord"),
                (boolean) getField(original, "allowMissingColumnNames")
        );
        // Set the specified field to the given value
        java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(copy, value);
        return copy;
    }

    private Object getField(CSVFormat instance, String fieldName) throws Exception {
        java.lang.reflect.Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    private char getCharField(CSVFormat instance, String fieldName) throws Exception {
        return (char) getField(instance, fieldName);
    }

    private Character getCharacterField(CSVFormat instance, String fieldName) throws Exception {
        return (Character) getField(instance, fieldName);
    }

    private Character differentCharacter(Character c) {
        if (c == null) {
            return 'X';
        }
        char ch = c.charValue();
        ch = (char) (ch == Character.MAX_VALUE ? ch - 1 : ch + 1);
        return ch;
    }
}