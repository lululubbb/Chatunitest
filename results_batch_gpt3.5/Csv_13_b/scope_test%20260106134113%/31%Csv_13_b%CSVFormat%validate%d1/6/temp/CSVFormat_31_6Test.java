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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatValidateTest {

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

    private CSVFormat createFormatWithFields(
            char delimiter,
            Character quoteCharacter,
            QuoteMode quoteMode,
            Character commentMarker,
            Character escapeCharacter,
            String[] header) throws Exception {
        return CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class)
                .newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                        false, false, "\n", null, null, header,
                        false, false, false);
    }

    @Test
    @Timeout(8000)
    public void testValidate_validFormat_noException() throws Throwable {
        CSVFormat format = createFormatWithFields(',', '"', QuoteMode.MINIMAL, '#', '\\', new String[]{"a", "b"});
        assertDoesNotThrow(() -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidate_delimiterIsLineBreak_throws() throws Throwable {
        char[] lineBreaks = {'\r', '\n'};
        for (char lb : lineBreaks) {
            CSVFormat format = createFormatWithFields(lb, '"', QuoteMode.MINIMAL, '#', '\\', new String[]{"a", "b"});
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
            assertEquals("The delimiter cannot be a line break", ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharacterEqualsDelimiter_throws() throws Throwable {
        CSVFormat format = createFormatWithFields('\'', '\'', QuoteMode.MINIMAL, '#', '\\', new String[]{"a", "b"});
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharacterEqualsDelimiter_throws() throws Throwable {
        CSVFormat format = createFormatWithFields(',', '"', QuoteMode.MINIMAL, '#', ',', new String[]{"a", "b"});
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsDelimiter_throws() throws Throwable {
        CSVFormat format = createFormatWithFields(',', '"', QuoteMode.MINIMAL, ',', '\\', new String[]{"a", "b"});
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsQuoteCharacter_throws() throws Throwable {
        CSVFormat format = createFormatWithFields(',', '"', QuoteMode.MINIMAL, '"', '\\', new String[]{"a", "b"});
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsEscapeCharacter_throws() throws Throwable {
        CSVFormat format = createFormatWithFields(',', '"', QuoteMode.MINIMAL, '\\', '\\', new String[]{"a", "b"});
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharacterNullQuoteModeNone_throws() throws Throwable {
        CSVFormat format = createFormatWithFields(',', '"', QuoteMode.NONE, '#', null, new String[]{"a", "b"});
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("No quotes mode set but no escape character is set", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerWithDuplicates_throws() throws Throwable {
        String[] header = new String[]{"a", "b", "a"};
        CSVFormat format = createFormatWithFields(',', '"', QuoteMode.MINIMAL, '#', '\\', header);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The header contains a duplicate entry"));
        assertTrue(ex.getMessage().contains("a"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerNull_noException() throws Throwable {
        CSVFormat format = createFormatWithFields(',', '"', QuoteMode.MINIMAL, '#', '\\', null);
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}