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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatHashCodeTest {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatCustom;

    @BeforeEach
    void setUp() throws Exception {
        // Accessing private constructor via reflection to create custom instances
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Default CSVFormat instance for baseline comparison
        csvFormatDefault = CSVFormat.DEFAULT;

        // Custom CSVFormat with different fields to test hashCode variations
        csvFormatCustom = constructor.newInstance(';', '"', QuoteMode.ALL, '#', '\\', true, false,
                "\n", "NULL", new String[] {"h1", "h2"}, new String[] {}, true, true, true);
    }

    @Test
    @Timeout(8000)
    void testHashCode_sameObject() {
        // hashCode should be consistent for the same instance
        int hash1 = csvFormatDefault.hashCode();
        int hash2 = csvFormatDefault.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    @Timeout(8000)
    void testHashCode_equalObjects() throws Exception {
        // Create another instance with the same field values as csvFormatCustom to test equality of hashCode
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat another = constructor.newInstance(';', '"', QuoteMode.ALL, '#', '\\', true, false,
                "\n", "NULL", new String[] {"h1", "h2"}, new String[] {}, true, true, true);

        assertEquals(csvFormatCustom.hashCode(), another.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_differentObjects() {
        // hashCode should differ for objects with different field values
        assertNotEquals(csvFormatDefault.hashCode(), csvFormatCustom.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_nullFields() throws Exception {
        // Create CSVFormat with null quoteMode, quoteCharacter, commentMarker, escapeCharacter, nullString, recordSeparator, header
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat nullFieldsFormat = constructor.newInstance(',', null, null, null, null, false, false,
                null, null, null, null, false, false, false);

        // Calculate expected hashCode manually
        final int prime = 31;
        int result = 1;
        result = prime * result + ',';
        result = prime * result + 0; // quoteMode null
        result = prime * result + 0; // quoteCharacter null
        result = prime * result + 0; // commentMarker null
        result = prime * result + 0; // escapeCharacter null
        result = prime * result + 0; // nullString null
        result = prime * result + (false ? 1231 : 1237);
        result = prime * result + (false ? 1231 : 1237);
        result = prime * result + (false ? 1231 : 1237);
        result = prime * result + (false ? 1231 : 1237);
        result = prime * result + 0; // recordSeparator null
        result = prime * result + Arrays.hashCode(null); // header null treated as 0

        assertEquals(result, nullFieldsFormat.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_booleanFields() throws Exception {
        // Create CSVFormat with all boolean fields true
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat allTrueBooleans = constructor.newInstance(',', '"', QuoteMode.MINIMAL, null, null,
                true, true, "\r\n", "null", new String[] {}, new String[] {"header"}, true, true, true);

        // Create CSVFormat with all boolean fields false
        CSVFormat allFalseBooleans = constructor.newInstance(',', '"', QuoteMode.MINIMAL, null, null,
                false, false, "\r\n", "null", new String[] {}, new String[] {"header"}, false, false, false);

        // Their hashCodes should differ due to boolean fields
        assertNotEquals(allTrueBooleans.hashCode(), allFalseBooleans.hashCode());
    }

}