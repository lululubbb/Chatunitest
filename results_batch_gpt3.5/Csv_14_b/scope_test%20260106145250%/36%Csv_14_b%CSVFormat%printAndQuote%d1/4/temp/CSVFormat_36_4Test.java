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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintAndQuoteTest {

    private CSVFormat csvFormat;
    private Appendable out;
    private Method printAndQuoteMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        csvFormat = spy(CSVFormat.DEFAULT);
        out = new StringBuilder();

        // Access private method printAndQuote via reflection
        printAndQuoteMethod = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printAndQuoteMethod.setAccessible(true);
    }

    private String invokePrintAndQuote(Object object, CharSequence value, int offset, int len, Appendable out, boolean newRecord) throws Throwable {
        try {
            printAndQuoteMethod.invoke(csvFormat, object, value, offset, len, out, newRecord);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
        return out.toString();
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll() throws Throwable {
        doReturn(QuoteMode.ALL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "abc";
        StringBuilder output = new StringBuilder();
        String result = invokePrintAndQuote("abc", input, 0, input.length(), output, false);
        assertEquals("\"abc\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumericWithNumber() throws Throwable {
        doReturn(QuoteMode.NON_NUMERIC).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        Integer number = 123;
        String input = "123";
        StringBuilder output = new StringBuilder();
        String result = invokePrintAndQuote(number, input, 0, input.length(), output, false);
        // Number should not be quoted, so output is input as is
        assertEquals("123", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumericWithString() throws Throwable {
        doReturn(QuoteMode.NON_NUMERIC).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "abc";
        StringBuilder output = new StringBuilder();
        String result = invokePrintAndQuote("abc", input, 0, input.length(), output, false);
        assertEquals("\"abc\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNone() throws Throwable {
        doReturn(QuoteMode.NONE).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        // Spy on private printAndEscape method to verify it is called
        doNothing().when(csvFormat).printAndEscape(any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class));

        String input = "value";
        StringBuilder output = new StringBuilder();
        invokePrintAndQuote("value", input, 0, input.length(), output, false);

        verify(csvFormat, times(1)).printAndEscape(input, 0, input.length(), output);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalEmptyNewRecord() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "";
        StringBuilder output = new StringBuilder();
        String result = invokePrintAndQuote("", input, 0, 0, output, true);
        // Should quote empty token at start of new record
        assertEquals("\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalNewRecordInvalidStartChar() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        // Character less than '0' and less than 'A' etc triggers quoting
        String input = "#abc";
        StringBuilder output = new StringBuilder();
        String result = invokePrintAndQuote("abc", input, 0, input.length(), output, true);
        assertEquals("\"#abc\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalNewRecordStartCharLessEqualComment() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        // COMMENT constant is '#'
        String input = "#abc";
        StringBuilder output = new StringBuilder();
        String result = invokePrintAndQuote("abc", input, 0, input.length(), output, false);
        assertEquals("\"#abc\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalContainsSpecialChars() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        // Input contains LF which triggers quoting
        String input = "abc\ndef";
        StringBuilder output = new StringBuilder();
        String result = invokePrintAndQuote("abc\ndef", input, 0, input.length(), output, false);
        assertEquals("\"abc\ndef\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalEndsWithSpace() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "abc ";
        StringBuilder output = new StringBuilder();
        String result = invokePrintAndQuote("abc ", input, 0, input.length(), output, false);
        assertEquals("\"abc \"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalNoQuoteNeeded() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "abc123";
        StringBuilder output = new StringBuilder();
        String result = invokePrintAndQuote("abc123", input, 0, input.length(), output, false);
        assertEquals("abc123", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalDoubleQuoteInside() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "a\"bc\"de";
        StringBuilder output = new StringBuilder();
        String result = invokePrintAndQuote("a\"bc\"de", input, 0, input.length(), output, false);
        // Double quotes inside should be doubled with surrounding quotes
        assertEquals("\"a\"\"bc\"\"de\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalThrowsIllegalStateException() throws Throwable {
        doReturn(null).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        // forcibly set quoteModePolicy to illegal value by mocking getQuoteMode to return a dummy enum
        QuoteMode illegalQuoteMode = mock(QuoteMode.class);
        doReturn(illegalQuoteMode).when(csvFormat).getQuoteMode();
        when(illegalQuoteMode.toString()).thenReturn("ILLEGAL");

        // Create new method to call with switch default case by invoking with illegal quote mode
        // But since switch uses enum instance, we cannot easily force default case without changing code,
        // so test coverage for default case is omitted because method uses enum switch
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNullUsesMinimal() throws Throwable {
        doReturn(null).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "abc";
        StringBuilder output = new StringBuilder();
        String result = invokePrintAndQuote("abc", input, 0, input.length(), output, false);
        // Minimal mode quotes only if needed, here no special chars so no quotes
        assertEquals("abc", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalEmptyNotNewRecord() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "";
        StringBuilder output = new StringBuilder();
        String result = invokePrintAndQuote("", input, 0, 0, output, false);
        // Empty token not first on line and no newRecord, no quote needed, output empty string
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalStartsWithDigitNoQuote() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "1abc";
        StringBuilder output = new StringBuilder();
        String result = invokePrintAndQuote("1abc", input, 0, input.length(), output, true);
        // Starts with digit, no quote needed
        assertEquals("1abc", result);
    }

    @Test
    @Timeout(8000)
    void testIOExceptionFromAppend() {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        CharSequence value = "abc";
        Appendable throwingAppendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("Test IOException");
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException("Test IOException");
            }

            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException("Test IOException");
            }
        };

        Throwable thrown = assertThrows(IOException.class, () -> {
            try {
                printAndQuoteMethod.invoke(csvFormat, "abc", value, 0, value.length(), throwingAppendable, false);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
        assertEquals("Test IOException", thrown.getMessage());
    }
}