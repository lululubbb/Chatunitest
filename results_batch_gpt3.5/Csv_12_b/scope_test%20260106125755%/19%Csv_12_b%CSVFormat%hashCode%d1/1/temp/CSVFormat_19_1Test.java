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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class CSVFormat_19_1Test {

    @Test
    @Timeout(8000)
    public void testHashCode_sameObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        int hash1 = format.hashCode();
        int hash2 = format.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    @Timeout(8000)
    public void testHashCode_equalObjects() {
        CSVFormat format1 = CSVFormat.DEFAULT.withHeader("a", "b", "c")
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreEmptyLines(true)
                .withIgnoreSurroundingSpaces(false)
                .withNullString("null")
                .withQuoteMode(QuoteMode.ALL)
                .withQuote('"')
                .withRecordSeparator("\n")
                .withSkipHeaderRecord(true);

        CSVFormat format2 = CSVFormat.DEFAULT.withHeader("a", "b", "c")
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreEmptyLines(true)
                .withIgnoreSurroundingSpaces(false)
                .withNullString("null")
                .withQuoteMode(QuoteMode.ALL)
                .withQuote('"')
                .withRecordSeparator("\n")
                .withSkipHeaderRecord(true);

        assertEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentObjects() {
        CSVFormat format1 = CSVFormat.DEFAULT.withHeader("a", "b", "c");
        CSVFormat format2 = CSVFormat.DEFAULT.withHeader("x", "y", "z");
        assertNotEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_nullFields() throws Exception {
        // Use reflection to create CSVFormat instance with null fields where possible
        Class<CSVFormat> clazz = CSVFormat.class;
        // The constructor is private, getDeclaredConstructor with all parameters
        Constructor<CSVFormat> ctor = clazz.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        ctor.setAccessible(true);

        CSVFormat format = ctor.newInstance(
                ',', // delimiter
                null, // quoteCharacter
                null, // quoteMode
                null, // commentMarker
                null, // escapeCharacter
                false, // ignoreSurroundingSpaces
                false, // ignoreEmptyLines
                null, // recordSeparator
                null, // nullString
                null, // header
                false, // skipHeaderRecord
                false // allowMissingColumnNames
        );

        int hash = format.hashCode();

        // Compute expected hash manually
        final int prime = 31;
        int expected = 1;
        expected = prime * expected + ',';
        expected = prime * expected + 0; // quoteMode null
        expected = prime * expected + 0; // quoteCharacter null
        expected = prime * expected + 0; // commentMarker null
        expected = prime * expected + 0; // escapeCharacter null
        expected = prime * expected + 0; // nullString null
        expected = prime * expected + (false ? 1231 : 1237); // ignoreSurroundingSpaces
        expected = prime * expected + (false ? 1231 : 1237); // ignoreEmptyLines
        expected = prime * expected + (false ? 1231 : 1237); // skipHeaderRecord
        expected = prime * expected + 0; // recordSeparator null
        expected = prime * expected + Arrays.hashCode((String[]) null); // header null

        assertEquals(expected, hash);
    }
}