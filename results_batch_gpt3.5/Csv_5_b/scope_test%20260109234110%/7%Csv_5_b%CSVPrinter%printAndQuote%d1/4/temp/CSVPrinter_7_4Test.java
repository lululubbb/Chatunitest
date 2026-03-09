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
        when(format.getQuoteChar()).thenReturn('\"');
        // By default, set quote policy to MINIMAL
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        printer = new CSVPrinter(out, format);
        // newRecord is private boolean, true by default, no need to set here
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
        // Should quote entire value
        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNonNumeric_withNumber() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        String val = "123";
        invokePrintAndQuote(123, val, 0, val.length());
        // Number: should not quote, so append directly
        verify(out).append(val, 0, val.length());
        verify(out, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNonNumeric_withNonNumber() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        String val = "abc";
        invokePrintAndQuote("abc", val, 0, val.length());
        // Non-number: should quote
        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNone_callsPrintAndEscape() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NONE);
        String val = "abc";
        CSVPrinter spyPrinter = spy(new CSVPrinter(out, format));
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        // We spy and verify printAndEscape is called
        method.invoke(spyPrinter, "abc", val, 0, val.length());
        verify(spyPrinter).printAndEscape(val, 0, val.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_emptyLen_newRecordTrue() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        // newRecord is private, set to true by reflection
        setNewRecord(printer, true);
        String val = "";
        invokePrintAndQuote("", val, 0, 0);
        verify(out).append('\"');
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_emptyLen_newRecordFalse() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, false);
        String val = "";
        invokePrintAndQuote("", val, 0, 0);
        // no quote, append empty string
        verify(out).append(val, 0, 0);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_newRecordTrue_startCharInvalid() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, true);
        String val = "#start";
        invokePrintAndQuote("x", val, 0, val.length());
        // Should quote because first char is '#'(COMMENT)
        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_containsSpecialChar() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, false);
        String val = "ab\ncd";
        invokePrintAndQuote("x", val, 0, val.length());
        verify(out).append('\"');
        // Should have appended with doubled quotes if any, here none
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_endsWithSpace() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, false);
        String val = "abc ";
        invokePrintAndQuote("x", val, 0, val.length());
        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_withQuoteCharInside() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, false);
        String val = "ab\"cd";
        invokePrintAndQuote("x", val, 0, val.length());
        verify(out).append('\"');
        // Because quote char inside, it should double quotes inside
        // The code appends segments including the quote char doubled
        // So verify append called at least twice with segments including quote char
        verify(out, atLeast(2)).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_noQuoteNeeded() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, false);
        String val = "abcDEF123";
        invokePrintAndQuote("x", val, 0, val.length());
        // No quote needed, should append directly once
        verify(out).append(val, 0, val.length());
        verify(out, never()).append('\"');
    }

    private void setNewRecord(CSVPrinter printer, boolean value) throws Exception {
        var field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        field.set(printer, value);
    }
}