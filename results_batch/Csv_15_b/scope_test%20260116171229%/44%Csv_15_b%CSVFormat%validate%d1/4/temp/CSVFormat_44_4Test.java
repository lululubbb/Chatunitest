package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormatValidateTest {

    private Method validateMethod;

    @BeforeEach
    void setUp() throws Exception {
        // Access private validate method
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) throws Throwable {
        try {
            validateMethod.invoke(format);
        } catch (InvocationTargetException e) {
            // unwrap the cause to throw the actual exception thrown by validate()
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterIsLineBreak() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter('\n');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The delimiter cannot be a line break", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsQuoteCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withQuote(',');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsEscapeCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';').withEscape(';');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter('#').withCommentMarker('#');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteEqualsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('!').withCommentMarker('!');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeEqualsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('^').withCommentMarker('^');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeNullAndQuoteModeNone() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null).withQuoteMode(QuoteMode.NONE);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("No quotes mode set but no escape character is set", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderDuplicateEntries() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("a", "b", "a");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The header contains a duplicate entry: 'a'"));
        assertTrue(ex.getMessage().contains("[a, b, a]"));
    }

    @Test
    @Timeout(8000)
    void testValidateValidFormat() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withQuote('"').withEscape('\\').withCommentMarker('#')
                .withQuoteMode(QuoteMode.MINIMAL).withHeader("col1", "col2");
        // Should not throw
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}