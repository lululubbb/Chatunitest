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
import org.mockito.Mockito;

class CSVPrinter_7_3Test {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');
        // default quote policy to null to test defaulting to MINIMAL
        when(format.getQuotePolicy()).thenReturn(null);
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
    void testPrintAndQuote_quoteAll() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.ALL);
        String val = "abc";
        invokePrintAndQuote("abc", val, 0, val.length());
        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteNonNumeric_withNumberObject() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        String val = "123";
        invokePrintAndQuote(123, val, 0, val.length());
        // Should not quote because object is Number
        verify(out).append(val, 0, val.length());
        verify(out, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteNonNumeric_withNonNumberObject() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        String val = "abc";
        invokePrintAndQuote("abc", val, 0, val.length());
        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteNone_callsPrintAndEscape() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NONE);
        String val = "abc";
        CSVPrinter spyPrinter = Mockito.spy(printer);
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        // Stub printAndEscape to do nothing to verify it is called
        doNothing().when(spyPrinter).printAndEscape(val, 0, val.length());

        try {
            method.invoke(spyPrinter, "abc", val, 0, val.length());
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        verify(spyPrinter).printAndEscape(val, 0, val.length());
        verify(out, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteMinimal_emptyLen_newRecordTrue() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        // newRecord is true by default
        String val = "";
        invokePrintAndQuote(null, val, 0, 0);
        verify(out).append('\"');
        verify(out).append(val, 0, 0);
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteMinimal_newRecordTrue_firstCharNonAlphanumeric() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        // newRecord true by default
        String val = "#abc";
        invokePrintAndQuote(null, val, 0, val.length());
        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteMinimal_containsSpecialChar() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        String val = "a,b";
        invokePrintAndQuote(null, val, 0, val.length());
        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteMinimal_endsWithSpace() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        String val = "abc ";
        invokePrintAndQuote(null, val, 0, val.length());
        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteMinimal_noQuoteNeeded() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        String val = "abc";
        // newRecord is true by default, first char 'a' is alphanumeric, no special chars or trailing space
        // So should not quote
        invokePrintAndQuote(null, val, 0, val.length());
        verify(out).append(val, 0, val.length());
        verify(out, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteMinimal_quoteCharInsideValue() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        String val = "ab\"cd";
        invokePrintAndQuote(null, val, 0, val.length());
        // Should double quote the quoteChar inside
        verify(out).append('\"');
        // Because of append calls with segments, verify at least two calls with val and correct ranges
        verify(out, atLeast(2)).append(eq(val), anyInt(), anyInt());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteMinimal_emptyLen_newRecordFalse_noQuote() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        // set newRecord to false using reflection
        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        String val = "";
        invokePrintAndQuote(null, val, 0, 0);
        verify(out).append(val, 0, 0);
        verify(out, never()).append('\"');
    }
}