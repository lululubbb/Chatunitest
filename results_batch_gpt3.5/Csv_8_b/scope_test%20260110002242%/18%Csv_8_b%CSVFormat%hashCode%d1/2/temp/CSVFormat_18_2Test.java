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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatHashCodeTest {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatCustom;

    @BeforeEach
    void setUp() throws Exception {
        // Using the public DEFAULT instance for baseline
        csvFormatDefault = CSVFormat.DEFAULT;

        // Create a custom CSVFormat instance with all fields set to non-default values
        csvFormatCustom = createCustomCSVFormat();
    }

    private CSVFormat createCustomCSVFormat() throws Exception {
        // Use reflection to instantiate CSVFormat with custom values since constructor is private
        Class<CSVFormat> clazz = CSVFormat.class;
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(char.class, Character.class, Quote.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        Character quoteChar = '\"';
        Quote quotePolicy = Quote.ALL;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        String[] header = new String[] {"A", "B", "C"};
        boolean skipHeaderRecord = true;

        return constructor.newInstance(';', quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }

    @Test
    @Timeout(8000)
    void testHashCode_DefaultInstance() {
        int hash1 = csvFormatDefault.hashCode();
        int hash2 = csvFormatDefault.hashCode();
        assertEquals(hash1, hash2, "hashCode should be consistent across calls");
    }

    @Test
    @Timeout(8000)
    void testHashCode_CustomInstance() {
        int hash = csvFormatCustom.hashCode();
        assertNotEquals(0, hash, "hashCode should not be zero for custom instance");
    }

    @Test
    @Timeout(8000)
    void testHashCode_DifferentInstancesDifferentHash() throws Exception {
        CSVFormat other = createCustomCSVFormat();

        // Change one field to differentiate
        setField(other, "delimiter", ':');

        int hash1 = csvFormatCustom.hashCode();
        int hash2 = other.hashCode();

        assertNotEquals(hash1, hash2, "Different delimiter should produce different hashCode");
    }

    @Test
    @Timeout(8000)
    void testHashCode_NullFieldsHandled() throws Exception {
        CSVFormat instance = createCustomCSVFormat();

        setField(instance, "quotePolicy", null);
        setField(instance, "quoteChar", null);
        setField(instance, "commentStart", null);
        setField(instance, "escape", null);
        setField(instance, "nullString", null);
        setField(instance, "recordSeparator", null);
        setField(instance, "header", null);

        int hash = instance.hashCode();
        assertNotEquals(0, hash, "hashCode should handle null fields correctly");
    }

    @Test
    @Timeout(8000)
    void testHashCode_BooleanFieldsTrueFalse() throws Exception {
        CSVFormat instanceTrue = createCustomCSVFormat();
        CSVFormat instanceFalse = createCustomCSVFormat();

        setField(instanceTrue, "ignoreSurroundingSpaces", true);
        setField(instanceTrue, "ignoreEmptyLines", true);
        setField(instanceTrue, "skipHeaderRecord", true);

        setField(instanceFalse, "ignoreSurroundingSpaces", false);
        setField(instanceFalse, "ignoreEmptyLines", false);
        setField(instanceFalse, "skipHeaderRecord", false);

        int hashTrue = instanceTrue.hashCode();
        int hashFalse = instanceFalse.hashCode();

        assertNotEquals(hashTrue, hashFalse, "hashCode should differ for different boolean flag combinations");
    }

    @Test
    @Timeout(8000)
    void testHashCode_HeaderArrayAffectsHash() throws Exception {
        CSVFormat instance1 = createCustomCSVFormat();
        CSVFormat instance2 = createCustomCSVFormat();

        setField(instance1, "header", new String[] {"X", "Y"});
        setField(instance2, "header", new String[] {"X", "Y"});
        assertEquals(instance1.hashCode(), instance2.hashCode(), "Equal header arrays should produce same hashCode");

        setField(instance2, "header", new String[] {"X", "Z"});
        assertNotEquals(instance1.hashCode(), instance2.hashCode(), "Different header arrays should produce different hashCode");
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