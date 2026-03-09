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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatValidateTest {

    private CSVFormat csvFormat;

    private Method validateMethod;

    public enum Quote {
        NONE, MINIMAL, ALL, NON_NUMERIC
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Default CSVFormat instance for tests
        csvFormat = CSVFormat.DEFAULT;

        // Access private validate method using reflection
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) throws Exception {
        validateMethod.invoke(format);
    }

    private CSVFormat createCSVFormat(
            char delimiter,
            Character quoteChar,
            Quote quotePolicy,
            Character commentStart,
            Character escape,
            boolean ignoreSurroundingSpaces,
            boolean ignoreEmptyLines,
            String recordSeparator,
            String nullString,
            String[] header,
            boolean skipHeaderRecord) throws Exception {

        // Use reflection to create an instance since the constructor is private
        // Get the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                Quote.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                String[].class,
                boolean.class);
        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter,
                quoteChar,
                quotePolicy,
                commentStart,
                escape,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                header,
                skipHeaderRecord);
    }

    @Test
    @Timeout(8000)
    public void testValidate_noException_default() throws Exception {
        // Default CSVFormat should pass validation
        assertDoesNotThrow(() -> invokeValidate(csvFormat));
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsDelimiter_throws() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',
                Character.valueOf(','),
                null,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        assert(ex.getMessage().contains("quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeEqualsDelimiter_throws() throws Exception {
        CSVFormat format = createCSVFormat(
                '\\',
                Character.valueOf('"'),
                null,
                null,
                Character.valueOf('\\'),
                false,
                true,
                "\r\n",
                null,
                null,
                false);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        assert(ex.getMessage().contains("escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentStartEqualsDelimiter_throws() throws Exception {
        CSVFormat format = createCSVFormat(
                '#',
                Character.valueOf('"'),
                null,
                Character.valueOf('#'),
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        assert(ex.getMessage().contains("comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentStartEqualsQuoteChar_throws() throws Exception {
        Character qc = Character.valueOf('"');
        CSVFormat format = createCSVFormat(
                ',',
                qc,
                null,
                qc,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        assert(ex.getMessage().contains("comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentStartEqualsEscape_throws() throws Exception {
        Character esc = Character.valueOf('\\');
        CSVFormat format = createCSVFormat(
                ',',
                Character.valueOf('"'),
                null,
                esc,
                esc,
                false,
                true,
                "\r\n",
                null,
                null,
                false);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        assert(ex.getMessage().contains("comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeNullAndQuotePolicyNone_throws() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',
                null,
                Quote.NONE,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        assert(ex.getMessage().contains("No quotes mode set but no escape character is set"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerWithDuplicates_throws() throws Exception {
        String[] headers = new String[] {"A", "B", "A"};
        CSVFormat format = createCSVFormat(
                ',',
                Character.valueOf('"'),
                null,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                headers,
                false);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        assert(ex.getMessage().contains("header contains duplicate names"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerUnique_noException() throws Exception {
        String[] headers = new String[] {"A", "B", "C"};
        CSVFormat format = createCSVFormat(
                ',',
                Character.valueOf('"'),
                null,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                headers,
                false);

        assertDoesNotThrow(() -> invokeValidate(format));
    }
}