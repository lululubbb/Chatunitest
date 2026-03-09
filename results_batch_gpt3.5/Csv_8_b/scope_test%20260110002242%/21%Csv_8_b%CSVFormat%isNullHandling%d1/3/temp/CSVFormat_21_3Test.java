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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class CSVFormat_21_3Test {

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    void testIsNullHandlingWhenNullStringIsNull() throws Exception {
        // Use DEFAULT which has nullString == null
        CSVFormat format = CSVFormat.DEFAULT;

        // forcibly set nullString to null via reflection to ensure test correctness
        setFinalField(format, "nullString", null);

        assertFalse(format.isNullHandling());
    }

    @Test
    @Timeout(8000)
    void testIsNullHandlingWhenNullStringIsNotNull() throws Exception {
        // Use DEFAULT and forcibly set nullString to "NULL" via reflection to ensure test correctness
        CSVFormat format = CSVFormat.DEFAULT;

        setFinalField(format, "nullString", "NULL");

        assertTrue(format.isNullHandling());
    }
}