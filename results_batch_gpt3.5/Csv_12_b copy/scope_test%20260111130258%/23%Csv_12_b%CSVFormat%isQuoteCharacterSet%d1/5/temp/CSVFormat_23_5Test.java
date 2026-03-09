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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class CSVFormat_23_5Test {

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        boolean result = csvFormat.isQuoteCharacterSet();

        // Then
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSetWithQuoteCharacter() {
        // Given
        CSVFormat csvFormat = new CSVFormat(',', '\'');

        // When
        boolean result = csvFormat.isQuoteCharacterSet();

        // Then
        assertTrue(result);
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar) {
        return new CSVFormat(delimiter, quoteChar, null, null, null,
                false, true, Constants.CRLF, null, null, false, false);
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSetPrivateMethod() throws Exception {
        // Given
        CSVFormat csvFormat = createCSVFormat(',', '\"');

        // When
        boolean result = invokePrivateMethod(csvFormat, "isQuoteCharacterSet");

        // Then
        assertTrue(result);
    }

    private boolean invokePrivateMethod(CSVFormat csvFormat, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVFormat.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return (boolean) method.invoke(csvFormat);
    }
}