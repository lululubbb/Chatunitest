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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_27_2Test {

    private Method validateMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) {
        try {
            validateMethod.invoke(format);
        } catch (InvocationTargetException e) {
            // unwrap the IllegalArgumentException thrown by validate()
            Throwable cause = e.getCause();
            if (cause instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) cause;
            } else {
                fail("Unexpected exception thrown: " + cause);
            }
        } catch (IllegalAccessException e) {
            fail("Could not access validate method");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsDelimiter_throws() {
        CSVFormat format = new CSVFormat(
                ',',
                Character.valueOf(','),
                QuoteMode.ALL,
                null,
                null,
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharEqualsDelimiter_throws() {
        CSVFormat format = new CSVFormat(
                ';',
                Character.valueOf('"'),
                QuoteMode.ALL,
                null,
                Character.valueOf(';'),
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsDelimiter_throws() {
        CSVFormat format = new CSVFormat(
                '#',
                Character.valueOf('"'),
                QuoteMode.ALL,
                Character.valueOf('#'),
                null,
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsCommentMarker_throws() {
        CSVFormat format = new CSVFormat(
                ',',
                Character.valueOf('#'),
                QuoteMode.ALL,
                Character.valueOf('#'),
                null,
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharEqualsCommentMarker_throws() {
        CSVFormat format = new CSVFormat(
                ',',
                Character.valueOf('"'),
                QuoteMode.ALL,
                Character.valueOf('!'),
                Character.valueOf('!'),
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharNullAndQuoteModeNone_throws() {
        CSVFormat format = new CSVFormat(
                ',',
                null,
                QuoteMode.NONE,
                null,
                null,
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertEquals("No quotes mode set but no escape character is set", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidate_noException() {
        CSVFormat format = new CSVFormat(
                ',',
                Character.valueOf('"'),
                QuoteMode.ALL,
                Character.valueOf('#'),
                Character.valueOf('\\'),
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        // Should not throw
        invokeValidate(format);
    }
}