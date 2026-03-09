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

import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormatValidateTest {

    private CSVFormat csvFormat;
    private Method validateMethod;

    @BeforeEach
    public void setUp() throws Exception {
        csvFormat = CSVFormat.DEFAULT;
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testValidateNoExceptionDefault() throws Exception {
        // Should not throw any exception for default CSVFormat
        validateMethod.invoke(csvFormat);
    }

    @Test
    @Timeout(8000)
    public void testQuoteCharEqualsDelimiter() throws Exception {
        CSVFormat format = new CSVFormat(
                ',',
                Character.valueOf(','),
                Quote.MINIMAL,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> validateMethod.invoke(format));
        // InvocationTargetException wraps the IllegalStateException, unwrap it
        Throwable cause = ex.getCause();
        assert cause instanceof IllegalStateException;
        assert cause.getMessage().contains("quoteChar character and the delimiter");
    }

    @Test
    @Timeout(8000)
    public void testEscapeEqualsDelimiter() throws Exception {
        CSVFormat format = new CSVFormat(
                ';',
                Character.valueOf('"'),
                Quote.MINIMAL,
                null,
                Character.valueOf(';'),
                false,
                true,
                "\r\n",
                null,
                null,
                false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> validateMethod.invoke(format));
        Throwable cause = ex.getCause();
        assert cause instanceof IllegalStateException;
        assert cause.getMessage().contains("escape character and the delimiter");
    }

    @Test
    @Timeout(8000)
    public void testCommentStartEqualsDelimiter() throws Exception {
        CSVFormat format = new CSVFormat(
                '|',
                Character.valueOf('"'),
                Quote.MINIMAL,
                Character.valueOf('|'),
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> validateMethod.invoke(format));
        Throwable cause = ex.getCause();
        assert cause instanceof IllegalStateException;
        assert cause.getMessage().contains("comment start character and the delimiter");
    }

    @Test
    @Timeout(8000)
    public void testQuoteCharEqualsCommentStart() throws Exception {
        CSVFormat format = new CSVFormat(
                ',',
                Character.valueOf('#'),
                Quote.MINIMAL,
                Character.valueOf('#'),
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> validateMethod.invoke(format));
        Throwable cause = ex.getCause();
        assert cause instanceof IllegalStateException;
        assert cause.getMessage().contains("comment start character and the quoteChar");
    }

    @Test
    @Timeout(8000)
    public void testEscapeEqualsCommentStart() throws Exception {
        CSVFormat format = new CSVFormat(
                ',',
                Character.valueOf('"'),
                Quote.MINIMAL,
                Character.valueOf('^'),
                Character.valueOf('^'),
                false,
                true,
                "\r\n",
                null,
                null,
                false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> validateMethod.invoke(format));
        Throwable cause = ex.getCause();
        assert cause instanceof IllegalStateException;
        assert cause.getMessage().contains("comment start and the escape character");
    }

    @Test
    @Timeout(8000)
    public void testNoEscapeWithQuoteNone() throws Exception {
        CSVFormat format = new CSVFormat(
                ',',
                null,
                Quote.NONE,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> validateMethod.invoke(format));
        Throwable cause = ex.getCause();
        assert cause instanceof IllegalStateException;
        assert cause.getMessage().contains("No quotes mode set but no escape character is set");
    }

    @Test
    @Timeout(8000)
    public void testHeaderWithDuplicates() throws Exception {
        String[] header = new String[] { "a", "b", "a" };
        CSVFormat format = new CSVFormat(
                ',',
                Character.valueOf('"'),
                Quote.MINIMAL,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                header,
                false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> validateMethod.invoke(format));
        Throwable cause = ex.getCause();
        assert cause instanceof IllegalStateException;
        assert cause.getMessage().contains("The header contains duplicate names");
    }

    @Test
    @Timeout(8000)
    public void testHeaderUnique() throws Exception {
        String[] header = new String[] { "a", "b", "c" };
        CSVFormat format = new CSVFormat(
                ',',
                Character.valueOf('"'),
                Quote.MINIMAL,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                header,
                false);
        // Should not throw
        validateMethod.invoke(format);
    }
}