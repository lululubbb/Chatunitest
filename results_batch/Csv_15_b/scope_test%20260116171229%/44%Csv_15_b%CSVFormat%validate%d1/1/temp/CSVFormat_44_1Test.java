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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_44_1Test {

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
            // Unwrap the IllegalArgumentException thrown by validate()
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterIsLineBreak() {
        CSVFormat format = CSVFormat.newFormat('\n'); // '\n' is a line break
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsQuoteCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote(','); // delimiter is ',' by default
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("The quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsEscapeCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape(','); // delimiter is ',' by default
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("The escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker(','); // delimiter is ',' by default
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("The comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteCharacterEqualsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('#').withCommentMarker('#');
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("The comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharacterEqualsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('#').withCommentMarker('#');
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("The comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateNoEscapeCharacterWithQuoteModeNone() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape(null).withQuoteMode(QuoteMode.NONE);
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertEquals("No quotes mode set but no escape character is set", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderWithDuplicates() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("a", "b", "a");
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("The header contains a duplicate entry"));
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderNoDuplicates() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("a", "b", "c");
        assertDoesNotThrow(() -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    void testValidateValidDefault() {
        // DEFAULT with no changes should not throw
        assertDoesNotThrow(() -> invokeValidate(CSVFormat.DEFAULT));
    }

}