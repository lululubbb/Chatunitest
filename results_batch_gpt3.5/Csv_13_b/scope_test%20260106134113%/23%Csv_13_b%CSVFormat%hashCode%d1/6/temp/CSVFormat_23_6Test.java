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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_23_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() throws Exception {
        // Use the public static factory method newFormat(char)
        csvFormat = CSVFormat.newFormat(',');
    }

    @Test
    @Timeout(8000)
    public void testHashCode_DefaultValues() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');

        // Set all fields to default or null values to check hashCode with nulls and false booleans
        setField(format, "delimiter", ',');
        setField(format, "quoteMode", null);
        setField(format, "quoteCharacter", null);
        setField(format, "commentMarker", null);
        setField(format, "escapeCharacter", null);
        setField(format, "nullString", null);
        setField(format, "ignoreSurroundingSpaces", false);
        setField(format, "ignoreHeaderCase", false);
        setField(format, "ignoreEmptyLines", false);
        setField(format, "skipHeaderRecord", false);
        setField(format, "recordSeparator", null);
        setField(format, "header", null);

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
        expected = prime * expected + 0; // header null (Arrays.hashCode(null) returns 0)

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_AllFieldsSet() throws Exception {
        CSVFormat format = CSVFormat.newFormat(';');

        // Set all fields to non-null and true booleans
        setField(format, "delimiter", ';');
        setField(format, "quoteMode", QuoteMode.ALL);
        setField(format, "quoteCharacter", Character.valueOf('\''));
        setField(format, "commentMarker", Character.valueOf('#'));
        setField(format, "escapeCharacter", Character.valueOf('\\'));
        setField(format, "nullString", "NULL");
        setField(format, "ignoreSurroundingSpaces", true);
        setField(format, "ignoreHeaderCase", true);
        setField(format, "ignoreEmptyLines", true);
        setField(format, "skipHeaderRecord", true);
        setField(format, "recordSeparator", "\n");
        setField(format, "header", new String[] { "a", "b", "c" });

        int prime = 31;
        int result = 1;
        result = prime * result + ';';
        result = prime * result + QuoteMode.ALL.hashCode();
        result = prime * result + Character.valueOf('\'').hashCode();
        result = prime * result + Character.valueOf('#').hashCode();
        result = prime * result + Character.valueOf('\\').hashCode();
        result = prime * result + "NULL".hashCode();
        result = prime * result + 1231; // ignoreSurroundingSpaces true
        result = prime * result + 1231; // ignoreHeaderCase true
        result = prime * result + 1231; // ignoreEmptyLines true
        result = prime * result + 1231; // skipHeaderRecord true
        result = prime * result + "\n".hashCode();
        result = prime * result + Arrays.hashCode(new String[] { "a", "b", "c" });

        assertEquals(result, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_DifferentObjects() throws Exception {
        CSVFormat format1 = CSVFormat.newFormat(',');
        CSVFormat format2 = CSVFormat.newFormat(',');

        // Set different quoteCharacter to create different hashCodes
        setField(format1, "quoteCharacter", Character.valueOf('"'));
        setField(format2, "quoteCharacter", Character.valueOf('\''));

        int hash1 = format1.hashCode();
        int hash2 = format2.hashCode();

        // They should not be equal because quoteCharacter differs
        assertNotEquals(hash1, hash2);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }
}