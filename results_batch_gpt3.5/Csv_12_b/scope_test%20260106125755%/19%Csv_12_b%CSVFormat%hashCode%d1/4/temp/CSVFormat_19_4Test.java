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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import java.lang.reflect.Constructor;
import java.util.Arrays;

class CSVFormatHashCodeTest {

    private CSVFormat createCSVFormat(
            char delimiter,
            Character quoteCharacter,
            QuoteMode quoteMode,
            Character commentMarker,
            Character escapeCharacter,
            boolean ignoreSurroundingSpaces,
            boolean ignoreEmptyLines,
            boolean skipHeaderRecord,
            String recordSeparator,
            String nullString,
            String[] header) throws Exception {

        // Use reflection to create instance since constructor is private
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                QuoteMode.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                String[].class,
                boolean.class,
                boolean.class
        );
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
                header,
                skipHeaderRecord,
                false // allowMissingColumnNames - set false for tests
        );
    }

    @Test
    @Timeout(8000)
    public void testHashCode_AllFieldsNullOrDefaults() throws Exception {
        CSVFormat format = createCSVFormat(
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
                null
        );

        int expected = 1;
        int prime = 31;
        expected = prime * expected + ',';
        expected = prime * expected + 0; // quoteMode null
        expected = prime * expected + 0; // quoteCharacter null
        expected = prime * expected + 0; // commentMarker null
        expected = prime * expected + 0; // escapeCharacter null
        expected = prime * expected + 0; // nullString null
        expected = prime * expected + 1237; // ignoreSurroundingSpaces false
        expected = prime * expected + 1237; // ignoreEmptyLines false
        expected = prime * expected + 1237; // skipHeaderRecord false
        expected = prime * expected + 0; // recordSeparator null
        expected = prime * expected + Arrays.hashCode((Object[]) null); // header null

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_AllFieldsSetNonNull() throws Exception {
        String[] header = new String[]{"a", "b"};
        CSVFormat format = createCSVFormat(
                ';',
                '"',
                QuoteMode.ALL,
                '#',
                '\\',
                true,
                true,
                true,
                "\r\n",
                "NULL",
                header
        );

        int prime = 31;
        int expected = 1;
        expected = prime * expected + ';';
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
    public void testHashCode_MixedFields() throws Exception {
        String[] header = new String[]{"header1"};
        CSVFormat format = createCSVFormat(
                '\t',
                '\'',
                QuoteMode.MINIMAL,
                null,
                null,
                false,
                true,
                false,
                "\n",
                null,
                header
        );

        int prime = 31;
        int expected = 1;
        expected = prime * expected + '\t';
        expected = prime * expected + QuoteMode.MINIMAL.hashCode();
        expected = prime * expected + Character.valueOf('\'').hashCode();
        expected = prime * expected + 0; // commentMarker null
        expected = prime * expected + 0; // escapeCharacter null
        expected = prime * expected + 0; // nullString null
        expected = prime * expected + 1237; // ignoreSurroundingSpaces false
        expected = prime * expected + 1231; // ignoreEmptyLines true
        expected = prime * expected + 1237; // skipHeaderRecord false
        expected = prime * expected + "\n".hashCode();
        expected = prime * expected + Arrays.hashCode(header);

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_Consistent() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        int h1 = format.hashCode();
        int h2 = format.hashCode();

        assertEquals(h1, h2);
    }

    @Test
    @Timeout(8000)
    public void testHashCode_DifferentObjectsDifferentHash() throws Exception {
        CSVFormat format1 = createCSVFormat(
                ',',
                '"',
                QuoteMode.ALL,
                '#',
                '\\',
                true,
                true,
                true,
                "\r\n",
                "NULL",
                new String[]{"a"}
        );

        CSVFormat format2 = createCSVFormat(
                ';',
                '\'',
                QuoteMode.MINIMAL,
                null,
                null,
                false,
                false,
                false,
                "\n",
                null,
                new String[]{"b"}
        );

        assertNotEquals(format1.hashCode(), format2.hashCode());
    }
}