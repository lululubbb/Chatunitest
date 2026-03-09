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
import static org.mockito.Mockito.when;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class CSVFormat_12_5Test {

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        
        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();
        
        // Then
        assertEquals(true, ignoreEmptyLines);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesCustom() {
        // Given
        CSVFormat csvFormat = new CSVFormat(',', '\"', null, null, null, false, true, "\r\n", null, null, false, false)
                .withIgnoreEmptyLines(false);

        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();

        // Then
        assertEquals(false, ignoreEmptyLines);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesUsingMockito() {
        // Given
        CSVFormat csvFormat = mock(CSVFormat.class);
        when(csvFormat.getIgnoreEmptyLines()).thenReturn(true);

        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();

        // Then
        assertEquals(true, ignoreEmptyLines);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesWithReflection() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        boolean ignoreEmptyLines = (boolean) invokePrivateMethod(csvFormat, "getIgnoreEmptyLines");

        // Then
        assertEquals(true, ignoreEmptyLines);
    }

    private boolean invokePrivateMethod(Object object, String methodName) throws Exception {
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        return (boolean) method.invoke(object);
    }
}