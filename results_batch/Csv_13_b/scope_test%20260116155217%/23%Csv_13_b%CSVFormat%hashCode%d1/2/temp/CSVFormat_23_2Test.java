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
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatHashCodeTest {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatCustom;

    @BeforeEach
    void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;

        // Create a custom CSVFormat instance using reflection since the constructor is private
        csvFormatCustom = createCSVFormat(
                ';',                  // delimiter
                Character.valueOf('"'),// quoteCharacter
                QuoteMode.ALL,        // quoteMode
                Character.valueOf('#'),// commentMarker
                Character.valueOf('\\'),// escapeCharacter
                true,                 // ignoreSurroundingSpaces
                false,                // ignoreEmptyLines
                "\n",                 // recordSeparator
                "NULL",               // nullString
                new String[]{"h1", "h2"}, // header
                false,                // skipHeaderRecord
                true                  // ignoreHeaderCase
        );
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord, boolean ignoreHeaderCase) {
        try {
            // The constructor signature as per provided:
            // CSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
            // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
            // boolean ignoreEmptyLines, String recordSeparator, String nullString,
            // Object[] headerComments, String[] header, boolean skipHeaderRecord,
            // boolean allowMissingColumnNames, boolean ignoreHeaderCase)
            Method constructor = CSVFormat.class.getDeclaredConstructor(
                    char.class, Character.class, QuoteMode.class,
                    Character.class, Character.class, boolean.class,
                    boolean.class, String.class, String.class,
                    Object[].class, String[].class, boolean.class,
                    boolean.class, boolean.class);
            constructor.setAccessible(true);
            // allowMissingColumnNames false here, headerComments null
            return (CSVFormat) constructor.invoke(null,
                    delimiter, quoteCharacter, quoteMode,
                    commentMarker, escapeCharacter, ignoreSurroundingSpaces,
                    ignoreEmptyLines, recordSeparator, nullString,
                    null, header, skipHeaderRecord,
                    false, ignoreHeaderCase);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void testHashCode_DefaultInstance() {
        int hash = csvFormatDefault.hashCode();

        // Calculate expected hash manually matching the hashCode method logic
        final int prime = 31;
        int expected = 1;
        expected = prime * expected + csvFormatDefault.getDelimiter();
        expected = prime * expected + (csvFormatDefault.getQuoteMode() == null ? 0 : csvFormatDefault.getQuoteMode().hashCode());
        expected = prime * expected + (csvFormatDefault.getQuoteCharacter() == null ? 0 : csvFormatDefault.getQuoteCharacter().hashCode());
        expected = prime * expected + (csvFormatDefault.getCommentMarker() == null ? 0 : csvFormatDefault.getCommentMarker().hashCode());
        expected = prime * expected + (csvFormatDefault.getEscapeCharacter() == null ? 0 : csvFormatDefault.getEscapeCharacter().hashCode());
        expected = prime * expected + (csvFormatDefault.getNullString() == null ? 0 : csvFormatDefault.getNullString().hashCode());
        expected = prime * expected + (csvFormatDefault.getIgnoreSurroundingSpaces() ? 1231 : 1237);
        expected = prime * expected + (csvFormatDefault.getIgnoreHeaderCase() ? 1231 : 1237);
        expected = prime * expected + (csvFormatDefault.getIgnoreEmptyLines() ? 1231 : 1237);
        expected = prime * expected + (csvFormatDefault.getSkipHeaderRecord() ? 1231 : 1237);
        expected = prime * expected + (csvFormatDefault.getRecordSeparator() == null ? 0 : csvFormatDefault.getRecordSeparator().hashCode());
        expected = prime * expected + Arrays.hashCode(csvFormatDefault.getHeader());

        assertEquals(expected, hash);
    }

    @Test
    @Timeout(8000)
    void testHashCode_CustomInstance() {
        int hash = csvFormatCustom.hashCode();

        final int prime = 31;
        int expected = 1;
        expected = prime * expected + csvFormatCustom.getDelimiter();
        expected = prime * expected + (csvFormatCustom.getQuoteMode() == null ? 0 : csvFormatCustom.getQuoteMode().hashCode());
        expected = prime * expected + (csvFormatCustom.getQuoteCharacter() == null ? 0 : csvFormatCustom.getQuoteCharacter().hashCode());
        expected = prime * expected + (csvFormatCustom.getCommentMarker() == null ? 0 : csvFormatCustom.getCommentMarker().hashCode());
        expected = prime * expected + (csvFormatCustom.getEscapeCharacter() == null ? 0 : csvFormatCustom.getEscapeCharacter().hashCode());
        expected = prime * expected + (csvFormatCustom.getNullString() == null ? 0 : csvFormatCustom.getNullString().hashCode());
        expected = prime * expected + (csvFormatCustom.getIgnoreSurroundingSpaces() ? 1231 : 1237);
        expected = prime * expected + (csvFormatCustom.getIgnoreHeaderCase() ? 1231 : 1237);
        expected = prime * expected + (csvFormatCustom.getIgnoreEmptyLines() ? 1231 : 1237);
        expected = prime * expected + (csvFormatCustom.getSkipHeaderRecord() ? 1231 : 1237);
        expected = prime * expected + (csvFormatCustom.getRecordSeparator() == null ? 0 : csvFormatCustom.getRecordSeparator().hashCode());
        expected = prime * expected + Arrays.hashCode(csvFormatCustom.getHeader());

        assertEquals(expected, hash);
    }

    @Test
    @Timeout(8000)
    void testHashCode_VaryFields() {
        // Create variations for each nullable field to test null and non-null cases

        CSVFormat base = csvFormatDefault;

        // quoteMode null vs non-null
        CSVFormat withQuoteModeNull = base.withQuoteMode(null);
        CSVFormat withQuoteModeNonNull = base.withQuoteMode(QuoteMode.MINIMAL);
        assertNotEquals(withQuoteModeNull.hashCode(), withQuoteModeNonNull.hashCode());

        // quoteCharacter null vs non-null
        CSVFormat withQuoteCharNull = base.withQuote((Character) null);
        CSVFormat withQuoteCharNonNull = base.withQuote('"');
        assertNotEquals(withQuoteCharNull.hashCode(), withQuoteCharNonNull.hashCode());

        // commentMarker null vs non-null
        CSVFormat withCommentNull = base.withCommentMarker((Character) null);
        CSVFormat withCommentNonNull = base.withCommentMarker('#');
        assertNotEquals(withCommentNull.hashCode(), withCommentNonNull.hashCode());

        // escapeCharacter null vs non-null
        CSVFormat withEscapeNull = base.withEscape((Character) null);
        CSVFormat withEscapeNonNull = base.withEscape('\\');
        assertNotEquals(withEscapeNull.hashCode(), withEscapeNonNull.hashCode());

        // nullString null vs non-null
        CSVFormat withNullStringNull = base.withNullString(null);
        CSVFormat withNullStringNonNull = base.withNullString("null");
        assertNotEquals(withNullStringNull.hashCode(), withNullStringNonNull.hashCode());

        // recordSeparator null vs non-null
        CSVFormat withRecordSepNull = base.withRecordSeparator((String) null);
        CSVFormat withRecordSepNonNull = base.withRecordSeparator("\r\n");
        assertNotEquals(withRecordSepNull.hashCode(), withRecordSepNonNull.hashCode());

        // header null vs non-null
        CSVFormat withHeaderNull = base.withHeader((String[]) null);
        CSVFormat withHeaderNonNull = base.withHeader("a", "b");
        assertNotEquals(withHeaderNull.hashCode(), withHeaderNonNull.hashCode());

        // Boolean flags changes
        CSVFormat withIgnoreSurroundingSpacesTrue = base.withIgnoreSurroundingSpaces(true);
        CSVFormat withIgnoreSurroundingSpacesFalse = base.withIgnoreSurroundingSpaces(false);
        assertNotEquals(withIgnoreSurroundingSpacesTrue.hashCode(), withIgnoreSurroundingSpacesFalse.hashCode());

        CSVFormat withIgnoreHeaderCaseTrue = base.withIgnoreHeaderCase(true);
        CSVFormat withIgnoreHeaderCaseFalse = base.withIgnoreHeaderCase(false);
        assertNotEquals(withIgnoreHeaderCaseTrue.hashCode(), withIgnoreHeaderCaseFalse.hashCode());

        CSVFormat withIgnoreEmptyLinesTrue = base.withIgnoreEmptyLines(true);
        CSVFormat withIgnoreEmptyLinesFalse = base.withIgnoreEmptyLines(false);
        assertNotEquals(withIgnoreEmptyLinesTrue.hashCode(), withIgnoreEmptyLinesFalse.hashCode());

        CSVFormat withSkipHeaderRecordTrue = base.withSkipHeaderRecord(true);
        CSVFormat withSkipHeaderRecordFalse = base.withSkipHeaderRecord(false);
        assertNotEquals(withSkipHeaderRecordTrue.hashCode(), withSkipHeaderRecordFalse.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_Consistency() {
        int hash1 = csvFormatDefault.hashCode();
        int hash2 = csvFormatDefault.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    @Timeout(8000)
    void testHashCode_EqualObjects() {
        CSVFormat copy = CSVFormat.DEFAULT.withIgnoreEmptyLines(true); // same as DEFAULT
        assertEquals(csvFormatDefault.hashCode(), copy.hashCode());
        assertTrue(csvFormatDefault.equals(copy));
    }

}