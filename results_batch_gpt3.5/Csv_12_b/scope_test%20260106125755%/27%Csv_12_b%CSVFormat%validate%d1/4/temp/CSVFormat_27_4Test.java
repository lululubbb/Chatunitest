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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

class CSVFormatValidateTest {

    private void invokeValidate(CSVFormat format) throws Exception {
        Method validate = CSVFormat.class.getDeclaredMethod("validate");
        validate.setAccessible(true);
        try {
            validate.invoke(format);
        } catch (InvocationTargetException e) {
            // unwrap the IllegalArgumentException thrown by validate()
            Throwable cause = e.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            } else {
                throw e;
            }
        }
    }

    @Test
    @Timeout(8000)
    void testValidate_quoteCharEqualsDelimiter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(',')
                .withQuote(',')
                .withQuoteMode(QuoteMode.MINIMAL);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The quoteChar character and the delimiter cannot be the same (',')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidate_escapeCharEqualsDelimiter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withQuote('"')
                .withEscape(';')
                .withQuoteMode(QuoteMode.MINIMAL);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The escape character and the delimiter cannot be the same (';')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidate_commentMarkerEqualsDelimiter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter('#')
                .withQuote('"')
                .withCommentMarker('#')
                .withQuoteMode(QuoteMode.MINIMAL);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The comment start character and the delimiter cannot be the same ('#')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidate_quoteCharEqualsCommentMarker() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(',')
                .withQuote('#')
                .withCommentMarker('#')
                .withQuoteMode(QuoteMode.MINIMAL);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The comment start character and the quoteChar cannot be the same ('#')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidate_escapeCharEqualsCommentMarker() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(',')
                .withQuote('"')
                .withCommentMarker('#')
                .withEscape('#')
                .withQuoteMode(QuoteMode.MINIMAL);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The comment start and the escape character cannot be the same ('#')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidate_escapeCharNullAndQuoteModeNone() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(',')
                .withQuote((Character) null)
                .withEscape((Character) null)
                .withQuoteMode(QuoteMode.NONE);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("No quotes mode set but no escape character is set", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidate_allValid() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(',')
                .withQuote('"')
                .withCommentMarker('#')
                .withEscape('\\')
                .withQuoteMode(QuoteMode.MINIMAL);

        assertDoesNotThrow(() -> invokeValidate(format));
    }
}