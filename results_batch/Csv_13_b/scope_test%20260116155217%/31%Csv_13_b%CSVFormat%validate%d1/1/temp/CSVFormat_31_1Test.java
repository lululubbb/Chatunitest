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
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatValidateTest {

    private CSVFormat csvFormat;
    private Method validateMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        // Access private validate method via reflection
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
    void testValidateDelimiterIsLineBreak() throws Throwable {
        // delimiter is line break (CR)
        csvFormat = CSVFormat.newFormat('\r');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(csvFormat));
        assertEquals("The delimiter cannot be a line break", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteCharSameAsDelimiter() throws Throwable {
        // delimiter and quoteChar are same
        csvFormat = new CSVFormat(',', '"', QuoteMode.MINIMAL, null, null, false, true,
                "\r\n", null, null, null, false, false, false) {
            @Override
            public Character getQuoteCharacter() {
                return '"';
            }
            @Override
            public char getDelimiter() {
                return ',';
            }
        };
        // forcibly set quoteChar to comma (same as delimiter)
        CSVFormat format = new CSVFormat(',', ',', QuoteMode.MINIMAL, null, null, false, true,
                "\r\n", null, null, null, false, false, false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The quoteChar character and the delimiter cannot be the same ('" + (char) ','
                + "')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharSameAsDelimiter() throws Throwable {
        // delimiter and escapeCharacter same
        CSVFormat format = new CSVFormat(',', null, QuoteMode.MINIMAL, null, '\\', false, true,
                "\r\n", null, null, null, false, false, false);
        // change delimiter to backslash to trigger exception
        CSVFormat format2 = new CSVFormat('\\', null, QuoteMode.MINIMAL, null, '\\', false, true,
                "\r\n", null, null, null, false, false, false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format2));
        assertEquals("The escape character and the delimiter cannot be the same ('\\')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateCommentMarkerSameAsDelimiter() throws Throwable {
        // delimiter and commentMarker same
        CSVFormat format = new CSVFormat(',', null, QuoteMode.MINIMAL, '#', null, false, true,
                "\r\n", null, null, null, false, false, false);
        CSVFormat format2 = new CSVFormat('#', null, QuoteMode.MINIMAL, '#', null, false, true,
                "\r\n", null, null, null, false, false, false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format2));
        assertEquals("The comment start character and the delimiter cannot be the same ('#')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteCharEqualsCommentMarker() throws Throwable {
        // quoteCharacter equals commentMarker
        CSVFormat format = new CSVFormat(',', '#', QuoteMode.MINIMAL, '#', null, false, true,
                "\r\n", null, null, null, false, false, false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The comment start character and the quoteChar cannot be the same ('#')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharEqualsCommentMarker() throws Throwable {
        // escapeCharacter equals commentMarker
        CSVFormat format = new CSVFormat(',', null, QuoteMode.MINIMAL, '#', '#', false, true,
                "\r\n", null, null, null, false, false, false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The comment start and the escape character cannot be the same ('#')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharNullQuoteModeNone() throws Throwable {
        CSVFormat format = new CSVFormat(',', null, QuoteMode.NONE, null, null, false, true,
                "\r\n", null, null, null, false, false, false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("No quotes mode set but no escape character is set", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderDuplicates() throws Throwable {
        String[] header = new String[] {"a", "b", "a"};
        CSVFormat format = new CSVFormat(',', null, QuoteMode.MINIMAL, null, null, false, true,
                "\r\n", null, null, header, false, false, false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().startsWith("The header contains a duplicate entry: 'a' in "));
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderNullNoException() throws Throwable {
        CSVFormat format = new CSVFormat(',', null, QuoteMode.MINIMAL, null, null, false, true,
                "\r\n", null, null, null, false, false, false);
        // Should not throw
        invokeValidate(format);
    }

    @Test
    @Timeout(8000)
    void testValidateValidConfig() throws Throwable {
        String[] header = new String[] {"a", "b", "c"};
        CSVFormat format = new CSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\', false, true,
                "\r\n", null, null, header, false, false, false);
        // Should not throw
        invokeValidate(format);
    }
}