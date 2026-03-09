package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_17_3Test {

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        String expectedRecordSeparator = "\r\n";

        // When
        String actualRecordSeparator = csvFormat.getRecordSeparator();

        // Then
        assertEquals(expectedRecordSeparator, actualRecordSeparator);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparatorWithReflection() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        String expectedRecordSeparator = "\r\n";

        // When
        String actualRecordSeparator;
        try {
            Method method = CSVFormat.class.getDeclaredMethod("getRecordSeparator");
            method.setAccessible(true);
            actualRecordSeparator = (String) method.invoke(csvFormat);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            actualRecordSeparator = null;
        }

        // Then
        assertEquals(expectedRecordSeparator, actualRecordSeparator);
    }
}