package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatValidateTest {

    private Method validateMethod;

    @BeforeEach
    public void setUp() throws Exception {
        // Access private validate method
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) throws Exception {
        try {
            validateMethod.invoke(format);
        } catch (java.lang.reflect.InvocationTargetException e) {
            // unwrap the underlying exception
            throw (Exception) e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterIsLineBreak() throws Exception {
        CSVFormat format = CSVFormat.newFormat('\n');
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("The delimiter cannot be a line break")) {
            fail("Expected message about line break delimiter");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsQuoteCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withQuote(',');
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("The quoteChar character and the delimiter cannot be the same")) {
            fail("Expected message about quoteChar and delimiter same");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsEscapeCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withEscape(',');
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("The escape character and the delimiter cannot be the same")) {
            fail("Expected message about escape character and delimiter same");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsCommentMarker() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withCommentMarker(',');
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("The comment start character and the delimiter cannot be the same")) {
            fail("Expected message about comment start char and delimiter same");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteEqualsCommentMarker() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('#').withCommentMarker('#');
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("The comment start character and the quoteChar cannot be the same")) {
            fail("Expected message about comment start char and quoteChar same");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeEqualsCommentMarker() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('#').withCommentMarker('#');
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("The comment start and the escape character cannot be the same")) {
            fail("Expected message about comment start and escape char same");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateNoEscapeCharacterAndQuoteModeNone() throws Exception {
        // Use withEscape(null) is invalid because withEscape expects a char, so we use reflection to set escapeCharacter to null
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.NONE);
        // Use reflection to set escapeCharacter field to null
        java.lang.reflect.Field escapeField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeField.setAccessible(true);
        escapeField.set(format, null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("No quotes mode set but no escape character is set")) {
            fail("Expected message about no quotes mode and no escape char");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderDuplicates() throws Exception {
        String[] headers = new String[] {"a", "b", "a"};
        CSVFormat format = CSVFormat.DEFAULT.withHeader(headers);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("The header contains a duplicate entry")) {
            fail("Expected message about duplicate header entry");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateValidFormat() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';').withQuote('"').withEscape('\\').withCommentMarker('#')
                .withQuoteMode(QuoteMode.ALL).withHeader("a", "b", "c");
        // Should not throw
        invokeValidate(format);
    }
}