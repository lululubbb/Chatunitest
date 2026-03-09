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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_42_5Test {

    private Method validateMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        // Get private validate method with CSVFormat.class and no parameters
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) throws Throwable {
        try {
            validateMethod.invoke(format);
        } catch (InvocationTargetException e) {
            // unwrap the underlying exception thrown by validate()
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterIsLineBreak() throws Throwable {
        CSVFormat format = CSVFormat.newFormat('\n');
        assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteCharEqualsDelimiter() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT.withQuote(CSVFormat.DEFAULT.getDelimiter());
        assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharEqualsDelimiter() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT.withEscape(CSVFormat.DEFAULT.getDelimiter());
        assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateCommentMarkerEqualsDelimiter() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker(CSVFormat.DEFAULT.getDelimiter());
        assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteCharEqualsCommentMarker() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('#').withCommentMarker('#');
        assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharEqualsCommentMarker() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('#').withCommentMarker('#');
        assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateNoEscapeCharAndQuoteModeNone() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT.withEscape(null).withQuoteMode(QuoteMode.NONE);
        assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderDuplicates() throws Throwable {
        String[] headers = new String[] {"a", "b", "a"};
        CSVFormat format = CSVFormat.DEFAULT.withHeader(headers);
        assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderNoDuplicates() throws Throwable {
        String[] headers = new String[] {"a", "b", "c"};
        CSVFormat format = CSVFormat.DEFAULT.withHeader(headers);
        // Should not throw
        invokeValidate(format);
    }

    @Test
    @Timeout(8000)
    public void testValidateAllValid() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';')
                .withQuote('"')
                .withEscape('\\')
                .withCommentMarker('#')
                .withQuoteMode(QuoteMode.ALL)
                .withHeader("header1", "header2");
        // Should not throw
        invokeValidate(format);
    }
}