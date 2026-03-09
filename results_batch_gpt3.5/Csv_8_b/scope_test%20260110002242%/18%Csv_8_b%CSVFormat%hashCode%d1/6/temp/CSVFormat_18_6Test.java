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
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.util.Arrays;

class CSVFormat_18_6Test {

    @Test
    @Timeout(8000)
    void testHashCode_AllFieldsSet() throws Exception {
        // Arrange
        Quote mockQuotePolicy = Mockito.mock(Quote.class);
        Mockito.when(mockQuotePolicy.hashCode()).thenReturn(12345);

        Character quoteChar = Character.valueOf('\"');
        Character commentStart = Character.valueOf('#');
        Character escape = Character.valueOf('\\');
        String nullString = "NULL";
        String recordSeparator = "\n";
        String[] header = new String[]{"a", "b", "c"};

        CSVFormat csvFormat = createCSVFormat(
                ',',
                quoteChar,
                mockQuotePolicy,
                commentStart,
                escape,
                true,
                true,
                recordSeparator,
                nullString,
                header,
                true
        );

        // Act
        int hash1 = csvFormat.hashCode();

        // Assert
        // Calculate expected hashCode manually
        final int prime = 31;
        int expected = 1;
        expected = prime * expected + ',';
        expected = prime * expected + 12345; // mockQuotePolicy.hashCode()
        expected = prime * expected + quoteChar.hashCode();
        expected = prime * expected + commentStart.hashCode();
        expected = prime * expected + escape.hashCode();
        expected = prime * expected + nullString.hashCode();
        expected = prime * expected + 1231; // ignoreSurroundingSpaces true
        expected = prime * expected + 1231; // ignoreEmptyLines true
        expected = prime * expected + 1231; // skipHeaderRecord true
        expected = prime * expected + recordSeparator.hashCode();
        expected = prime * expected + Arrays.hashCode(header);

        assertEquals(expected, hash1);
    }

    @Test
    @Timeout(8000)
    void testHashCode_NullFieldsAndBooleansFalse() throws Exception {
        // Arrange
        CSVFormat csvFormat = createCSVFormat(
                ';',
                null,
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                null,
                false
        );

        // Act
        int hash = csvFormat.hashCode();

        // Assert
        final int prime = 31;
        int expected = 1;
        expected = prime * expected + ';';
        expected = prime * expected + 0; // quotePolicy null
        expected = prime * expected + 0; // quoteChar null
        expected = prime * expected + 0; // commentStart null
        expected = prime * expected + 0; // escape null
        expected = prime * expected + 0; // nullString null
        expected = prime * expected + 1237; // ignoreSurroundingSpaces false
        expected = prime * expected + 1237; // ignoreEmptyLines false
        expected = prime * expected + 1237; // skipHeaderRecord false
        expected = prime * expected + 0; // recordSeparator null
        expected = prime * expected + Arrays.hashCode(null); // header null

        assertEquals(expected, hash);
    }

    @Test
    @Timeout(8000)
    void testHashCode_DifferentHeaders() throws Exception {
        // Arrange
        String[] header1 = new String[]{"x", "y"};
        String[] header2 = new String[]{"x", "y", "z"};

        CSVFormat csvFormat1 = createCSVFormat(
                ',',
                null,
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                header1,
                false
        );

        CSVFormat csvFormat2 = createCSVFormat(
                ',',
                null,
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                header2,
                false
        );

        // Act
        int hash1 = csvFormat1.hashCode();
        int hash2 = csvFormat2.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);
    }

    @Test
    @Timeout(8000)
    void testHashCode_ConsistentForEqualObjects() throws Exception {
        // Arrange
        Quote mockQuotePolicy = Mockito.mock(Quote.class);
        Mockito.when(mockQuotePolicy.hashCode()).thenReturn(9999);

        String[] header = new String[]{"header"};

        CSVFormat csvFormat1 = createCSVFormat(
                ',',
                '\"',
                mockQuotePolicy,
                '#',
                '\\',
                true,
                false,
                "\r\n",
                "null",
                header,
                true
        );

        CSVFormat csvFormat2 = createCSVFormat(
                ',',
                '\"',
                mockQuotePolicy,
                '#',
                '\\',
                true,
                false,
                "\r\n",
                "null",
                header,
                true
        );

        // Act & Assert
        assertEquals(csvFormat1.hashCode(), csvFormat2.hashCode());
    }

    // Helper method to instantiate CSVFormat with reflection since constructor is private
    private CSVFormat createCSVFormat(
            char delimiter,
            Character quoteChar,
            Quote quotePolicy,
            Character commentStart,
            Character escape,
            boolean ignoreSurroundingSpaces,
            boolean ignoreEmptyLines,
            String recordSeparator,
            String nullString,
            String[] header,
            boolean skipHeaderRecord) throws Exception {

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                Quote.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                String[].class,
                boolean.class);

        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter,
                quoteChar,
                quotePolicy,
                commentStart,
                escape,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                header,
                skipHeaderRecord);
    }
}