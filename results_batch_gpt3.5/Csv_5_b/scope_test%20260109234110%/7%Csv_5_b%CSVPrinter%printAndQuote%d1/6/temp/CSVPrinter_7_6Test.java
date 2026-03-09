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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinterPrintAndQuoteTest {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        // Default delimiter and quoteChar
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');
        when(format.getQuotePolicy()).thenReturn(null); // to test default MINIMAL
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

        // Should append quote, value, quote
        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNonNumericObject() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);

        String val = "hello";
        invokePrintAndQuote("hello", val, 0, val.length());

        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNonNumericNumber() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);

        String val = "123";
        Integer number = 123;
        invokePrintAndQuote(number, val, 0, val.length());

        // Number should not be quoted, so append original value only
        verify(out).append(val, 0, val.length());
        verify(out, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNone() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NONE);

        // Spy on printer to verify printAndEscape is called
        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(out, format));
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        // We expect printAndEscape to be called once, so mock it
        doNothing().when(spyPrinter).printAndEscape(any(CharSequence.class), anyInt(), anyInt());

        method.invoke(spyPrinter, "abc", "abc", 0, 3);

        verify(spyPrinter).printAndEscape("abc", 0, 3);
        verify(out, never()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_emptyTokenNewRecord() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(null); // default MINIMAL

        // newRecord true triggers quoting empty token
        String val = "";
        invokePrintAndQuote("", val, 0, 0);

        verify(out).append('\"');
        verify(out).append(val, 0, 0);
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_newRecordFirstCharSpecial() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(null); // MINIMAL

        // newRecord true and first char < '0' or between non-alphanumeric ranges triggers quote
        String val = "#value";
        invokePrintAndQuote(val, val, 0, val.length());

        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_newRecordFirstCharLessEqualCommentChar() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(null); // MINIMAL

        String val = "\u0000value";
        invokePrintAndQuote(val, val, 0, val.length());

        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_containsLFCRQuoteDelim() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(null); // MINIMAL

        String val = "abc\ndef";
        invokePrintAndQuote(val, val, 0, val.length());

        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_endsWithSpace() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(null); // MINIMAL

        String val = "abc ";
        invokePrintAndQuote(val, val, 0, val.length());

        verify(out).append('\"');
        verify(out).append(val, 0, val.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_noQuoteNeeded() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(null); // MINIMAL

        String val = "abc";
        // newRecord false disables empty token quote, so set false by reflection
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        invokePrintAndQuote(val, val, 0, val.length());

        verify(out).append(val, 0, val.length());
        verify(out, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_quoteCharInsideValue() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(null); // MINIMAL

        String val = "a\"b\"c";
        invokePrintAndQuote(val, val, 0, val.length());

        // Should append quote, then value with doubled quotes, then quote
        verify(out).append('\"');

        // The method appends in segments, so verify append called multiple times with correct segments
        // It appends from start to pos+1 on quoteChar found, doubling quotes

        // We expect at least 3 append calls with CharSequence:
        // "a\"", "\"b\"", "\"c"
        // But since out is a mock, verify at least these calls:

        verify(out, atLeast(3)).append(any(CharSequence.class), anyInt(), anyInt());

        verify(out).append('\"');
    }

}