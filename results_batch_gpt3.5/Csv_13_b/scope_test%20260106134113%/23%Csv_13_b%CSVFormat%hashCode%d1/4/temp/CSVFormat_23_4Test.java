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
import java.util.Arrays;

class CSVFormatHashCodeTest {

    @Test
    @Timeout(8000)
    void testHashCode_allFieldsDefault() {
        CSVFormat format = CSVFormat.DEFAULT;
        int prime = 31;
        int expected = 1;
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
    void testHashCode_variousFields() throws Exception {
        // Create CSVFormat instance using reflection to set fields to custom values
        CSVFormat format = createCSVFormatForTest(
                ';',
                Character.valueOf('\''),
                QuoteMode.ALL,
                Character.valueOf('#'),
                Character.valueOf('\\'),
                true,
                true,
                false,
                "RS",
                "nullStr",
                new String[]{"h1", "h2"},
                true,
                true
        );

        int prime = 31;
        int expected = 1;
        expected = prime * expected + ';';
        expected = prime * expected + QuoteMode.ALL.hashCode();
        expected = prime * expected + Character.valueOf('\'').hashCode();
        expected = prime * expected + Character.valueOf('#').hashCode();
        expected = prime * expected + Character.valueOf('\\').hashCode();
        expected = prime * expected + "nullStr".hashCode();
        expected = prime * expected + (true ? 1231 : 1237);
        expected = prime * expected + (true ? 1231 : 1237);
        expected = prime * expected + (false ? 1231 : 1237);
        expected = prime * expected + (true ? 1231 : 1237);
        expected = prime * expected + "RS".hashCode();
        expected = prime * expected + Arrays.hashCode(new String[]{"h1", "h2"});

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_nullFields() throws Exception {
        CSVFormat format = createCSVFormatForTest(
                ',',
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                null,
                null,
                null,
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
        expected = prime * expected + 0; // explicitly 0 for null header

        assertEquals(expected, format.hashCode());
    }

    private CSVFormat createCSVFormatForTest(
            char delimiter,
            Character quoteCharacter,
            QuoteMode quoteMode,
            Character commentMarker,
            Character escapeCharacter,
            boolean ignoreSurroundingSpaces,
            boolean ignoreHeaderCase,
            boolean ignoreEmptyLines,
            String recordSeparator,
            String nullString,
            String[] header,
            boolean skipHeaderRecord,
            boolean allowMissingColumnNames
    ) throws Exception {
        // Use reflection to invoke private constructor
        java.lang.reflect.Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                QuoteMode.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                Object[].class,
                String[].class,
                boolean.class,
                boolean.class,
                boolean.class
        );
        ctor.setAccessible(true);
        CSVFormat format = ctor.newInstance(
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
        return format;
    }
}