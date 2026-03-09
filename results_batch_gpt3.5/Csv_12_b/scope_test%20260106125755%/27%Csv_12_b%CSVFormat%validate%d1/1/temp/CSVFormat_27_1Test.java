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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormatValidateTest {

    private Method validateMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) throws Throwable {
        try {
            validateMethod.invoke(format);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsDelimiter_throws() {
        CSVFormat format = new CSVFormat(
                ',',
                ',',
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
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains quoteChar and delimiter same
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharEqualsDelimiter_throws() {
        CSVFormat format = new CSVFormat(
                ',',
                '"',
                QuoteMode.MINIMAL,
                null,
                ',',
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains escape character and delimiter same
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsDelimiter_throws() {
        CSVFormat format = new CSVFormat(
                ',',
                '"',
                QuoteMode.MINIMAL,
                ',',
                null,
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains comment start character and delimiter same
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsQuoteChar_throws() {
        CSVFormat format = new CSVFormat(
                ',',
                '"',
                QuoteMode.MINIMAL,
                '"',
                null,
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains comment start character and quoteChar same
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsEscapeChar_throws() {
        CSVFormat format = new CSVFormat(
                ',',
                '"',
                QuoteMode.MINIMAL,
                '#',
                '#',
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains comment start and escape character same
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
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains no quotes mode set but no escape character set
    }

    @Test
    @Timeout(8000)
    public void testValidate_validConfig_noException() {
        CSVFormat format = new CSVFormat(
                ',',
                '"',
                QuoteMode.MINIMAL,
                '#',
                '\\',
                false,
                false,
                "\n",
                null,
                null,
                false,
                false);
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}