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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_25_5Test {

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

    private void invokeValidate(CSVFormat format) throws Exception {
        Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
        try {
            validateMethod.invoke(format);
        } catch (java.lang.reflect.InvocationTargetException e) {
            // unwrap the underlying exception and rethrow it
            throw (Exception) e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsDelimiter_throws() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',
                Character.valueOf(','),
                Quote.MINIMAL,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("quoteChar character and the delimiter cannot be the same")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeEqualsDelimiter_throws() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',
                Character.valueOf('"'),
                Quote.MINIMAL,
                null,
                Character.valueOf(','),
                false,
                true,
                "\r\n",
                null,
                null,
                false);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("escape character and the delimiter cannot be the same")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentStartEqualsDelimiter_throws() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',
                Character.valueOf('"'),
                Quote.MINIMAL,
                Character.valueOf(','),
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("comment start character and the delimiter cannot be the same")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentStartEqualsQuoteChar_throws() throws Exception {
        Character sameChar = Character.valueOf('"');
        CSVFormat format = createCSVFormat(
                ',',
                sameChar,
                Quote.MINIMAL,
                sameChar,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("comment start character and the quoteChar cannot be the same")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentStartEqualsEscape_throws() throws Exception {
        Character sameChar = Character.valueOf('\\');
        CSVFormat format = createCSVFormat(
                ',',
                Character.valueOf('"'),
                Quote.MINIMAL,
                sameChar,
                sameChar,
                false,
                true,
                "\r\n",
                null,
                null,
                false);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("comment start and the escape character cannot be the same")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_noEscapeAndQuoteNone_throws() throws Exception {
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

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("No quotes mode set but no escape character is set")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerDuplicates_throws() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',
                Character.valueOf('"'),
                Quote.MINIMAL,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                new String[]{"a", "b", "a"},
                false);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("The header contains duplicate names")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_valid_noException() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',
                Character.valueOf('"'),
                Quote.MINIMAL,
                Character.valueOf('#'),
                Character.valueOf('\\'),
                false,
                true,
                "\r\n",
                null,
                new String[]{"a", "b", "c"},
                false);

        try {
            invokeValidate(format);
        } catch (Exception e) {
            fail("validate() should not throw, but threw: " + e.getMessage());
        }
    }
}