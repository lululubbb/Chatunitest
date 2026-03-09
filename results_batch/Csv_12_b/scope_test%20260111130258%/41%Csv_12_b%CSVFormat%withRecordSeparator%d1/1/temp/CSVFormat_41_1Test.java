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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;

public class CSVFormat_41_1Test {

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
    public void testWithRecordSeparatorPrivateMethod() throws Exception {
        // Given
        char recordSeparator = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedFormat = invokePrivateMethod(csvFormat, "withRecordSeparator", char.class, recordSeparator);

        // Then
        assertEquals(String.valueOf(recordSeparator), updatedFormat.getRecordSeparator());
    }

    private <T> T invokePrivateMethod(Object object, String methodName, Class<?> parameterType, Object argument) throws Exception {
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName, parameterType);
        method.setAccessible(true);
        return (T) method.invoke(object, argument);
    }
}