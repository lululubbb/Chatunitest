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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;

public class CSVFormat_38_4Test {

    @Test
    @Timeout(8000)
    public void testWithQuote() {
        // Given
        char quoteChar = '"';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withQuote(quoteChar);

        // Then
        assertEquals(Character.valueOf(quoteChar), newCsvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_Null() {
        // Given
        char quoteChar = '\0'; // Null character
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withQuote(quoteChar);

        // Then
        assertNull(newCsvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_PrivateMethodInvocation() throws Exception {
        // Given
        char quoteChar = '"';
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat spyCsvFormat = spy(csvFormat);

        // When
        CSVFormat newCsvFormat = (CSVFormat) invokePrivateMethod(spyCsvFormat, "withQuote", char.class, quoteChar);

        // Then
        assertEquals(Character.valueOf(quoteChar), newCsvFormat.getQuoteCharacter());
    }

    private Object invokePrivateMethod(Object object, String methodName, Class<?> parameterType, Object argument) throws Exception {
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName, parameterType);
        method.setAccessible(true);
        return method.invoke(object, argument);
    }
}