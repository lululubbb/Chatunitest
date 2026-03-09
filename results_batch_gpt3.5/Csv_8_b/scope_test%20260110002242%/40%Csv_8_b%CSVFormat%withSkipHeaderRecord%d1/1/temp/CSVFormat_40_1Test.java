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
import java.lang.reflect.Field;
import java.util.Arrays;

class CSVFormat_40_1Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withSkipHeaderRecord(true);

        assertNotSame(original, updated, "withSkipHeaderRecord should return a new instance");
        assertTrue(updated.getSkipHeaderRecord(), "skipHeaderRecord should be true");

        // Other fields should be equal - use reflection to access private fields
        assertEquals(getCharField(original, "delimiter"), getCharField(updated, "delimiter"));
        assertEquals(getCharacterField(original, "quoteChar"), getCharacterField(updated, "quoteChar"));
        assertEquals(getField(original, "quotePolicy"), getField(updated, "quotePolicy"));
        assertEquals(getCharacterField(original, "commentStart"), getCharacterField(updated, "commentStart"));
        assertEquals(getCharacterField(original, "escape"), getCharacterField(updated, "escape"));
        assertEquals(getBooleanField(original, "ignoreSurroundingSpaces"), getBooleanField(updated, "ignoreSurroundingSpaces"));
        assertEquals(getBooleanField(original, "ignoreEmptyLines"), getBooleanField(updated, "ignoreEmptyLines"));
        assertEquals(getField(original, "recordSeparator"), getField(updated, "recordSeparator"));
        assertEquals(getField(original, "nullString"), getField(updated, "nullString"));
        assertArrayEquals(getStringArrayField(original, "header"), getStringArrayField(updated, "header"));
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordFalse() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat updated = original.withSkipHeaderRecord(false);

        assertNotSame(original, updated, "withSkipHeaderRecord should return a new instance");
        assertFalse(updated.getSkipHeaderRecord(), "skipHeaderRecord should be false");

        // Other fields should be equal - use reflection to access private fields
        assertEquals(getCharField(original, "delimiter"), getCharField(updated, "delimiter"));
        assertEquals(getCharacterField(original, "quoteChar"), getCharacterField(updated, "quoteChar"));
        assertEquals(getField(original, "quotePolicy"), getField(updated, "quotePolicy"));
        assertEquals(getCharacterField(original, "commentStart"), getCharacterField(updated, "commentStart"));
        assertEquals(getCharacterField(original, "escape"), getCharacterField(updated, "escape"));
        assertEquals(getBooleanField(original, "ignoreSurroundingSpaces"), getBooleanField(updated, "ignoreSurroundingSpaces"));
        assertEquals(getBooleanField(original, "ignoreEmptyLines"), getBooleanField(updated, "ignoreEmptyLines"));
        assertEquals(getField(original, "recordSeparator"), getField(updated, "recordSeparator"));
        assertEquals(getField(original, "nullString"), getField(updated, "nullString"));
        assertArrayEquals(getStringArrayField(original, "header"), getStringArrayField(updated, "header"));
    }

    private Object getField(CSVFormat format, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(format);
    }

    private char getCharField(CSVFormat format, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getChar(format);
    }

    private Character getCharacterField(CSVFormat format, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (Character) field.get(format);
    }

    private boolean getBooleanField(CSVFormat format, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getBoolean(format);
    }

    private String[] getStringArrayField(CSVFormat format, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        String[] array = (String[]) field.get(format);
        if (array == null) {
            return new String[0];
        }
        return array;
    }
}