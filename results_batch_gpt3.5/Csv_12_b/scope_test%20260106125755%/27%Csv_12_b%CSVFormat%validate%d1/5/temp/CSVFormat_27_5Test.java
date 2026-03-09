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

public class CSVFormatValidateTest {

    private CSVFormat csvFormat;
    private Method validateMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        // Access private validate method via reflection
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    // Helper to invoke validate method and handle exceptions
    private void invokeValidate(CSVFormat format) throws Throwable {
        try {
            validateMethod.invoke(format);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_noException_default() throws Throwable {
        csvFormat = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL).withEscape('\\');
        // Should pass without exceptions
        invokeValidate(csvFormat);
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsDelimiter_throws() {
        csvFormat = new CSVFormat(
                ',',
                Character.valueOf(','),
                QuoteMode.MINIMAL,
                null,
                null,
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeValidate(csvFormat);
        });
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharEqualsDelimiter_throws() {
        csvFormat = new CSVFormat(
                ';',
                Character.valueOf('"'),
                QuoteMode.MINIMAL,
                null,
                Character.valueOf(';'),
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeValidate(csvFormat);
        });
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsDelimiter_throws() {
        csvFormat = new CSVFormat(
                '#',
                Character.valueOf('"'),
                QuoteMode.MINIMAL,
                Character.valueOf('#'),
                null,
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeValidate(csvFormat);
        });
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsCommentMarker_throws() {
        csvFormat = new CSVFormat(
                ',',
                Character.valueOf('!'),
                QuoteMode.MINIMAL,
                Character.valueOf('!'),
                null,
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeValidate(csvFormat);
        });
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharEqualsCommentMarker_throws() {
        csvFormat = new CSVFormat(
                ',',
                Character.valueOf('"'),
                QuoteMode.MINIMAL,
                Character.valueOf('?'),
                Character.valueOf('?'),
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeValidate(csvFormat);
        });
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_noEscapeCharacterAndQuoteModeNone_throws() {
        csvFormat = new CSVFormat(
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
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeValidate(csvFormat);
        });
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("No quotes mode set but no escape character is set"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharacterNullQuoteModeNotNone_noThrow() throws Throwable {
        csvFormat = new CSVFormat(
                ',',
                null,
                QuoteMode.MINIMAL,
                null,
                null,
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        // Should not throw
        invokeValidate(csvFormat);
    }
}