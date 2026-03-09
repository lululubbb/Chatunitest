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
import java.util.Arrays;

class CSVFormatHashCodeTest {

    @Test
    @Timeout(8000)
    void testHashCode_allFieldsNonNullAndBooleansTrue() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',
                Character.valueOf('\"'),
                createQuoteMock(12345),
                Character.valueOf('#'),
                Character.valueOf('\\'),
                true,
                true,
                "\r\n",
                "NULL",
                new String[]{"a", "b"},
                true
        );

        int expected = 1;
        int prime = 31;
        expected = prime * expected + ',';
        expected = prime * expected + 12345;
        expected = prime * expected + Character.valueOf('\"').hashCode();
        expected = prime * expected + Character.valueOf('#').hashCode();
        expected = prime * expected + Character.valueOf('\\').hashCode();
        expected = prime * expected + "NULL".hashCode();
        expected = prime * expected + 1231; // ignoreSurroundingSpaces true
        expected = prime * expected + 1231; // ignoreEmptyLines true
        expected = prime * expected + 1231; // skipHeaderRecord true
        expected = prime * expected + "\r\n".hashCode();
        expected = prime * expected + Arrays.hashCode(new String[]{"a", "b"});

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_someFieldsNullAndBooleansFalse() throws Exception {
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
                null,
                false
        );

        int expected = 1;
        int prime = 31;
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
        expected = prime * expected + 0; // header null

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_headerEmptyArray() throws Exception {
        CSVFormat format = createCSVFormat(
                ':',
                Character.valueOf('\''),
                createQuoteMock(1),
                Character.valueOf('!'),
                Character.valueOf('?'),
                true,
                false,
                "RS",
                "NS",
                new String[]{},
                false
        );

        int expected = 1;
        int prime = 31;
        expected = prime * expected + ':';
        expected = prime * expected + 1;
        expected = prime * expected + Character.valueOf('\'').hashCode();
        expected = prime * expected + Character.valueOf('!').hashCode();
        expected = prime * expected + Character.valueOf('?').hashCode();
        expected = prime * expected + "NS".hashCode();
        expected = prime * expected + 1231; // ignoreSurroundingSpaces true
        expected = prime * expected + 1237; // ignoreEmptyLines false
        expected = prime * expected + 1237; // skipHeaderRecord false
        expected = prime * expected + "RS".hashCode();
        expected = prime * expected + Arrays.hashCode(new String[]{});

        assertEquals(expected, format.hashCode());
    }

    private CSVFormat createCSVFormat(char delimiter,
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
                skipHeaderRecord
        );
    }

    // Minimal mock for Quote class with hashCode override
    private static class Quote {
        private final int hc;

        Quote(int hc) {
            this.hc = hc;
        }

        @Override
        public int hashCode() {
            return hc;
        }
    }

    private Quote createQuoteMock(int hashCode) {
        return new Quote(hashCode);
    }
}