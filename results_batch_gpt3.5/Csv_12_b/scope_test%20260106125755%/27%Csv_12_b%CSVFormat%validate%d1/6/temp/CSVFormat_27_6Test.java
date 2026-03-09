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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatValidateTest {

    private Method validateMethod;

    @BeforeEach
    public void setUp() throws Exception {
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) throws Exception {
        try {
            validateMethod.invoke(format);
        } catch (InvocationTargetException e) {
            // unwrap the underlying exception thrown by validate()
            throw (Exception) e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void testValidate_noException_defaultFormat() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        assertDoesNotThrow(() -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsDelimiter_throws() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',')
                                            .withQuote(',');

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assert ex.getMessage().contains("quoteChar character and the delimiter cannot be the same");
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharEqualsDelimiter_throws() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';')
                                            .withQuote('"')
                                            .withEscape(';');

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assert ex.getMessage().contains("escape character and the delimiter cannot be the same");
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsDelimiter_throws() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter('#')
                                            .withQuote('"')
                                            .withCommentMarker('#');

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assert ex.getMessage().contains("comment start character and the delimiter cannot be the same");
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsQuoteChar_throws() throws Exception {
        Character qc = Character.valueOf('"');
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',')
                                            .withQuote(qc)
                                            .withCommentMarker(qc);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assert ex.getMessage().contains("comment start character and the quoteChar cannot be the same");
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsEscapeChar_throws() throws Exception {
        Character cm = Character.valueOf('#');
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',')
                                            .withQuote('"')
                                            .withCommentMarker(cm)
                                            .withEscape(cm);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assert ex.getMessage().contains("comment start and the escape character cannot be the same");
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharacterNullAndQuoteModeNone_throws() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',')
                                            .withQuote(null)
                                            .withQuoteMode(QuoteMode.NONE)
                                            .withEscape(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assert ex.getMessage().contains("No quotes mode set but no escape character is set");
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharacterNullAndQuoteModeNotNone_noThrow() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',')
                                            .withQuote(null)
                                            .withQuoteMode(QuoteMode.MINIMAL)
                                            .withEscape(null);

        assertDoesNotThrow(() -> invokeValidate(format));
    }
}