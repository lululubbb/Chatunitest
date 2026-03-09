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
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.io.Reader;
import org.junit.jupiter.api.Test;

public class CSVFormat_23_3Test {

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet_WhenQuoteCharacterIsNotNull_ReturnsTrue() {
        // Arrange
        char delimiter = ',';
        char quoteChar = '"';
        CSVFormat csvFormat = CSVFormat.newFormat(delimiter).withQuote(quoteChar);

        // Act
        boolean result = csvFormat.isQuoteCharacterSet();

        // Assert
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet_WhenQuoteCharacterIsNull_ReturnsFalse() {
        // Arrange
        char delimiter = ',';
        CSVFormat csvFormat = CSVFormat.newFormat(delimiter);

        // Act
        boolean result = csvFormat.isQuoteCharacterSet();

        // Assert
        assertFalse(result);
    }

    // Additional tests for branch coverage can be added here

    // Helper method to access private methods using reflection
    private boolean invokePrivateMethod(CSVFormat csvFormat, String methodName, Class<?>[] parameterTypes, Object... parameters) {
        try {
            java.lang.reflect.Method method = CSVFormat.class.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return (boolean) method.invoke(csvFormat, parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_CharParameter_ReturnsTrue() {
        // Arrange
        char lineBreakChar = '\n';

        // Act
        boolean result = invokePrivateMethod(new CSVFormat(',', null, null, null, null,
                                                        false, true, Constants.CRLF, null, null, false, false),
                                                        "isLineBreak", new Class[]{char.class}, lineBreakChar);

        // Assert
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_CharacterParameter_ReturnsTrue() {
        // Arrange
        Character lineBreakChar = '\n';

        // Act
        boolean result = invokePrivateMethod(new CSVFormat(',', null, null, null, null,
                                                        false, true, Constants.CRLF, null, null, false, false),
                                                        "isLineBreak", new Class[]{Character.class}, lineBreakChar);

        // Assert
        assertTrue(result);
    }
}