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

public class CSVFormat_13_2Test {

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpaces() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);

        // When
        boolean result = csvFormat.getIgnoreSurroundingSpaces();

        // Then
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpacesFalse() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);

        // When
        boolean result = csvFormat.getIgnoreSurroundingSpaces();

        // Then
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpacesReflection() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);

        // When
        boolean result = (boolean) invokePrivateMethod(csvFormat, "getIgnoreSurroundingSpaces");

        // Then
        assertTrue(result);
    }

    private boolean invokePrivateMethod(Object object, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        return (boolean) method.invoke(object);
    }
}