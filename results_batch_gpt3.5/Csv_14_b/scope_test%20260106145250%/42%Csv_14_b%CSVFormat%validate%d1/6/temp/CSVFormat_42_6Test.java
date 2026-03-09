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
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormatValidateTest {

    private CSVFormat csvFormat;
    private Method validateMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        // Use default CSVFormat instance for starting point
        csvFormat = CSVFormat.DEFAULT;
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) {
        try {
            validateMethod.invoke(format);
        } catch (InvocationTargetException e) {
            // Unwrap the cause to throw the actual IllegalArgumentException
            Throwable cause = e.getCause();
            if (cause instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) cause;
            }
            throw new RuntimeException(cause);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterIsLineBreak() throws Exception {
        CSVFormat format = new CSVFormat('\n', CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeaderComments(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase(), CSVFormat.DEFAULT.getTrim(),
                CSVFormat.DEFAULT.getTrailingDelimiter());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertEquals("The delimiter cannot be a line break", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsQuoteCharacter() {
        CSVFormat format = new CSVFormat(',', '\"', CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeaderComments(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase(), CSVFormat.DEFAULT.getTrim(),
                CSVFormat.DEFAULT.getTrailingDelimiter()) {
            @Override
            public Character getQuoteCharacter() {
                return Character.valueOf(',');
            }
        };
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertEquals(
                "The quoteChar character and the delimiter cannot be the same (',')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsEscapeCharacter() {
        CSVFormat format = new CSVFormat(',', CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), '\\',
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeaderComments(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase(), CSVFormat.DEFAULT.getTrim(),
                CSVFormat.DEFAULT.getTrailingDelimiter()) {
            @Override
            public Character getEscapeCharacter() {
                return Character.valueOf(',');
            }
        };
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertEquals(
                "The escape character and the delimiter cannot be the same (',')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsCommentMarker() {
        CSVFormat format = new CSVFormat(',', CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                '#', CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeaderComments(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase(), CSVFormat.DEFAULT.getTrim(),
                CSVFormat.DEFAULT.getTrailingDelimiter()) {
            @Override
            public Character getCommentMarker() {
                return Character.valueOf(',');
            }
        };
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertEquals(
                "The comment start character and the delimiter cannot be the same (',')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteCharacterEqualsCommentMarker() {
        CSVFormat format = new CSVFormat(',', '\"', CSVFormat.DEFAULT.getQuoteMode(),
                '#', CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeaderComments(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase(), CSVFormat.DEFAULT.getTrim(),
                CSVFormat.DEFAULT.getTrailingDelimiter()) {
            @Override
            public Character getCommentMarker() {
                return Character.valueOf('\"');
            }
        };
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertEquals(
                "The comment start character and the quoteChar cannot be the same ('\"')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharacterEqualsCommentMarker() {
        CSVFormat format = new CSVFormat(',', '\"', CSVFormat.DEFAULT.getQuoteMode(),
                '#', '\\',
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeaderComments(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase(), CSVFormat.DEFAULT.getTrim(),
                CSVFormat.DEFAULT.getTrailingDelimiter()) {
            @Override
            public Character getCommentMarker() {
                return Character.valueOf('\\');
            }
        };
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertEquals(
                "The comment start and the escape character cannot be the same ('\\')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharacterNullQuoteModeNone() {
        CSVFormat format = new CSVFormat(',', '\"', QuoteMode.NONE,
                CSVFormat.DEFAULT.getCommentMarker(), null,
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeaderComments(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase(), CSVFormat.DEFAULT.getTrim(),
                CSVFormat.DEFAULT.getTrailingDelimiter());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertEquals("No quotes mode set but no escape character is set", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderDuplicates() {
        String[] headerWithDuplicates = new String[] { "col1", "col2", "col1" };
        CSVFormat format = new CSVFormat(',', '\"', CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeaderComments(), headerWithDuplicates,
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase(), CSVFormat.DEFAULT.getTrim(),
                CSVFormat.DEFAULT.getTrailingDelimiter());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().startsWith("The header contains a duplicate entry: 'col1'"));
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains(Arrays.toString(headerWithDuplicates)));
    }

    @Test
    @Timeout(8000)
    void testValidateSuccess() {
        // Use default CSVFormat which is valid
        try {
            invokeValidate(CSVFormat.DEFAULT);
        } catch (IllegalArgumentException e) {
            fail("Validation failed unexpectedly: " + e.getMessage());
        }
    }
}