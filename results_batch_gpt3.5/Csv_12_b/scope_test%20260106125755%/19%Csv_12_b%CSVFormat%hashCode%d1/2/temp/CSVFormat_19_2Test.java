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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

class CSVFormat_19_2Test {

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord, boolean allowMissingColumnNames) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteChar, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header,
                skipHeaderRecord, allowMissingColumnNames);
    }

    @Test
    @Timeout(8000)
    void testHashCode_allFieldsSet() throws Exception {
        String[] header = new String[] { "a", "b" };
        CSVFormat format = createCSVFormat(
                ',',
                '"',
                QuoteMode.ALL,
                '#',
                '\\',
                true,
                true,
                "\r\n",
                "NULL",
                header,
                true,
                false);

        int expected = 1;
        int prime = 31;
        expected = prime * expected + (int) ',';
        expected = prime * expected + QuoteMode.ALL.hashCode();
        expected = prime * expected + Character.valueOf('"').hashCode();
        expected = prime * expected + Character.valueOf('#').hashCode();
        expected = prime * expected + Character.valueOf('\\').hashCode();
        expected = prime * expected + "NULL".hashCode();
        expected = prime * expected + 1231; // ignoreSurroundingSpaces true
        expected = prime * expected + 1231; // ignoreEmptyLines true
        expected = prime * expected + 1231; // skipHeaderRecord true
        expected = prime * expected + "\r\n".hashCode();
        expected = prime * expected + Arrays.hashCode(header);

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_nullFieldsAndBooleans() throws Exception {
        String[] header = null;
        CSVFormat format = createCSVFormat(
                ';',
                null,
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                header,
                false,
                false);

        int expected = 1;
        int prime = 31;
        expected = prime * expected + (int) ';';
        expected = prime * expected + 0; // quoteMode null
        expected = prime * expected + 0; // quoteCharacter null
        expected = prime * expected + 0; // commentMarker null
        expected = prime * expected + 0; // escapeCharacter null
        expected = prime * expected + 0; // nullString null
        expected = prime * expected + 1237; // ignoreSurroundingSpaces false
        expected = prime * expected + 1237; // ignoreEmptyLines false
        expected = prime * expected + 1237; // skipHeaderRecord false
        expected = prime * expected + 0; // recordSeparator null
        expected = prime * expected + 0; // Arrays.hashCode(null) is 0

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_differentObjectsSameValues() throws Exception {
        String[] header = new String[] { "x", "y", "z" };
        CSVFormat format1 = createCSVFormat(
                '\t',
                '\'',
                QuoteMode.MINIMAL,
                '!',
                '?',
                true,
                false,
                "\n",
                "NULL",
                header,
                false,
                true);
        CSVFormat format2 = createCSVFormat(
                '\t',
                '\'',
                QuoteMode.MINIMAL,
                '!',
                '?',
                true,
                false,
                "\n",
                "NULL",
                header,
                false,
                true);

        // hashCode should be same for equal objects
        assertEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_reflectionInvocation() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Method hashCodeMethod = CSVFormat.class.getDeclaredMethod("hashCode");
        hashCodeMethod.setAccessible(true);

        int result = (int) hashCodeMethod.invoke(format);

        assertEquals(format.hashCode(), result);
    }
}