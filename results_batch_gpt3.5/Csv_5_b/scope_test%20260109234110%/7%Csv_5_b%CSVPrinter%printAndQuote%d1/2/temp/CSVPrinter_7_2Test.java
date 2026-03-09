package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_7_2Test {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    private Method printAndQuoteMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        out = new StringBuilder();
        format = mock(CSVFormat.class);

        // Default mocks for format
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');
        when(format.getQuotePolicy()).thenReturn(null);

        printer = new CSVPrinter(out, format);

        printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len) throws IOException, InvocationTargetException, IllegalAccessException {
        printAndQuoteMethod.invoke(printer, object, value, offset, len);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_allQuotePolicy_quotesAll() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.ALL);

        invokePrintAndQuote("test", "test", 0, 4);
        assertEquals("\"test\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_nonNumericQuotePolicy_withNumber_noQuotes() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);

        invokePrintAndQuote(123, "123", 0, 3);
        assertEquals("123", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_nonNumericQuotePolicy_withNonNumber_quotes() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);

        invokePrintAndQuote("abc", "abc", 0, 3);
        assertEquals("\"abc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_noneQuotePolicy_callsPrintAndEscape() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NONE);

        // Spy on printer to verify printAndEscape call
        CSVPrinter spyPrinter = Mockito.spy(printer);

        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        // We replace printer with spyPrinter for this test
        printAndQuoteMethod.invoke(spyPrinter, "abc", "abc", 0, 3);

        verify(spyPrinter, times(1)).printAndEscape("abc", 0, 3);
        // Since printAndEscape is mocked, out should be empty (StringBuilder)
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_minimalQuotePolicy_emptyTokenNewRecord_quotes() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        // Set newRecord to true via reflection
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);

        invokePrintAndQuote(null, "", 0, 0);
        assertEquals("\"\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_minimalQuotePolicy_newRecordFirstCharSpecialChar_quotes() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);

        // First char is comment char (# = 35), COMMENT constant assumed 35 or less
        String value = "#abc";
        invokePrintAndQuote("abc", value, 0, value.length());
        assertEquals("\"#abc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_minimalQuotePolicy_containsSpecialChar_quotes() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        String value = "ab,c";
        invokePrintAndQuote("abc", value, 0, value.length());
        assertEquals("\"ab,c\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_minimalQuotePolicy_endsWithSpace_quotes() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        String value = "abc ";
        invokePrintAndQuote("abc", value, 0, value.length());
        assertEquals("\"abc \"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_minimalQuotePolicy_noQuotes() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        String value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length());
        assertEquals("abc", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_minimalQuotePolicy_quotesWithEmbeddedQuoteChars() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        String value = "ab\"c\"d";
        invokePrintAndQuote("abc", value, 0, value.length());
        // Quotes should be doubled inside quoted string
        assertEquals("\"ab\"\"c\"\"d\"", out.toString());
    }
}