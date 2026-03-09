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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatHashCodeTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to create an instance of CSVFormat with all fields set to default or null
        // Because the constructor is private, use reflection to instantiate
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        csvFormat = constructor.newInstance(
                ',',               // delimiter
                '"',               // quoteChar
                QuoteMode.MINIMAL, // quoteMode
                null,              // commentStart
                null,              // escape
                false,             // ignoreSurroundingSpaces
                false,             // ignoreEmptyLines
                "\r\n",            // recordSeparator
                null,              // nullString
                null,              // headerComments
                null,              // header
                false,             // skipHeaderRecord
                false,             // allowMissingColumnNames
                false              // ignoreHeaderCase
        );
    }

    @Test
    @Timeout(8000)
    void testHashCode_allFieldsDefault() {
        int h1 = csvFormat.hashCode();
        int h2 = csvFormat.hashCode();
        assertEquals(h1, h2, "hashCode should be stable");
    }

    @Test
    @Timeout(8000)
    void testHashCode_withNullFields() throws Exception {
        // Set all nullable fields to null explicitly
        setField(csvFormat, "quoteMode", null);
        setField(csvFormat, "quoteCharacter", null);
        setField(csvFormat, "commentMarker", null);
        setField(csvFormat, "escapeCharacter", null);
        setField(csvFormat, "nullString", null);
        setField(csvFormat, "recordSeparator", null);
        setField(csvFormat, "header", null);

        int hash = csvFormat.hashCode();

        // Recalculate expected hash manually
        final int prime = 31;
        int result = 1;
        result = prime * result + ',';
        result = prime * result + 0; // quoteMode null
        result = prime * result + 0; // quoteCharacter null
        result = prime * result + 0; // commentMarker null
        result = prime * result + 0; // escapeCharacter null
        result = prime * result + 0; // nullString null
        result = prime * result + (false ? 1231 : 1237); // ignoreSurroundingSpaces false
        result = prime * result + (false ? 1231 : 1237); // ignoreHeaderCase false
        result = prime * result + (false ? 1231 : 1237); // ignoreEmptyLines false
        result = prime * result + (false ? 1231 : 1237); // skipHeaderRecord false
        result = prime * result + 0; // recordSeparator null
        result = prime * result + 0; // header null (Arrays.hashCode(null) == 0)

        assertEquals(result, hash);
    }

    @Test
    @Timeout(8000)
    void testHashCode_withNonNullFields() throws Exception {
        setField(csvFormat, "delimiter", ';');
        setField(csvFormat, "quoteMode", QuoteMode.ALL);
        setField(csvFormat, "quoteCharacter", '\'');
        setField(csvFormat, "commentMarker", '#');
        setField(csvFormat, "escapeCharacter", '\\');
        setField(csvFormat, "nullString", "NULL");
        setField(csvFormat, "ignoreSurroundingSpaces", true);
        setField(csvFormat, "ignoreHeaderCase", true);
        setField(csvFormat, "ignoreEmptyLines", true);
        setField(csvFormat, "skipHeaderRecord", true);
        setField(csvFormat, "recordSeparator", "\n");
        setField(csvFormat, "header", new String[] {"a", "b"});

        int hash = csvFormat.hashCode();

        final int prime = 31;
        int result = 1;
        result = prime * result + ';';
        result = prime * result + QuoteMode.ALL.hashCode();
        result = prime * result + Character.valueOf('\'').hashCode();
        result = prime * result + Character.valueOf('#').hashCode();
        result = prime * result + Character.valueOf('\\').hashCode();
        result = prime * result + "NULL".hashCode();
        result = prime * result + (true ? 1231 : 1237);
        result = prime * result + (true ? 1231 : 1237);
        result = prime * result + (true ? 1231 : 1237);
        result = prime * result + (true ? 1231 : 1237);
        result = prime * result + "\n".hashCode();
        result = prime * result + Arrays.hashCode(new String[] {"a", "b"});

        assertEquals(result, hash);
    }

    @Test
    @Timeout(8000)
    void testHashCode_differentObjectsDifferentHash() throws Exception {
        // Create second CSVFormat with different fields
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat2 = constructor.newInstance(
                ';',
                '\'',
                QuoteMode.ALL,
                '#',
                '\\',
                true,
                true,
                "\n",
                "NULL",
                null,
                new String[] {"a", "b"},
                true,
                false,
                true
        );

        int h1 = csvFormat.hashCode();
        int h2 = csvFormat2.hashCode();

        assertNotEquals(h1, h2, "Different field values should produce different hashCodes");
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}