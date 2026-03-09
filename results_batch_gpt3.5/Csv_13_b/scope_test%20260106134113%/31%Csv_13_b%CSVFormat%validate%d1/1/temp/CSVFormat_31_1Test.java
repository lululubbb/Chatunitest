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
    private java.lang.reflect.Constructor<CSVFormat> constructor;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
        constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
            Character commentMarker, Character escapeCharacter, String[] header) throws Exception {
        return constructor.newInstance(delimiter, quoteChar, quoteMode, commentMarker, escapeCharacter,
                false, false, "\r\n", null, header,
                false, false, false);
    }

    @Test
    @Timeout(8000)
    public void testValidate_validDefault() throws Throwable {
        CSVFormat format = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\', null);
        // Should not throw
        validateMethod.invoke(format);
    }

    @Test
    @Timeout(8000)
    public void testValidate_delimiterIsLineBreak_throws() throws Throwable {
        char[] lineBreaks = {'\r', '\n'};
        for (char lb : lineBreaks) {
            CSVFormat format = createCSVFormat(lb, '"', QuoteMode.MINIMAL, '#', '\\', null);
            InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> validateMethod.invoke(format));
            assertTrue(thrown.getCause() instanceof IllegalArgumentException);
            assertEquals("The delimiter cannot be a line break", thrown.getCause().getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharSameAsDelimiter_throws() throws Throwable {
        CSVFormat format = createCSVFormat(';', ';', QuoteMode.MINIMAL, '#', '\\', null);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> validateMethod.invoke(format));
        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
        assertTrue(thrown.getCause().getMessage().contains("The quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharSameAsDelimiter_throws() throws Throwable {
        CSVFormat format = createCSVFormat(';', '"', QuoteMode.MINIMAL, '#', ';', null);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> validateMethod.invoke(format));
        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
        assertTrue(thrown.getCause().getMessage().contains("The escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerSameAsDelimiter_throws() throws Throwable {
        CSVFormat format = createCSVFormat(';', '"', QuoteMode.MINIMAL, ';', '\\', null);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> validateMethod.invoke(format));
        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
        assertTrue(thrown.getCause().getMessage().contains("The comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsCommentMarker_throws() throws Throwable {
        Character ch = '#';
        CSVFormat format = createCSVFormat(';', ch, QuoteMode.MINIMAL, ch, '\\', null);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> validateMethod.invoke(format));
        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
        assertTrue(thrown.getCause().getMessage().contains("The comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharEqualsCommentMarker_throws() throws Throwable {
        Character ch = '#';
        CSVFormat format = createCSVFormat(';', '"', QuoteMode.MINIMAL, ch, ch, null);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> validateMethod.invoke(format));
        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
        assertTrue(thrown.getCause().getMessage().contains("The comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharNullAndQuoteModeNone_throws() throws Throwable {
        CSVFormat format = createCSVFormat(';', '"', QuoteMode.NONE, '#', null, null);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> validateMethod.invoke(format));
        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
        assertEquals("No quotes mode set but no escape character is set", thrown.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerWithNoDuplicates_passes() throws Throwable {
        String[] header = {"A", "B", "C"};
        CSVFormat format = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\', header);
        // Should not throw
        validateMethod.invoke(format);
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerWithDuplicates_throws() throws Throwable {
        String[] header = {"A", "B", "A"};
        CSVFormat format = createCSVFormat(',', '"', QuoteMode.MINIMAL, '#', '\\', header);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> validateMethod.invoke(format));
        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
        assertTrue(thrown.getCause().getMessage().contains("duplicate entry"));
        assertTrue(thrown.getCause().getMessage().contains("A"));
        assertTrue(thrown.getCause().getMessage().contains(Arrays.toString(header)));
    }
}