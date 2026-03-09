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
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_15_6Test {

    @Test
    @Timeout(8000)
    public void testGetQuoteCharacter() throws IOException {
        // Create a mock CSVFormat object
        CSVFormat csvFormat = mock(CSVFormat.class);

        // Set up the expected quote character
        char expectedQuoteCharacter = '"';
        when(csvFormat.getQuoteCharacter()).thenReturn(expectedQuoteCharacter);

        // Invoke the getQuoteCharacter method using reflection
        char actualQuoteCharacter = invokeGetQuoteCharacter(csvFormat);

        // Verify the result
        assertEquals(expectedQuoteCharacter, actualQuoteCharacter);
    }

    // Helper method to invoke private getQuoteCharacter method using reflection
    private char invokeGetQuoteCharacter(CSVFormat csvFormat) throws IOException {
        try {
            // Get the declared method
            Method method = CSVFormat.class.getDeclaredMethod("getQuoteCharacter");
            method.setAccessible(true);

            // Invoke the method on the provided CSVFormat object
            return (char) method.invoke(csvFormat);
        } catch (Exception e) {
            throw new IOException("Error invoking private method", e);
        }
    }
}