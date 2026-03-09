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
import static org.mockito.Mockito.*;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class CSVFormat_21_5Test {

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSet() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When escapeCharacter is not null
        csvFormat = csvFormat.withEscape('\\');

        // Then
        assertTrue(csvFormat.isEscapeCharacterSet());

        // When escapeCharacter is null
        csvFormat = csvFormat.withEscape(null);

        // Then
        assertFalse(csvFormat.isEscapeCharacterSet());
    }

    // Helper method to set private fields using reflection
    private void setPrivateField(Object targetObject, String fieldName, Object newValue) {
        try {
            java.lang.reflect.Field field = targetObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(targetObject, newValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}