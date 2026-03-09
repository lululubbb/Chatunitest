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

public class CSVFormat_41_4Test {

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator() {
        // Given
        char recordSeparator = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedFormat = csvFormat.withRecordSeparator(recordSeparator);

        // Then
        assertEquals(String.valueOf(recordSeparator), updatedFormat.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparatorUsingReflection() throws Exception {
        // Given
        char recordSeparator = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedFormat = invokeWithRecordSeparator(csvFormat, recordSeparator);

        // Then
        assertEquals(String.valueOf(recordSeparator), updatedFormat.getRecordSeparator());
    }

    private CSVFormat invokeWithRecordSeparator(CSVFormat csvFormat, char recordSeparator) throws Exception {
        CSVFormat updatedFormat;
        try {
            Method method = CSVFormat.class.getDeclaredMethod("withRecordSeparator", char.class);
            method.setAccessible(true);
            updatedFormat = (CSVFormat) method.invoke(csvFormat, recordSeparator);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Fallback for older versions of Apache Commons CSV
            updatedFormat = csvFormat.withRecordSeparator(String.valueOf(recordSeparator));
        }
        return updatedFormat;
    }
}