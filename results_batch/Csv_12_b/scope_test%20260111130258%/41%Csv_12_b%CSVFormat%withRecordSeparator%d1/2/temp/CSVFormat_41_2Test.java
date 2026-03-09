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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_41_2Test {

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator() {
        // Given
        char recordSeparator = '\n';
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withRecordSeparator("\r\n");

        // When
        CSVFormat result = csvFormat.withRecordSeparator(recordSeparator);

        // Then
        assertEquals("\n", result.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_String() {
        // Given
        String recordSeparator = "\r\n";
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withRecordSeparator("\n");

        // When
        CSVFormat result = csvFormat.withRecordSeparator(recordSeparator);

        // Then
        assertEquals("\r\n", result.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_PrivateMethodInvocation() throws Exception {
        // Given
        char recordSeparator = '\r';
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withRecordSeparator("\n");

        // When
        CSVFormat result = invokePrivateMethod(csvFormat, "withRecordSeparator", String.class, String.valueOf(recordSeparator));

        // Then
        assertEquals("\r", result.getRecordSeparator());
    }

    private <T> T invokePrivateMethod(Object object, String methodName, Class<?> parameterType, Object argument) throws Exception {
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName, parameterType);
        method.setAccessible(true);
        return (T) method.invoke(object, argument);
    }
}