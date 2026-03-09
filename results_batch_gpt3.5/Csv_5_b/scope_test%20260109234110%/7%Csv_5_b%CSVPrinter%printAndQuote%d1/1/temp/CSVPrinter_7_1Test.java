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

import org.apache.commons.csv.CSVFormat.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterPrintAndQuoteTest {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('"');
        printer = new CSVPrinter(out, format);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len) throws Throwable {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        try {
            method.invoke(printer, object, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyAll() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.ALL);
        String val = "abc";
        invokePrintAndQuote("abc", val, 0, val.length());

        // Expected: quote = true, so output wrapped in quotes
        verify(out).append('"');
        verify(out).append(val, 0, val.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNonNumericWithNumber() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        Integer number = 123;
        String val = "123";
        invokePrintAndQuote(number, val, 0, val.length());

        // Number instance, quote = false, so append original value only
        verify(out).append(val, 0, val.length());
        verify(out, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNonNumericWithNonNumber() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        String val = "abc";
        invokePrintAndQuote("abc", val, 0, val.length());

        // Non-number, quote = true, so quotes added
        verify(out).append('"');
        verify(out).append(val, 0, val.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNone() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NONE);
        String val = "abc";
        // We spy printer to verify printAndEscape called
        CSVPrinter spyPrinter = spy(new CSVPrinter(out, format));
        when(format.getQuotePolicy()).thenReturn(Quote.NONE);

        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        // Replace printer with spy to check printAndEscape call
        try {
            method.invoke(spyPrinter, "abc", val, 0, val.length());
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        verify(spyPrinter).printAndEscape(val, 0, val.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalNewRecordEmptyLen() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        // Set newRecord true via reflection
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);

        String val = "";
        invokePrintAndQuote("", val, 0, 0);

        // Should quote empty token on new record
        verify(out).append('"');
        verify(out).append(val, 0, 0);
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalNewRecordSpecialCharStart() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);

        // Character less or equal to COMMENT (35 '#'), e.g. 30 (record separator)
        String val = "\u001Eabc"; // ASCII RS character 30
        invokePrintAndQuote("val", val, 0, val.length());

        verify(out).append('"');
        verify(out).append(val, 0, val.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalNoQuoteNeeded() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        // Value with normal characters, no special chars at start or inside or end
        String val = "HelloWorld";
        invokePrintAndQuote("val", val, 0, val.length());

        // Should append original value only, no quotes
        verify(out).append(val, 0, val.length());
        verify(out, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalQuoteBecauseOfDelimiter() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        // Value contains delimiter ',' at position 5
        String val = "Hello,World";
        invokePrintAndQuote("val", val, 0, val.length());

        // Should quote because of delimiter
        verify(out).append('"');
        // The method appends in segments, verify first segment up to and including delimiter
        verify(out).append(val, 0, 6);
        // Then second segment rest of string
        verify(out).append(val, 5, val.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalQuoteBecauseOfQuoteChar() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        // Value contains quote char '"' at position 5
        String val = "Hello\"World";
        invokePrintAndQuote("val", val, 0, val.length());

        // Should quote because of quote char and double the quote char inside
        verify(out).append('"');
        verify(out).append(val, 0, 6);
        verify(out).append(val, 5, val.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalQuoteBecauseOfLf() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        // Value contains LF '\n' at position 5
        String val = "Hello\nWorld";
        invokePrintAndQuote("val", val, 0, val.length());

        verify(out).append('"');
        verify(out).append(val, 0, 6);
        verify(out).append(val, 5, val.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalQuoteBecauseOfCr() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        // Value contains CR '\r' at position 5
        String val = "Hello\rWorld";
        invokePrintAndQuote("val", val, 0, val.length());

        verify(out).append('"');
        verify(out).append(val, 0, 6);
        verify(out).append(val, 5, val.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalQuoteBecauseOfTrailingSpace() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        // Value ends with space character (<= SP)
        String val = "Hello ";
        invokePrintAndQuote("val", val, 0, val.length());

        verify(out).append('"');
        verify(out).append(val, 0, val.length());
        verify(out).append('"');
    }
}