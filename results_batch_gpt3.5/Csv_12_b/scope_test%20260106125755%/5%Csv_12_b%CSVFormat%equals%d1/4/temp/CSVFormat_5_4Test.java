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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord, boolean allowMissingColumnNames) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class,
                QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header,
                skipHeaderRecord, allowMissingColumnNames);
    }

    @Test
    @Timeout(8000)
    void testEquals_SameInstance() {
        assertTrue(CSVFormat.DEFAULT.equals(CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    void testEquals_Null() {
        assertFalse(CSVFormat.DEFAULT.equals(null));
    }

    @Test
    @Timeout(8000)
    void testEquals_DifferentClass() {
        assertFalse(CSVFormat.DEFAULT.equals("some string"));
    }

    @Test
    @Timeout(8000)
    void testEquals_DifferentDelimiter() throws Exception {
        CSVFormat other = createCSVFormat((char) (CSVFormat.DEFAULT.getDelimiter() + 1),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEquals_DifferentQuoteMode() throws Exception {
        QuoteMode differentQuoteMode = CSVFormat.DEFAULT.getQuoteMode() == QuoteMode.MINIMAL ? QuoteMode.ALL : QuoteMode.MINIMAL;
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), differentQuoteMode,
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEquals_QuoteCharacterNullMismatch() throws Exception {
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                null, CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEquals_QuoteCharacterMismatch() throws Exception {
        Character differentQuoteChar = CSVFormat.DEFAULT.getQuoteCharacter() == null ? '"' : (char) (CSVFormat.DEFAULT.getQuoteCharacter() + 1);
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                differentQuoteChar, CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEquals_CommentMarkerNullMismatch() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                null, CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                '!', CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_CommentMarkerMismatch() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                '!', CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                '#', CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_EscapeCharacterNullMismatch() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), null,
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), '\\',
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_EscapeCharacterMismatch() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), '\\',
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), '/',
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_NullStringNullMismatch() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), null,
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), "null",
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_NullStringMismatch() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), "null1",
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), "null2",
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_HeaderMismatch() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                new String[]{"A", "B"}, CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                new String[]{"A", "C"}, CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_IgnoreSurroundingSpacesMismatch() throws Exception {
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                !CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEquals_IgnoreEmptyLinesMismatch() throws Exception {
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), !CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEquals_SkipHeaderRecordMismatch() throws Exception {
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), !CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEquals_RecordSeparatorNullMismatch() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                null, CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                "\n", CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_RecordSeparatorMismatch() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                "\r\n", CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                "\n", CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_EqualObjects() throws Exception {
        CSVFormat format1 = createCSVFormat(';', '"', QuoteMode.ALL, '#', '\\', true,
                false, "\r\n", "NULL", new String[]{"h1", "h2"}, true, true);
        CSVFormat format2 = createCSVFormat(';', '"', QuoteMode.ALL, '#', '\\', true,
                false, "\r\n", "NULL", new String[]{"h1", "h2"}, true, true);
        assertTrue(format1.equals(format2));
        assertTrue(format2.equals(format1));
    }
}