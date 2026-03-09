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

import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatValidateTest {

    private CSVFormat csvFormat;
    private Method validateMethod;

    @BeforeEach
    void setUp() throws Exception {
        // Use the default CSVFormat instance as base
        csvFormat = CSVFormat.DEFAULT;

        // Access private validate method by reflection
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) throws Exception {
        // validate() is private and returns void, invoke must handle reflection exceptions
        try {
            validateMethod.invoke(format);
        } catch (java.lang.reflect.InvocationTargetException e) {
            // unwrap the underlying exception and rethrow it
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                throw new RuntimeException(cause);
            }
        }
    }

    @Test
    @Timeout(8000)
    void testValidate_validDefaultFormat() {
        assertDoesNotThrow(() -> invokeValidate(csvFormat));
    }

    @Test
    @Timeout(8000)
    void testValidate_delimiterIsLineBreak() {
        CSVFormat format = csvFormat.withDelimiter('\n');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains "The delimiter cannot be a line break"
    }

    @Test
    @Timeout(8000)
    void testValidate_delimiterEqualsQuoteCharacter() {
        CSVFormat format = csvFormat.withDelimiter(',').withQuote(',');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains "The quoteChar character and the delimiter cannot be the same"
    }

    @Test
    @Timeout(8000)
    void testValidate_delimiterEqualsEscapeCharacter() {
        CSVFormat format = csvFormat.withDelimiter(',').withEscape(',');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains "The escape character and the delimiter cannot be the same"
    }

    @Test
    @Timeout(8000)
    void testValidate_delimiterEqualsCommentMarker() {
        CSVFormat format = csvFormat.withDelimiter(',').withCommentMarker(',');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains "The comment start character and the delimiter cannot be the same"
    }

    @Test
    @Timeout(8000)
    void testValidate_quoteCharacterEqualsCommentMarker() {
        CSVFormat format = csvFormat.withQuote('#').withCommentMarker('#');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains "The comment start character and the quoteChar cannot be the same"
    }

    @Test
    @Timeout(8000)
    void testValidate_escapeCharacterEqualsCommentMarker() {
        CSVFormat format = csvFormat.withEscape('#').withCommentMarker('#');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains "The comment start and the escape character cannot be the same"
    }

    @Test
    @Timeout(8000)
    void testValidate_noEscapeCharacterButQuoteModeNone() {
        CSVFormat format = csvFormat.withEscape(null).withQuoteMode(QuoteMode.NONE);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains "No quotes mode set but no escape character is set"
    }

    @Test
    @Timeout(8000)
    void testValidate_headerContainsDuplicates() {
        String[] duplicateHeader = new String[] {"a", "b", "a"};
        CSVFormat format = csvFormat.withHeader(duplicateHeader);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains "The header contains a duplicate entry"
    }

    @Test
    @Timeout(8000)
    void testValidate_headerNoDuplicates() {
        String[] uniqueHeader = new String[] {"a", "b", "c"};
        CSVFormat format = csvFormat.withHeader(uniqueHeader);
        assertDoesNotThrow(() -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    void testValidate_nullQuoteCharacterDelimiterDifferent() {
        CSVFormat format = csvFormat.withQuote(null).withDelimiter(';').withEscape('\\').withQuoteMode(QuoteMode.ALL);
        assertDoesNotThrow(() -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    void testValidate_escapeCharacterNullQuoteModeNotNone() {
        CSVFormat format = csvFormat.withEscape(null).withQuoteMode(QuoteMode.ALL);
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}