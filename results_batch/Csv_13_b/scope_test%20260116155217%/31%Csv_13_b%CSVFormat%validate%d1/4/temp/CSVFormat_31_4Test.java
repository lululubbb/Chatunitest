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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatValidateTest {

    private Method validateMethod;
    private Method constructorMethod;

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

    private CSVFormat createFormat(char delimiter, Character quoteChar, Character escapeChar, Character commentMarker,
            QuoteMode quoteMode, String[] header) throws Exception {
        // Use reflection to invoke private constructor
        return CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class)
                .newInstance(
                        delimiter, quoteChar, quoteMode, commentMarker, escapeChar,
                        false, false, "\n", null, null,
                        header, false, false, false);
    }

    @Test
    @Timeout(8000)
    public void testValidate_validDefault() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT;
        invokeValidate(format);
    }

    @Test
    @Timeout(8000)
    public void testValidate_delimiterIsLineBreak_throws() throws Throwable {
        CSVFormat format = createFormat('\n', null, null, null, QuoteMode.MINIMAL, null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The delimiter cannot be a line break", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharSameAsDelimiter_throws() throws Throwable {
        CSVFormat format = createFormat(';', ';', null, null, QuoteMode.MINIMAL, null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharSameAsDelimiter_throws() throws Throwable {
        CSVFormat format = createFormat(',', '"', ',', null, QuoteMode.MINIMAL, null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerSameAsDelimiter_throws() throws Throwable {
        CSVFormat format = createFormat('#', '"', '\\', '#', QuoteMode.MINIMAL, null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerSameAsQuoteChar_throws() throws Throwable {
        CSVFormat format = createFormat(',', '"', '\\', '"', QuoteMode.MINIMAL, null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerSameAsEscapeChar_throws() throws Throwable {
        CSVFormat format = createFormat(',', '"', '#', '#', QuoteMode.MINIMAL, null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeNullQuoteModeNone_throws() throws Throwable {
        CSVFormat format = createFormat(',', '"', null, null, QuoteMode.NONE, null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("No quotes mode set but no escape character is set", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerWithDuplicates_throws() throws Throwable {
        String[] header = new String[] { "a", "b", "a" };
        CSVFormat format = createFormat(',', '"', '\\', '#', QuoteMode.MINIMAL, header);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The header contains a duplicate entry"));
        assertTrue(ex.getMessage().contains("a"));
        assertTrue(ex.getMessage().contains(Arrays.toString(header)));
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerWithoutDuplicates_passes() throws Throwable {
        String[] header = new String[] { "a", "b", "c" };
        CSVFormat format = createFormat(',', '"', '\\', '#', QuoteMode.MINIMAL, header);
        invokeValidate(format);
    }

    @Test
    @Timeout(8000)
    public void testValidate_nullHeader_passes() throws Throwable {
        CSVFormat format = createFormat(',', '"', '\\', '#', QuoteMode.MINIMAL, null);
        invokeValidate(format);
    }
}