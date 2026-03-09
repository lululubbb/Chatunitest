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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormatValidateTest {

    private void invokeValidate(CSVFormat format) throws Throwable {
        Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
        try {
            validateMethod.invoke(format);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeChar,
                                      boolean ignoreSurroundingSpaces, boolean allowMissingColumnNames,
                                      boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord) throws Exception {
        // Note: The constructor parameter order must match the actual CSVFormat constructor:
        // CSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode, Character commentMarker,
        // Character escapeCharacter, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
        // String recordSeparator, String nullString, String[] header,
        // boolean skipHeaderRecord, boolean allowMissingColumnNames)

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(
                delimiter, quoteChar, quoteMode, commentMarker, escapeChar,
                ignoreSurroundingSpaces, ignoreEmptyLines,
                recordSeparator, nullString, header,
                skipHeaderRecord, allowMissingColumnNames);
    }

    @Test
    @Timeout(8000)
    void validate_noException_whenValidConfig() throws Throwable {
        CSVFormat format = createCSVFormat(
                ',',
                '"',
                QuoteMode.MINIMAL,
                '#',
                '\\',
                false,
                false,
                false,
                "\r\n",
                null,
                null,
                false
        );
        assertDoesNotThrow(() -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    void validate_throwsWhenQuoteCharEqualsDelimiter() throws Throwable {
        CSVFormat format = createCSVFormat(
                '"',
                '"',
                QuoteMode.MINIMAL,
                null,
                null,
                false,
                false,
                false,
                "\r\n",
                null,
                null,
                false
        );
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void validate_throwsWhenEscapeCharEqualsDelimiter() throws Throwable {
        CSVFormat format = createCSVFormat(
                '\\',
                '"',
                QuoteMode.MINIMAL,
                null,
                '\\',
                false,
                false,
                false,
                "\r\n",
                null,
                null,
                false
        );
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void validate_throwsWhenCommentMarkerEqualsDelimiter() throws Throwable {
        CSVFormat format = createCSVFormat(
                '#',
                '"',
                QuoteMode.MINIMAL,
                '#',
                null,
                false,
                false,
                false,
                "\r\n",
                null,
                null,
                false
        );
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void validate_throwsWhenCommentMarkerEqualsQuoteChar() throws Throwable {
        CSVFormat format = createCSVFormat(
                ',',
                '#',
                QuoteMode.MINIMAL,
                '#',
                null,
                false,
                false,
                false,
                "\r\n",
                null,
                null,
                false
        );
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void validate_throwsWhenCommentMarkerEqualsEscapeChar() throws Throwable {
        CSVFormat format = createCSVFormat(
                ',',
                '"',
                QuoteMode.MINIMAL,
                '#',
                '#',
                false,
                false,
                false,
                "\r\n",
                null,
                null,
                false
        );
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void validate_throwsWhenEscapeCharNullAndQuoteModeNone() throws Throwable {
        CSVFormat format = createCSVFormat(
                ',',
                null,
                QuoteMode.NONE,
                null,
                null,
                false,
                false,
                false,
                "\r\n",
                null,
                null,
                false
        );
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("No quotes mode set but no escape character is set", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void validate_noExceptionWhenEscapeCharNullAndQuoteModeNotNone() throws Throwable {
        CSVFormat format = createCSVFormat(
                ',',
                null,
                QuoteMode.MINIMAL,
                null,
                null,
                false,
                false,
                false,
                "\r\n",
                null,
                null,
                false
        );
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}