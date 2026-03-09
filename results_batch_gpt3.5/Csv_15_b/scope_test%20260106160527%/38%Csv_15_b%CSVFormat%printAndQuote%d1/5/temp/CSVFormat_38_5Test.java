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
    private Appendable appendable;

    private Method printAndQuoteMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        csvFormat = spy(CSVFormat.DEFAULT);
        appendable = new StringBuilder();

        printAndQuoteMethod = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printAndQuoteMethod.setAccessible(true);
    }

    private String invokePrintAndQuote(Object object, CharSequence value, int offset, int len, Appendable out, boolean newRecord)
            throws Throwable {
        try {
            printAndQuoteMethod.invoke(csvFormat, object, value, offset, len, out, newRecord);
            return out.toString();
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll() throws Throwable {
        doReturn(QuoteMode.ALL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote("data", "data", 0, 4, out, false);
        assertEquals("\"data\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAllNonNull_withNullObject() throws Throwable {
        doReturn(QuoteMode.ALL_NON_NULL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote(null, "null", 0, 4, out, false);
        assertEquals("null", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAllNonNull_withNonNullObject() throws Throwable {
        doReturn(QuoteMode.ALL_NON_NULL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote("string", "string", 0, 6, out, false);
        assertEquals("\"string\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_withNumber() throws Throwable {
        doReturn(QuoteMode.NON_NUMERIC).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote(123, "123", 0, 3, out, false);
        assertEquals("123", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_withNonNumber() throws Throwable {
        doReturn(QuoteMode.NON_NUMERIC).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote("abc", "abc", 0, 3, out, false);
        assertEquals("\"abc\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNone_callsPrintAndEscape() throws Throwable {
        doReturn(QuoteMode.NONE).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        CSVFormat spyFormat = spy(csvFormat);
        doNothing().when(spyFormat).printAndEscape(any(), anyInt(), anyInt(), any());

        Method printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class,
                Appendable.class);
        printAndEscapeMethod.setAccessible(true);

        // We replace csvFormat with spyFormat to test printAndEscape call
        printAndQuoteMethod.invoke(spyFormat, "value", "value", 0, 5, appendable, false);

        verify(spyFormat, times(1)).printAndEscape("value", 0, 5, appendable);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_emptyLenNewRecordTrue() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote("obj", "", 0, 0, out, true);
        assertEquals("\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_emptyLenNewRecordFalse() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote("obj", "", 0, 0, out, false);
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_newRecordWithCharOutOfRange() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        // char at pos = 0 is 0x00 (less than 0x20)
        CharSequence value = "\u001Fabc";

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote("obj", value, 0, value.length(), out, true);
        assertEquals("\"" + value + "\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_newRecordWithCharLessOrEqualComment() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        // char at pos = 0 is '#' (comment char)
        CharSequence value = "#comment";

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote("obj", value, 0, value.length(), out, false);
        assertEquals("\"" + value + "\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsLF() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        CharSequence value = "abc\ndef";

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote("obj", value, 0, value.length(), out, false);
        assertEquals("\"" + value + "\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsCR() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        CharSequence value = "abc\rdef";

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote("obj", value, 0, value.length(), out, false);
        assertEquals("\"" + value + "\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsQuoteChar() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        CharSequence value = "abc\"def";

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote("obj", value, 0, value.length(), out, false);
        // Quotes inside value should be doubled
        assertEquals("\"abc\"\"def\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsDelimiter() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(';').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        CharSequence value = "abc;def";

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote("obj", value, 0, value.length(), out, false);
        assertEquals("\"abc;def\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_noQuoteNeeded() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        CharSequence value = "abc";

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote("obj", value, 0, value.length(), out, false);
        assertEquals("abc", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_lastCharLessOrEqualSP() throws Throwable {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        CharSequence value = "abc ";

        StringBuilder out = new StringBuilder();
        String result = invokePrintAndQuote("obj", value, 0, value.length(), out, false);
        assertEquals("\"abc \"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeUnexpected_throwsIllegalStateException() throws Throwable {
        doReturn(null).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        // Force an unexpected QuoteMode by mocking getQuoteMode to return a dummy enum
        QuoteMode dummyQuoteMode = mock(QuoteMode.class);
        doReturn(dummyQuoteMode).when(csvFormat).getQuoteMode();

        // We override equals for dummyQuoteMode to always return false so switch default triggers
        when(dummyQuoteMode.toString()).thenReturn("DUMMY");

        // We need to call the method with reflection and expect exception
        Throwable thrown = assertThrows(IllegalStateException.class, () -> {
            invokePrintAndQuote("obj", "abc", 0, 3, new StringBuilder(), false);
        });
        assertTrue(thrown.getMessage().contains("Unexpected Quote value"));
    }
}