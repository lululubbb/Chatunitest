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
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;

public class CSVFormat_32_3Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_validEscapeCharacter() {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withEscape(escape);

        // Then
        assertEquals(escape, result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_invalidEscapeCharacter() {
        // Given
        char escape = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            csvFormat.withEscape(escape);
        });

        // Then
        assertEquals("The escape character cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_privateMethodInvocation() throws Exception {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = invokePrivateMethod(csvFormat, "withEscape", Character.class, escape);

        // Then
        assertEquals(escape, result.getEscapeCharacter());
    }

    private <T> T invokePrivateMethod(Object object, String methodName, Class<?> parameterType, Object argument) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName, parameterType);
        method.setAccessible(true);
        return (T) method.invoke(object, argument);
    }
}