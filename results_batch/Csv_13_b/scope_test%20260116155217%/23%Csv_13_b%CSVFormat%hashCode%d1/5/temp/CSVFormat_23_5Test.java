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

import java.lang.reflect.Constructor;
import java.util.Arrays;

class CSVFormatHashCodeTest {

    @Test
    @Timeout(8000)
    void testHashCode_AllFieldsDefault() {
        CSVFormat format = CSVFormat.DEFAULT;

        int expected = 1;
        int prime = 31;

        expected = prime * expected + format.getDelimiter();
        expected = prime * expected + (format.getQuoteMode() == null ? 0 : format.getQuoteMode().hashCode());
        expected = prime * expected + (format.getQuoteCharacter() == null ? 0 : format.getQuoteCharacter().hashCode());
        expected = prime * expected + (format.getCommentMarker() == null ? 0 : format.getCommentMarker().hashCode());
        expected = prime * expected + (format.getEscapeCharacter() == null ? 0 : format.getEscapeCharacter().hashCode());
        expected = prime * expected + (format.getNullString() == null ? 0 : format.getNullString().hashCode());
        expected = prime * expected + (format.getIgnoreSurroundingSpaces() ? 1231 : 1237);
        expected = prime * expected + (format.getIgnoreHeaderCase() ? 1231 : 1237);
        expected = prime * expected + (format.getIgnoreEmptyLines() ? 1231 : 1237);
        expected = prime * expected + (format.getSkipHeaderRecord() ? 1231 : 1237);
        expected = prime * expected + (format.getRecordSeparator() == null ? 0 : format.getRecordSeparator().hashCode());
        expected = prime * expected + Arrays.hashCode(format.getHeader());

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_VariousFieldValues() throws Exception {
        CSVFormat format = createCSVFormatWithFields(
                ';',
                '\'',
                QuoteMode.ALL,
                '#',
                '\\',
                true,
                false,
                "\n",
                "NULL",
                new String[]{"header1", "header2"},
                true,
                true,
                false
        );

        int prime = 31;
        int expected = 1;

        expected = prime * expected + ';';
        expected = prime * expected + QuoteMode.ALL.hashCode();
        expected = prime * expected + Character.valueOf('\'').hashCode();
        expected = prime * expected + Character.valueOf('#').hashCode();
        expected = prime * expected + Character.valueOf('\\').hashCode();
        expected = prime * expected + "NULL".hashCode();
        expected = prime * expected + (true ? 1231 : 1237);
        expected = prime * expected + (false ? 1231 : 1237);
        expected = prime * expected + (true ? 1231 : 1237);
        expected = prime * expected + (true ? 1231 : 1237);
        expected = prime * expected + "\n".hashCode();
        expected = prime * expected + Arrays.hashCode(new String[]{"header1", "header2"});

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_NullFields() throws Exception {
        CSVFormat format = createCSVFormatWithFields(
                ',',
                null,
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                null,
                false,
                false,
                false
        );

        int prime = 31;
        int expected = 1;

        expected = prime * expected + ',';
        expected = prime * expected + 0;
        expected = prime * expected + 0;
        expected = prime * expected + 0;
        expected = prime * expected + 0;
        expected = prime * expected + 0;
        expected = prime * expected + (false ? 1231 : 1237);
        expected = prime * expected + (false ? 1231 : 1237);
        expected = prime * expected + (false ? 1231 : 1237);
        expected = prime * expected + (false ? 1231 : 1237);
        expected = prime * expected + 0;
        expected = prime * expected + 0;

        assertEquals(expected, format.hashCode());
    }

    private CSVFormat createCSVFormatWithFields(
            char delimiter,
            Character quoteCharacter,
            QuoteMode quoteMode,
            Character commentMarker,
            Character escapeCharacter,
            boolean ignoreSurroundingSpaces,
            boolean ignoreHeaderCase,
            String recordSeparator,
            String nullString,
            String[] header,
            boolean skipHeaderRecord,
            boolean ignoreEmptyLines,
            boolean allowMissingColumnNames
    ) throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // The order of parameters must match the constructor's parameter order exactly:
        // delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces,
        // ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
        // skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase

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
                allowMissingColumnNames,
                ignoreHeaderCase
        );
    }
}