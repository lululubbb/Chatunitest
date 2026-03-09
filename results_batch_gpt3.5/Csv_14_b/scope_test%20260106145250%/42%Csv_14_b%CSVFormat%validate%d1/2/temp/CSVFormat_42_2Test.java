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
    void testValidate_Default_NoException() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT;
        assertDoesNotThrow(() -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    void testValidate_DelimiterIsLineBreak_Throws() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter('\n');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("delimiter cannot be a line break"));
    }

    @Test
    @Timeout(8000)
    void testValidate_DelimiterEqualsQuoteCharacter_Throws() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withQuote(',');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("quotechar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidate_DelimiterEqualsEscapeCharacter_Throws() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withEscape(',');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidate_DelimiterEqualsCommentMarker_Throws() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withCommentMarker(',');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidate_QuoteCharacterEqualsCommentMarker_Throws() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('#').withCommentMarker('#');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("comment start character and the quotechar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidate_EscapeCharacterEqualsCommentMarker_Throws() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('#').withCommentMarker('#');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidate_NoEscapeCharacterAndQuoteModeNone_Throws() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT.withEscape(null).withQuoteMode(QuoteMode.NONE);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("no quotes mode set but no escape character is set"));
    }

    @Test
    @Timeout(8000)
    void testValidate_HeaderWithDuplicates_Throws() throws Throwable {
        String[] headerWithDup = new String[] { "a", "b", "a" };
        CSVFormat format = CSVFormat.DEFAULT.withHeader(headerWithDup);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().toLowerCase().contains("header contains a duplicate entry"));
    }

    @Test
    @Timeout(8000)
    void testValidate_HeaderUnique_NoException() throws Throwable {
        String[] uniqueHeader = new String[] { "a", "b", "c" };
        CSVFormat format = CSVFormat.DEFAULT.withHeader(uniqueHeader);
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}