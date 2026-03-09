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
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class CSVFormatEqualsTest {

    private CSVFormat baseFormat;

    @BeforeEach
    public void setUp() throws Exception {
        // Use DEFAULT as base format
        baseFormat = CSVFormat.DEFAULT;
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord) throws Exception {
        // Use reflection to create CSVFormat instance with private constructor
        // Constructor signature:
        // (char delimiter, Character quoteChar, QuoteMode quoteMode,
        //  Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        //  boolean ignoreEmptyLines, String recordSeparator, String nullString,
        //  Object[] headerComments, String[] header, boolean skipHeaderRecord,
        //  boolean allowMissingColumnNames, boolean ignoreHeaderCase)
        var constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter,
                quoteCharacter,
                quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                null,
                header,
                skipHeaderRecord,
                false,
                false);
    }

    @Test
    @Timeout(8000)
    public void testEquals_sameInstance() {
        assertTrue(baseFormat.equals(baseFormat));
    }

    @Test
    @Timeout(8000)
    public void testEquals_null() {
        assertFalse(baseFormat.equals(null));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentClass() {
        assertFalse(baseFormat.equals("a string"));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentDelimiter() throws Exception {
        CSVFormat other = createCSVFormat(
                (char)(baseFormat.getDelimiter() + 1),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        assertFalse(baseFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentQuoteMode() throws Exception {
        QuoteMode differentQuoteMode = baseFormat.getQuoteMode() == QuoteMode.ALL ? QuoteMode.MINIMAL : QuoteMode.ALL;
        CSVFormat other = createCSVFormat(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                differentQuoteMode,
                baseFormat.getCommentMarker(),
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        assertFalse(baseFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacter_nullAndNonNull() throws Exception {
        // base quoteCharacter non-null, other null
        if (baseFormat.getQuoteCharacter() != null) {
            CSVFormat other = createCSVFormat(
                    baseFormat.getDelimiter(),
                    null,
                    baseFormat.getQuoteMode(),
                    baseFormat.getCommentMarker(),
                    baseFormat.getEscapeCharacter(),
                    baseFormat.getIgnoreSurroundingSpaces(),
                    baseFormat.getIgnoreEmptyLines(),
                    baseFormat.getRecordSeparator(),
                    baseFormat.getNullString(),
                    baseFormat.getHeader(),
                    baseFormat.getSkipHeaderRecord());
            assertFalse(baseFormat.equals(other));
        }
        // base quoteCharacter null, other non-null
        CSVFormat baseNull = createCSVFormat(
                baseFormat.getDelimiter(),
                null,
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        if (baseFormat.getQuoteCharacter() != null) {
            CSVFormat otherNonNull = createCSVFormat(
                    baseFormat.getDelimiter(),
                    baseFormat.getQuoteCharacter(),
                    baseFormat.getQuoteMode(),
                    baseFormat.getCommentMarker(),
                    baseFormat.getEscapeCharacter(),
                    baseFormat.getIgnoreSurroundingSpaces(),
                    baseFormat.getIgnoreEmptyLines(),
                    baseFormat.getRecordSeparator(),
                    baseFormat.getNullString(),
                    baseFormat.getHeader(),
                    baseFormat.getSkipHeaderRecord());
            assertFalse(baseNull.equals(otherNonNull));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacter_notEqual() throws Exception {
        Character baseQC = baseFormat.getQuoteCharacter();
        Character differentQC = (baseQC == null) ? '"' : (char)(baseQC + 1);
        CSVFormat other = createCSVFormat(
                baseFormat.getDelimiter(),
                differentQC,
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        if (baseQC != null && !baseQC.equals(differentQC)) {
            assertFalse(baseFormat.equals(other));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarker_nullAndNonNull() throws Exception {
        // base commentMarker non-null, other null
        if (baseFormat.getCommentMarker() != null) {
            CSVFormat other = createCSVFormat(
                    baseFormat.getDelimiter(),
                    baseFormat.getQuoteCharacter(),
                    baseFormat.getQuoteMode(),
                    null,
                    baseFormat.getEscapeCharacter(),
                    baseFormat.getIgnoreSurroundingSpaces(),
                    baseFormat.getIgnoreEmptyLines(),
                    baseFormat.getRecordSeparator(),
                    baseFormat.getNullString(),
                    baseFormat.getHeader(),
                    baseFormat.getSkipHeaderRecord());
            assertFalse(baseFormat.equals(other));
        }
        // base commentMarker null, other non-null
        CSVFormat baseNull = createCSVFormat(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                null,
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        if (baseFormat.getCommentMarker() != null) {
            CSVFormat otherNonNull = createCSVFormat(
                    baseFormat.getDelimiter(),
                    baseFormat.getQuoteCharacter(),
                    baseFormat.getQuoteMode(),
                    baseFormat.getCommentMarker(),
                    baseFormat.getEscapeCharacter(),
                    baseFormat.getIgnoreSurroundingSpaces(),
                    baseFormat.getIgnoreEmptyLines(),
                    baseFormat.getRecordSeparator(),
                    baseFormat.getNullString(),
                    baseFormat.getHeader(),
                    baseFormat.getSkipHeaderRecord());
            assertFalse(baseNull.equals(otherNonNull));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarker_notEqual() throws Exception {
        Character baseCM = baseFormat.getCommentMarker();
        Character differentCM = (baseCM == null) ? '#' : (char)(baseCM + 1);
        CSVFormat other = createCSVFormat(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                differentCM,
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        if (baseCM != null && !baseCM.equals(differentCM)) {
            assertFalse(baseFormat.equals(other));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacter_nullAndNonNull() throws Exception {
        // base escapeCharacter non-null, other null
        if (baseFormat.getEscapeCharacter() != null) {
            CSVFormat other = createCSVFormat(
                    baseFormat.getDelimiter(),
                    baseFormat.getQuoteCharacter(),
                    baseFormat.getQuoteMode(),
                    baseFormat.getCommentMarker(),
                    null,
                    baseFormat.getIgnoreSurroundingSpaces(),
                    baseFormat.getIgnoreEmptyLines(),
                    baseFormat.getRecordSeparator(),
                    baseFormat.getNullString(),
                    baseFormat.getHeader(),
                    baseFormat.getSkipHeaderRecord());
            assertFalse(baseFormat.equals(other));
        }
        // base escapeCharacter null, other non-null
        CSVFormat baseNull = createCSVFormat(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                null,
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        if (baseFormat.getEscapeCharacter() != null) {
            CSVFormat otherNonNull = createCSVFormat(
                    baseFormat.getDelimiter(),
                    baseFormat.getQuoteCharacter(),
                    baseFormat.getQuoteMode(),
                    baseFormat.getCommentMarker(),
                    baseFormat.getEscapeCharacter(),
                    baseFormat.getIgnoreSurroundingSpaces(),
                    baseFormat.getIgnoreEmptyLines(),
                    baseFormat.getRecordSeparator(),
                    baseFormat.getNullString(),
                    baseFormat.getHeader(),
                    baseFormat.getSkipHeaderRecord());
            assertFalse(baseNull.equals(otherNonNull));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacter_notEqual() throws Exception {
        Character baseEC = baseFormat.getEscapeCharacter();
        Character differentEC = (baseEC == null) ? '\\' : (char)(baseEC + 1);
        CSVFormat other = createCSVFormat(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                differentEC,
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        if (baseEC != null && !baseEC.equals(differentEC)) {
            assertFalse(baseFormat.equals(other));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullString_nullAndNonNull() throws Exception {
        // base nullString non-null, other null
        if (baseFormat.getNullString() != null) {
            CSVFormat other = createCSVFormat(
                    baseFormat.getDelimiter(),
                    baseFormat.getQuoteCharacter(),
                    baseFormat.getQuoteMode(),
                    baseFormat.getCommentMarker(),
                    baseFormat.getEscapeCharacter(),
                    baseFormat.getIgnoreSurroundingSpaces(),
                    baseFormat.getIgnoreEmptyLines(),
                    baseFormat.getRecordSeparator(),
                    null,
                    baseFormat.getHeader(),
                    baseFormat.getSkipHeaderRecord());
            assertFalse(baseFormat.equals(other));
        }
        // base nullString null, other non-null
        CSVFormat baseNull = createCSVFormat(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                null,
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        if (baseFormat.getNullString() != null) {
            CSVFormat otherNonNull = createCSVFormat(
                    baseFormat.getDelimiter(),
                    baseFormat.getQuoteCharacter(),
                    baseFormat.getQuoteMode(),
                    baseFormat.getCommentMarker(),
                    baseFormat.getEscapeCharacter(),
                    baseFormat.getIgnoreSurroundingSpaces(),
                    baseFormat.getIgnoreEmptyLines(),
                    baseFormat.getRecordSeparator(),
                    baseFormat.getNullString(),
                    baseFormat.getHeader(),
                    baseFormat.getSkipHeaderRecord());
            assertFalse(baseNull.equals(otherNonNull));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullString_notEqual() throws Exception {
        String baseNS = baseFormat.getNullString();
        String differentNS = (baseNS == null) ? "null" : baseNS + "diff";
        CSVFormat other = createCSVFormat(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                differentNS,
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        if (baseNS != null && !baseNS.equals(differentNS)) {
            assertFalse(baseFormat.equals(other));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_header_notEqual() throws Exception {
        String[] baseHeader = baseFormat.getHeader();
        String[] diffHeader;
        if (baseHeader == null) {
            diffHeader = new String[]{"A"};
        } else {
            diffHeader = Arrays.copyOf(baseHeader, baseHeader.length + 1);
            diffHeader[diffHeader.length - 1] = "diff";
        }
        CSVFormat other = createCSVFormat(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                diffHeader,
                baseFormat.getSkipHeaderRecord());
        assertFalse(baseFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreSurroundingSpaces_different() throws Exception {
        CSVFormat other = createCSVFormat(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                baseFormat.getEscapeCharacter(),
                !baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        assertFalse(baseFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreEmptyLines_different() throws Exception {
        CSVFormat other = createCSVFormat(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                !baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        assertFalse(baseFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_skipHeaderRecord_different() throws Exception {
        CSVFormat other = createCSVFormat(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                baseFormat.getHeader(),
                !baseFormat.getSkipHeaderRecord());
        assertFalse(baseFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparator_nullAndNonNull() throws Exception {
        // base recordSeparator non-null, other null
        if (baseFormat.getRecordSeparator() != null) {
            CSVFormat other = createCSVFormat(
                    baseFormat.getDelimiter(),
                    baseFormat.getQuoteCharacter(),
                    baseFormat.getQuoteMode(),
                    baseFormat.getCommentMarker(),
                    baseFormat.getEscapeCharacter(),
                    baseFormat.getIgnoreSurroundingSpaces(),
                    baseFormat.getIgnoreEmptyLines(),
                    null,
                    baseFormat.getNullString(),
                    baseFormat.getHeader(),
                    baseFormat.getSkipHeaderRecord());
            assertFalse(baseFormat.equals(other));
        }
        // base recordSeparator null, other non-null
        CSVFormat baseNull = createCSVFormat(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                null,
                baseFormat.getNullString(),
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        if (baseFormat.getRecordSeparator() != null) {
            CSVFormat otherNonNull = createCSVFormat(
                    baseFormat.getDelimiter(),
                    baseFormat.getQuoteCharacter(),
                    baseFormat.getQuoteMode(),
                    baseFormat.getCommentMarker(),
                    baseFormat.getEscapeCharacter(),
                    baseFormat.getIgnoreSurroundingSpaces(),
                    baseFormat.getIgnoreEmptyLines(),
                    baseFormat.getRecordSeparator(),
                    baseFormat.getNullString(),
                    baseFormat.getHeader(),
                    baseFormat.getSkipHeaderRecord());
            assertFalse(baseNull.equals(otherNonNull));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparator_notEqual() throws Exception {
        String baseRS = baseFormat.getRecordSeparator();
        String differentRS = (baseRS == null) ? "\n" : baseRS + "diff";
        CSVFormat other = createCSVFormat(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                differentRS,
                baseFormat.getNullString(),
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        if (baseRS != null && !baseRS.equals(differentRS)) {
            assertFalse(baseFormat.equals(other));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_allFieldsEqual() throws Exception {
        CSVFormat other = createCSVFormat(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                baseFormat.getHeader(),
                baseFormat.getSkipHeaderRecord());
        assertTrue(baseFormat.equals(other));
        assertTrue(other.equals(baseFormat));
    }
}