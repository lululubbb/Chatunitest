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
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CSVFormat_23_1Test {

    @Test
    @Timeout(8000)
    public void testHashCode_allFieldsDefault() {
        CSVFormat format = CSVFormat.DEFAULT;
        int expected = 1;
        final int prime = 31;

        expected = prime * expected + format.getDelimiter();
        expected = prime * expected + ((format.getQuoteMode() == null) ? 0 : format.getQuoteMode().hashCode());
        expected = prime * expected + ((format.getQuoteCharacter() == null) ? 0 : format.getQuoteCharacter().hashCode());
        expected = prime * expected + ((format.getCommentMarker() == null) ? 0 : format.getCommentMarker().hashCode());
        expected = prime * expected + ((format.getEscapeCharacter() == null) ? 0 : format.getEscapeCharacter().hashCode());
        expected = prime * expected + ((format.getNullString() == null) ? 0 : format.getNullString().hashCode());
        expected = prime * expected + (format.getIgnoreSurroundingSpaces() ? 1231 : 1237);
        expected = prime * expected + (format.getIgnoreHeaderCase() ? 1231 : 1237);
        expected = prime * expected + (format.getIgnoreEmptyLines() ? 1231 : 1237);
        expected = prime * expected + (format.getSkipHeaderRecord() ? 1231 : 1237);
        expected = prime * expected + ((format.getRecordSeparator() == null) ? 0 : format.getRecordSeparator().hashCode());
        expected = prime * expected + Arrays.hashCode(format.getHeader());

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_variousFieldValues() throws Exception {
        // Using reflection to create CSVFormat instance with custom fields since constructor is private
        CSVFormat format = createCSVFormat(
                ';',                          // delimiter
                'Q',                          // quoteCharacter
                QuoteMode.ALL,                // quoteMode
                '#',                          // commentMarker
                '\\',                         // escapeCharacter
                true,                        // ignoreSurroundingSpaces
                false,                       // ignoreEmptyLines
                "\n",                        // recordSeparator
                "NULL",                      // nullString
                new String[]{"h1", "h2"},    // header
                true,                        // skipHeaderRecord
                true                         // ignoreHeaderCase
        );

        int expected = 1;
        final int prime = 31;

        expected = prime * expected + format.getDelimiter();
        expected = prime * expected + ((format.getQuoteMode() == null) ? 0 : format.getQuoteMode().hashCode());
        expected = prime * expected + ((format.getQuoteCharacter() == null) ? 0 : format.getQuoteCharacter().hashCode());
        expected = prime * expected + ((format.getCommentMarker() == null) ? 0 : format.getCommentMarker().hashCode());
        expected = prime * expected + ((format.getEscapeCharacter() == null) ? 0 : format.getEscapeCharacter().hashCode());
        expected = prime * expected + ((format.getNullString() == null) ? 0 : format.getNullString().hashCode());
        expected = prime * expected + (format.getIgnoreSurroundingSpaces() ? 1231 : 1237);
        expected = prime * expected + (format.getIgnoreHeaderCase() ? 1231 : 1237);
        expected = prime * expected + (format.getIgnoreEmptyLines() ? 1231 : 1237);
        expected = prime * expected + (format.getSkipHeaderRecord() ? 1231 : 1237);
        expected = prime * expected + ((format.getRecordSeparator() == null) ? 0 : format.getRecordSeparator().hashCode());
        expected = prime * expected + Arrays.hashCode(format.getHeader());

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_nullFields() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',         // delimiter
                null,        // quoteCharacter
                null,        // quoteMode
                null,        // commentMarker
                null,        // escapeCharacter
                false,       // ignoreSurroundingSpaces
                true,        // ignoreEmptyLines
                null,        // recordSeparator
                null,        // nullString
                null,        // header
                false,       // skipHeaderRecord
                false        // ignoreHeaderCase
        );

        int expected = 1;
        final int prime = 31;

        expected = prime * expected + format.getDelimiter();
        expected = prime * expected + 0; // quoteMode null
        expected = prime * expected + 0; // quoteCharacter null
        expected = prime * expected + 0; // commentMarker null
        expected = prime * expected + 0; // escapeCharacter null
        expected = prime * expected + 0; // nullString null
        expected = prime * expected + (format.getIgnoreSurroundingSpaces() ? 1231 : 1237);
        expected = prime * expected + (format.getIgnoreHeaderCase() ? 1231 : 1237);
        expected = prime * expected + (format.getIgnoreEmptyLines() ? 1231 : 1237);
        expected = prime * expected + (format.getSkipHeaderRecord() ? 1231 : 1237);
        expected = prime * expected + 0; // recordSeparator null
        expected = prime * expected + Arrays.hashCode(format.getHeader());

        assertEquals(expected, format.hashCode());
    }

    // Helper method to create CSVFormat instance with specified fields via reflection
    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord, boolean ignoreHeaderCase) throws Exception {

        // Access private constructor
        java.lang.reflect.Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);

        constructor.setAccessible(true);

        // headerComments is nullable, pass null
        Object[] headerComments = null;

        // allowMissingColumnNames set to false for test
        boolean allowMissingColumnNames = false;

        return constructor.newInstance(
                delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }
}