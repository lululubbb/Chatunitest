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
import java.util.Arrays;

class CSVFormatHashCodeTest {

    private CSVFormat createCSVFormatWithFields(
            char delimiter,
            Character quoteCharacter,
            QuoteMode quoteMode,
            Character commentMarker,
            Character escapeCharacter,
            boolean ignoreSurroundingSpaces,
            boolean ignoreHeaderCase,
            boolean ignoreEmptyLines,
            boolean skipHeaderRecord,
            String recordSeparator,
            String nullString,
            String[] header) throws Exception {
        // Use reflection to invoke private constructor
        var constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        // headerComments as null
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
                ignoreHeaderCase);
    }

    @Test
    @Timeout(8000)
    void testHashCode_AllFieldsNullOrDefaults() throws Exception {
        CSVFormat format = createCSVFormatWithFields(
                ',',
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                null,
                null,
                null);

        int expected = 1;
        int prime = 31;
        expected = prime * expected + ',';
        expected = prime * expected + 0; // quoteMode null
        expected = prime * expected + 0; // quoteCharacter null
        expected = prime * expected + 0; // commentMarker null
        expected = prime * expected + 0; // escapeCharacter null
        expected = prime * expected + 0; // nullString null
        expected = prime * expected + 1237; // ignoreSurroundingSpaces false
        expected = prime * expected + 1237; // ignoreHeaderCase false
        expected = prime * expected + 1237; // ignoreEmptyLines false
        expected = prime * expected + 1237; // skipHeaderRecord false
        expected = prime * expected + 0; // recordSeparator null
        expected = prime * expected + Arrays.hashCode(null); // header null

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_AllFieldsNonNullAndTrueFlags() throws Exception {
        Character quoteChar = '\"';
        QuoteMode quoteMode = QuoteMode.ALL;
        Character commentMarker = '#';
        Character escapeChar = '\\';
        String nullStr = "NULL";
        String recordSep = "\n";
        String[] header = new String[]{"a", "b"};

        CSVFormat format = createCSVFormatWithFields(
                ';',
                quoteChar,
                quoteMode,
                commentMarker,
                escapeChar,
                true,
                true,
                true,
                true,
                recordSep,
                nullStr,
                header);

        int prime = 31;
        int expected = 1;
        expected = prime * expected + ';';
        expected = prime * expected + quoteMode.hashCode();
        expected = prime * expected + quoteChar.hashCode();
        expected = prime * expected + commentMarker.hashCode();
        expected = prime * expected + escapeChar.hashCode();
        expected = prime * expected + nullStr.hashCode();
        expected = prime * expected + 1231; // ignoreSurroundingSpaces true
        expected = prime * expected + 1231; // ignoreHeaderCase true
        expected = prime * expected + 1231; // ignoreEmptyLines true
        expected = prime * expected + 1231; // skipHeaderRecord true
        expected = prime * expected + recordSep.hashCode();
        expected = prime * expected + Arrays.hashCode(header);

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_QuoteModeNullOthersSet() throws Exception {
        Character quoteChar = '\"';
        Character commentMarker = '#';
        Character escapeChar = '\\';
        String nullStr = "null";
        String recordSep = "\r\n";
        String[] header = new String[]{"header"};

        CSVFormat format = createCSVFormatWithFields(
                '|',
                quoteChar,
                null,
                commentMarker,
                escapeChar,
                true,
                false,
                true,
                false,
                recordSep,
                nullStr,
                header);

        int prime = 31;
        int expected = 1;
        expected = prime * expected + '|';
        expected = prime * expected + 0; // quoteMode null
        expected = prime * expected + quoteChar.hashCode();
        expected = prime * expected + commentMarker.hashCode();
        expected = prime * expected + escapeChar.hashCode();
        expected = prime * expected + nullStr.hashCode();
        expected = prime * expected + 1231; // ignoreSurroundingSpaces true
        expected = prime * expected + 1237; // ignoreHeaderCase false
        expected = prime * expected + 1231; // ignoreEmptyLines true
        expected = prime * expected + 1237; // skipHeaderRecord false
        expected = prime * expected + recordSep.hashCode();
        expected = prime * expected + Arrays.hashCode(header);

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_EmptyHeaderArray() throws Exception {
        CSVFormat format = createCSVFormatWithFields(
                ',',
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                null,
                null,
                new String[0]);

        int prime = 31;
        int expected = 1;
        expected = prime * expected + ',';
        expected = prime * expected + 0; // quoteMode null
        expected = prime * expected + 0; // quoteCharacter null
        expected = prime * expected + 0; // commentMarker null
        expected = prime * expected + 0; // escapeCharacter null
        expected = prime * expected + 0; // nullString null
        expected = prime * expected + 1237; // ignoreSurroundingSpaces false
        expected = prime * expected + 1237; // ignoreHeaderCase false
        expected = prime * expected + 1237; // ignoreEmptyLines false
        expected = prime * expected + 1237; // skipHeaderRecord false
        expected = prime * expected + 0; // recordSeparator null
        expected = prime * expected + Arrays.hashCode(new String[0]);

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_SkipHeaderRecordTrue() throws Exception {
        CSVFormat format = createCSVFormatWithFields(
                ',',
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                true,
                null,
                null,
                null);

        int prime = 31;
        int expected = 1;
        expected = prime * expected + ',';
        expected = prime * expected + 0; // quoteMode null
        expected = prime * expected + 0; // quoteCharacter null
        expected = prime * expected + 0; // commentMarker null
        expected = prime * expected + 0; // escapeCharacter null
        expected = prime * expected + 0; // nullString null
        expected = prime * expected + 1237; // ignoreSurroundingSpaces false
        expected = prime * expected + 1237; // ignoreHeaderCase false
        expected = prime * expected + 1237; // ignoreEmptyLines false
        expected = prime * expected + 1231; // skipHeaderRecord true
        expected = prime * expected + 0; // recordSeparator null
        expected = prime * expected + Arrays.hashCode(null);

        assertEquals(expected, format.hashCode());
    }
}