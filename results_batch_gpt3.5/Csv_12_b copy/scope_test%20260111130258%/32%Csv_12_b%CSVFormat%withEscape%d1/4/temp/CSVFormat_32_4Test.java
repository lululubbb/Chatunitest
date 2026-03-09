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

import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;
import org.mockito.verification.VerificationMode;

public class CSVFormat_32_4Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_validEscapeCharacter_returnCSVFormatObject() {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withEscape(escape);

        // Then
        assertNotNull(result);
        assertEquals(escape, result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_invalidEscapeCharacter_throwIllegalArgumentException() {
        // Given
        char escape = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            csvFormat.withEscape(escape);
        });

        // Then
        assertEquals("The escape character cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_privateMethodIsLineBreakCalled() throws Exception {
        // Given
        char escape = 'a';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        CSVFormat spyCsvFormat = spy(csvFormat);

        // When
        spyCsvFormat.withEscape(escape);

        // Then
        try {
            spyCsvFormat.getClass().getDeclaredMethod("isLineBreak", char.class).setAccessible(true);
            verifyPrivate(spyCsvFormat, times(1), "isLineBreak", 'a');
        } catch (NoSuchMethodException | InvocationTargetException e) {
            fail("isLineBreak method not found or could not be invoked");
        }
    }

    private void verifyPrivate(Object object, VerificationMode mode, String methodName, char escape) throws NoSuchMethodException, InvocationTargetException {
        try {
            object.getClass().getDeclaredMethod(methodName, char.class).setAccessible(true);
            verify(object, mode).invoke(methodName, escape);
        } catch (IllegalAccessException e) {
            fail("Method could not be accessed");
        }
    }

    private Object invoke(String methodName, char escape) {
        // Method intentionally left blank for test compilation
        return null;
    }
}