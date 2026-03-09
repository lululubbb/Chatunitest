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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_14_2Test {

    @Test
    @Timeout(8000)
    public void testGetNullString() {
        // Given
        String expectedNullString = "testNullString";
        CSVFormat csvFormat = CSVFormat.newFormat(',').withNullString(expectedNullString);

        // When
        String actualNullString = csvFormat.getNullString();

        // Then
        assertEquals(expectedNullString, actualNullString);
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_NullCase() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',').withNullString(null);

        // When
        String actualNullString = csvFormat.getNullString();

        // Then
        assertEquals(null, actualNullString);
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_PrivateMethodInvocation() throws Exception {
        // Given
        String expectedNullString = "testNullString";
        CSVFormat csvFormat = CSVFormat.newFormat(',').withNullString(expectedNullString);

        // When
        String actualNullString = invokePrivateMethod(csvFormat, "getNullString");

        // Then
        assertEquals(expectedNullString, actualNullString);
    }

    private String invokePrivateMethod(Object object, String methodName) throws Exception {
        Method method = object.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        return (String) method.invoke(object);
    }
}