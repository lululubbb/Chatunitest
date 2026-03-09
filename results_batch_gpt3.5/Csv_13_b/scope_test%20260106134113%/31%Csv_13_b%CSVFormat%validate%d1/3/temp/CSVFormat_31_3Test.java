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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatValidateTest {

    private Method validateMethod;

    @BeforeEach
    public void setUp() throws Exception {
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
    void testValidateDelimiterIsLineBreak() {
        CSVFormat format = CSVFormat.newFormat('\n');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("delimiter") && ex.getMessage().toLowerCase().contains("line break"));
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteCharSameAsDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withQuote(Character.valueOf(','));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("quotechar") && ex.getMessage().toLowerCase().contains("delimiter"));
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharSameAsDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';').withEscape(Character.valueOf(';'));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("escape") && ex.getMessage().toLowerCase().contains("delimiter"));
    }

    @Test
    @Timeout(8000)
    void testValidateCommentMarkerSameAsDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter('#').withCommentMarker(Character.valueOf('#'));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("comment") && ex.getMessage().toLowerCase().contains("delimiter"));
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteCharSameAsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote(Character.valueOf('#')).withCommentMarker(Character.valueOf('#'));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("comment") && ex.getMessage().toLowerCase().contains("quotechar"));
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharSameAsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape(Character.valueOf('#')).withCommentMarker(Character.valueOf('#'));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("comment") && ex.getMessage().toLowerCase().contains("escape"));
    }

    @Test
    @Timeout(8000)
    void testValidateNoEscapeCharWhenQuoteModeNone() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null).withQuoteMode(QuoteMode.NONE);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("no quotes") && ex.getMessage().toLowerCase().contains("escape"));
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderWithDuplicates() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("a", "b", "a");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("duplicate"));
    }

    @Test
    @Timeout(8000)
    void testValidateValidFormatNoException() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';').withQuote('"').withEscape('\\')
                .withCommentMarker('#').withHeader("a", "b", "c");
        assertDoesNotThrow(() -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    void testValidateNullHeader() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader((String[]) null);
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}