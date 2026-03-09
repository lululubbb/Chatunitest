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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_25_3Test {

    private void invokeValidate(CSVFormat format) {
        try {
            Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
            validateMethod.setAccessible(true);
            validateMethod.invoke(format);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof IllegalStateException) {
                throw (IllegalStateException) cause;
            }
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsDelimiter_throws() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',')
                .withQuoteChar(',')
                .withQuotePolicy(Quote.MINIMAL);
        assertThrows(IllegalStateException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeEqualsDelimiter_throws() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',')
                .withQuoteChar('"')
                .withQuotePolicy(Quote.MINIMAL)
                .withEscape(',');
        assertThrows(IllegalStateException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentStartEqualsDelimiter_throws() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',')
                .withQuoteChar('"')
                .withQuotePolicy(Quote.MINIMAL)
                .withCommentStart(',');
        assertThrows(IllegalStateException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsCommentStart_throws() {
        Character c = Character.valueOf('#');
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',')
                .withQuoteChar(c)
                .withQuotePolicy(Quote.MINIMAL)
                .withCommentStart(c);
        assertThrows(IllegalStateException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeEqualsCommentStart_throws() {
        Character c = Character.valueOf('#');
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',')
                .withQuoteChar('"')
                .withQuotePolicy(Quote.MINIMAL)
                .withCommentStart(c)
                .withEscape(c);
        assertThrows(IllegalStateException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeNullAndQuotePolicyNone_throws() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',')
                .withQuoteChar((Character) null)
                .withQuotePolicy(Quote.NONE)
                .withEscape((Character) null);
        assertThrows(IllegalStateException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerWithDuplicates_throws() {
        String[] header = new String[] { "a", "b", "a" };
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',')
                .withQuoteChar('"')
                .withQuotePolicy(Quote.MINIMAL)
                .withHeader(header);
        assertThrows(IllegalStateException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidate_validConfiguration_noException() {
        String[] header = new String[] { "a", "b", "c" };
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',')
                .withQuoteChar('"')
                .withQuotePolicy(Quote.MINIMAL)
                .withCommentStart('#')
                .withEscape('\\')
                .withHeader(header);
        try {
            invokeValidate(format);
        } catch (IllegalStateException e) {
            fail("No exception expected but got: " + e.getMessage());
        }
    }
}