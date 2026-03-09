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

public class CSVFormat_21_1Test {

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        if (field.getType() == char.class) {
            if (value == null) {
                field.setChar(target, (char) 0);
            } else if (value instanceof Character) {
                field.setChar(target, (Character) value);
            } else {
                throw new IllegalArgumentException("Invalid value for char field");
            }
        } else if (field.getType() == Character.class) {
            // For Character field, set the boxed value (can be null)
            field.set(target, value);
        } else {
            field.set(target, value);
        }
    }

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSet_whenEscapeCharacterIsNull() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        setFinalField(csvFormat, "escapeCharacter", null);

        assertFalse(csvFormat.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSet_whenEscapeCharacterIsSet() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        setFinalField(csvFormat, "escapeCharacter", Character.valueOf('\\'));

        assertTrue(csvFormat.isEscapeCharacterSet());
    }
}