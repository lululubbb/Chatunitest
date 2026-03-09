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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_19_6Test {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatCustom;

    @BeforeEach
    public void setUp() throws Exception {
        csvFormatDefault = CSVFormat.DEFAULT;

        // Use reflection to create custom instance since constructor is private
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        csvFormatCustom = constructor.newInstance(
                ';', // delimiter
                Character.valueOf('"'), // quoteCharacter boxed
                QuoteMode.ALL, // quoteMode
                Character.valueOf('#'), // commentMarker boxed
                Character.valueOf('\\'), // escapeCharacter boxed
                true, // ignoreSurroundingSpaces
                false, // ignoreEmptyLines
                "\n", // recordSeparator
                "NULL", // nullString
                new String[] {"a", "b", "c"}, // header
                true, // skipHeaderRecord
                true // allowMissingColumnNames
        );
    }

    @Test
    @Timeout(8000)
    public void testHashCode_DefaultInstances() {
        int hash1 = csvFormatDefault.hashCode();
        int hash2 = CSVFormat.DEFAULT.hashCode();
        assertEquals(hash1, hash2, "Hash codes of identical default instances should be equal");
    }

    @Test
    @Timeout(8000)
    public void testHashCode_DifferentInstances() {
        int hashDefault = csvFormatDefault.hashCode();
        int hashCustom = csvFormatCustom.hashCode();
        // They should not be equal because fields differ
        assertNotEquals(hashDefault, hashCustom, "Hash codes of different instances should differ");
    }

    @Test
    @Timeout(8000)
    public void testHashCode_Consistency() {
        int hash1 = csvFormatCustom.hashCode();
        int hash2 = csvFormatCustom.hashCode();
        assertEquals(hash1, hash2, "Hash code should be consistent across multiple invocations");
    }

    @Test
    @Timeout(8000)
    public void testHashCode_WithNullFields() throws Exception {
        // Use reflection to create instance with null fields to cover null branches
        Class<CSVFormat> clazz = CSVFormat.class;
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat formatWithNulls = constructor.newInstance(
                ',', // delimiter
                null, // quoteCharacter null
                null, // quoteMode null
                null, // commentMarker null
                null, // escapeCharacter null
                false, // ignoreSurroundingSpaces
                false, // ignoreEmptyLines
                null, // recordSeparator null
                null, // nullString null
                null, // header null
                false, // skipHeaderRecord
                false // allowMissingColumnNames
        );

        // Compute hashCode and verify no exceptions and expected calculation
        int result = formatWithNulls.hashCode();

        // Manually compute expected hashCode for verification
        final int prime = 31;
        int expected = 1;
        expected = prime * expected + ',';
        expected = prime * expected + 0; // quoteMode null
        expected = prime * expected + 0; // quoteCharacter null
        expected = prime * expected + 0; // commentMarker null
        expected = prime * expected + 0; // escapeCharacter null
        expected = prime * expected + 0; // nullString null
        expected = prime * expected + (false ? 1231 : 1237); // ignoreSurroundingSpaces false => 1237
        expected = prime * expected + (false ? 1231 : 1237); // ignoreEmptyLines false => 1237
        expected = prime * expected + (false ? 1231 : 1237); // skipHeaderRecord false => 1237
        expected = prime * expected + 0; // recordSeparator null
        expected = prime * expected + 0; // header null -> Arrays.hashCode(null) == 0

        assertEquals(expected, result);
    }

}