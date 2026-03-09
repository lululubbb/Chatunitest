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

    @BeforeEach
    void setUp() throws NoSuchMethodException {
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
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteCharEqualsDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withQuote(',');
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(thrown.getMessage().contains("The quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharEqualsDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withEscape(',');
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(thrown.getMessage().contains("The escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateCommentMarkerEqualsDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withCommentMarker(',');
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(thrown.getMessage().contains("The comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteCharEqualsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('#').withCommentMarker('#');
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(thrown.getMessage().contains("The comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharEqualsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('#').withCommentMarker('#');
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(thrown.getMessage().contains("The comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharNullAndQuoteModeNone() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.NONE).withEscape((Character) null);
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("No quotes mode set but no escape character is set", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderContainsDuplicates() {
        String[] header = new String[] {"a", "b", "a"};
        CSVFormat format = CSVFormat.DEFAULT.withHeader(header);
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(thrown.getMessage().contains("The header contains a duplicate entry: 'a'"));
        assertTrue(thrown.getMessage().contains(Arrays.toString(header)));
    }

    @Test
    @Timeout(8000)
    void testValidateAllValid() throws Throwable {
        String[] header = new String[] {"a", "b", "c"};
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';').withQuote('"').withCommentMarker('#').withEscape('\\').withHeader(header);
        // Should not throw any exception
        invokeValidate(format);
    }
}