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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter, boolean ignoreSurroundingSpaces,
                                      boolean ignoreEmptyLines, String recordSeparator, String nullString,
                                      String[] header, boolean skipHeaderRecord) throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to create CSVFormat instances with specific fields because constructor is private
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        Field headerField = CSVFormat.class.getDeclaredField("header");
        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");

        delimiterField.setAccessible(true);
        quoteCharField.setAccessible(true);
        quoteModeField.setAccessible(true);
        commentMarkerField.setAccessible(true);
        escapeCharacterField.setAccessible(true);
        ignoreSurroundingSpacesField.setAccessible(true);
        ignoreEmptyLinesField.setAccessible(true);
        recordSeparatorField.setAccessible(true);
        nullStringField.setAccessible(true);
        headerField.setAccessible(true);
        skipHeaderRecordField.setAccessible(true);

        delimiterField.set(format, delimiter);
        quoteCharField.set(format, quoteCharacter);
        quoteModeField.set(format, quoteMode);
        commentMarkerField.set(format, commentMarker);
        escapeCharacterField.set(format, escapeCharacter);
        ignoreSurroundingSpacesField.set(format, ignoreSurroundingSpaces);
        ignoreEmptyLinesField.set(format, ignoreEmptyLines);
        recordSeparatorField.set(format, recordSeparator);
        nullStringField.set(format, nullString);
        headerField.set(format, header);
        skipHeaderRecordField.set(format, skipHeaderRecord);

        return format;
    }

    @Test
    @Timeout(8000)
    void testEquals_sameObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentClass() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals("some string"));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentDelimiter() throws Exception {
        CSVFormat format1 = createCSVFormat(',', CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(';', CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentQuoteMode() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), QuoteMode.ALL,
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), QuoteMode.MINIMAL,
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), null, CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), '"', CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacterDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), '"', CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), '\'', CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarkerNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                null, CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                '#', CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarkerDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                '#', CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                '!', CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), null,
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), '\\',
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacterDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), '\\',
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), '/',
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullStringNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), null,
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), "NULL",
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullStringDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), "NULL",
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), "null",
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_headerDifferentLengths() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                new String[]{"a", "b"}, CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                new String[]{"a"}, CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_headerDifferentContents() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                new String[]{"a", "b"}, CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                new String[]{"a", "c"}, CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreSurroundingSpacesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                true, CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                false, CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreEmptyLinesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), true,
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), false,
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_skipHeaderRecordDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), true);

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), false);

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparatorNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                null, CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                "\n", CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparatorDifferentValues() throws Exception {
        CSVFormat format1 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                "\n", CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        CSVFormat format2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                "\r\n", CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord());

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_allFieldsEqual() throws Exception {
        String[] header = {"a", "b"};
        CSVFormat format1 = createCSVFormat(';', '"', QuoteMode.ALL,
                '#', '\\', true, false,
                "\r\n", "NULL", header, true);

        CSVFormat format2 = createCSVFormat(';', '"', QuoteMode.ALL,
                '#', '\\', true, false,
                "\r\n", "NULL", header, true);

        assertTrue(format1.equals(format2));
    }
}