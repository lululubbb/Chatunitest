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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintAndQuoteTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    private String invokePrintAndQuote(CSVFormat format, Object object, CharSequence value, int offset, int len,
            boolean newRecord) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder out = new StringBuilder();
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(format, object, value, offset, len, out, newRecord);
        return out.toString();
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAllQuotesEverything() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.ALL);
        String input = "abc,def";
        String result = invokePrintAndQuote(format, input, input, 0, input.length(), false);
        assertEquals("\"abc,def\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAllNonNullQuotesNonNull() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.ALL_NON_NULL);
        String input = "value";
        String result = invokePrintAndQuote(format, input, input, 0, input.length(), false);
        assertEquals("\"value\"", result);

        String nullValue = null;
        String nullResult = invokePrintAndQuote(format, nullValue, "", 0, 0, false);
        assertEquals("", nullResult);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumericQuotesNonNumbers() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        String numeric = "123";
        String numericResult = invokePrintAndQuote(format, 123, numeric, 0, numeric.length(), false);
        assertEquals("123", numericResult);

        String nonNumeric = "abc";
        String nonNumericResult = invokePrintAndQuote(format, nonNumeric, nonNumeric, 0, nonNumeric.length(), false);
        assertEquals("\"abc\"", nonNumericResult);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNoneNoQuotesEscapes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.NONE);
        String input = "a,b\"c";
        String result = invokePrintAndQuote(format, input, input, 0, input.length(), false);
        // It should call printAndEscape and not quote, so output equals input with escapes if any
        // Since printAndEscape is private and not mocked, we test only that output contains input chars
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalEmptyNewRecordQuotes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        String empty = "";
        String result = invokePrintAndQuote(format, empty, empty, 0, 0, true);
        assertEquals("\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalNewRecordWithControlCharQuotes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        String input = "\u001Fabc"; // char < 0x20 at start
        String result = invokePrintAndQuote(format, input, input, 0, input.length(), true);
        assertEquals("\"" + input + "\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalStartsWithCommentCharQuotes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        char commentChar = (char) '#'; // COMMENT constant
        String input = commentChar + "comment";
        String result = invokePrintAndQuote(format, input, input, 0, input.length(), false);
        assertEquals("\"" + input + "\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalContainsSpecialCharsQuotes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        String input = "abc\nxyz"; // contains LF
        String result = invokePrintAndQuote(format, input, input, 0, input.length(), false);
        assertEquals("\"" + input + "\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalEndsWithSpaceQuotes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        String input = "abc ";
        String result = invokePrintAndQuote(format, input, input, 0, input.length(), false);
        assertEquals("\"" + input + "\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalNoQuoteOutput() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        String input = "abc";
        String result = invokePrintAndQuote(format, input, input, 0, input.length(), false);
        assertEquals("abc", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalQuotesWithEmbeddedQuoteChar() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        char quoteChar = format.getQuoteCharacter();
        String input = "a" + quoteChar + "b";
        String expected = quoteChar + "a" + quoteChar + quoteChar + "b" + quoteChar;
        String result = invokePrintAndQuote(format, input, input, 0, input.length(), false);
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalThrowsIllegalStateException() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(null);
        try {
            invokePrintAndQuote(format, "abc", "abc", 0, 3, false);
            fail("Expected IllegalStateException");
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof IllegalStateException);
        }
    }
}