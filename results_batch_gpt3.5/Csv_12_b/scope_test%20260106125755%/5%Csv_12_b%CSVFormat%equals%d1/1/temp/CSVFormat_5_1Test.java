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
import java.lang.reflect.Field;
import java.util.Arrays;

public class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord) throws Exception {
        // Use reflection to invoke the private constructor
        var constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        return (CSVFormat) constructor.newInstance(
                delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header,
                skipHeaderRecord, false);
    }

    @Test
    @Timeout(8000)
    void testEqualsSameInstance() {
        assertTrue(CSVFormat.DEFAULT.equals(CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    void testEqualsNull() {
        assertFalse(CSVFormat.DEFAULT.equals(null));
    }

    @Test
    @Timeout(8000)
    void testEqualsDifferentClass() {
        assertFalse(CSVFormat.DEFAULT.equals("some string"));
    }

    @Test
    @Timeout(8000)
    void testEqualsDifferentDelimiter() throws Exception {
        CSVFormat other = createCSVFormat((char) (CSVFormat.DEFAULT.getDelimiter() + 1),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEqualsDifferentQuoteMode() throws Exception {
        QuoteMode differentQuoteMode = CSVFormat.DEFAULT.getQuoteMode() == QuoteMode.ALL ?
                QuoteMode.MINIMAL : QuoteMode.ALL;
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), differentQuoteMode,
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEqualsQuoteCharacterNullMismatch() throws Exception {
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                null, CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));

        CSVFormat baseWithNullQuote = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                null, CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(baseWithNullQuote.equals(CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    void testEqualsQuoteCharacterMismatch() throws Exception {
        Character differentQuote = CSVFormat.DEFAULT.getQuoteCharacter() == null ? '"' : (char)(CSVFormat.DEFAULT.getQuoteCharacter() + 1);
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                differentQuote, CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEqualsCommentMarkerNullMismatch() throws Exception {
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                null, CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));

        CSVFormat baseWithNullComment = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                null, CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(baseWithNullComment.equals(CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    void testEqualsCommentMarkerMismatch() throws Exception {
        Character differentComment = CSVFormat.DEFAULT.getCommentMarker() == null ? '#' : (char)(CSVFormat.DEFAULT.getCommentMarker() + 1);
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                differentComment, CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEqualsEscapeCharacterNullMismatch() throws Exception {
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), null,
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));

        CSVFormat baseWithNullEscape = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), null,
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(baseWithNullEscape.equals(CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    void testEqualsEscapeCharacterMismatch() throws Exception {
        Character differentEscape = CSVFormat.DEFAULT.getEscapeCharacter() == null ? '\\' : (char)(CSVFormat.DEFAULT.getEscapeCharacter() + 1);
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), differentEscape,
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEqualsNullStringNullMismatch() throws Exception {
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), null,
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));

        CSVFormat baseWithNullNullString = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), null,
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(baseWithNullNullString.equals(CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    void testEqualsNullStringMismatch() throws Exception {
        String differentNullString = CSVFormat.DEFAULT.getNullString() == null ? "null" : CSVFormat.DEFAULT.getNullString() + "x";
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), differentNullString,
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEqualsHeaderMismatch() throws Exception {
        String[] differentHeader = CSVFormat.DEFAULT.getHeader() == null ? new String[]{"a"} : new String[]{"x"};
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                differentHeader, CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEqualsIgnoreSurroundingSpacesMismatch() throws Exception {
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                !CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEqualsIgnoreEmptyLinesMismatch() throws Exception {
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), !CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEqualsSkipHeaderRecordMismatch() throws Exception {
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), !CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEqualsRecordSeparatorNullMismatch() throws Exception {
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                null, CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));

        CSVFormat baseWithNullRecordSeparator = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                null, CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(baseWithNullRecordSeparator.equals(CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    void testEqualsRecordSeparatorMismatch() throws Exception {
        String differentRecordSeparator = CSVFormat.DEFAULT.getRecordSeparator() == null ? "\n" : CSVFormat.DEFAULT.getRecordSeparator() + "x";
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                differentRecordSeparator, CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    void testEqualsIdenticalFields() throws Exception {
        CSVFormat other = createCSVFormat(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertTrue(CSVFormat.DEFAULT.equals(other));
    }
}