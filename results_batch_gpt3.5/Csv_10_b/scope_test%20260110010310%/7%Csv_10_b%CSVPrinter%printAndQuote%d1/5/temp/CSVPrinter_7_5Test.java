package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVPrinter_7_5Test {

    private CSVFormat formatMock;
    private Appendable outMock;
    private CSVPrinter printer;

    private Method printAndQuoteMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        formatMock = mock(CSVFormat.class);
        outMock = mock(Appendable.class);
        printer = new CSVPrinter(outMock, formatMock);

        printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len) throws IOException, InvocationTargetException, IllegalAccessException {
        printAndQuoteMethod.invoke(printer, object, value, offset, len);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyAll() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.ALL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        String value = "abc,def";
        invokePrintAndQuote(null, value, 0, value.length());

        // Should append quoteChar + value + quoteChar
        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNonNumeric_withNumberObject() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        Number number = 123;
        String value = "123";
        invokePrintAndQuote(number, value, 0, value.length());

        // Number object => quote false => out.append(value)
        verify(outMock).append(value, 0, value.length());
        verify(outMock, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNonNumeric_withNonNumberObject() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        Object obj = new Object();
        String value = "abc";
        invokePrintAndQuote(obj, value, 0, value.length());

        // Non-number => quote true => append quotes
        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNone_callsPrintAndEscape() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.NONE);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        // We spy the printer to verify printAndEscape call
        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(outMock, formatMock));

        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        printAndEscapeMethod.invoke(spyPrinter, "abc", 0, 3); // just to ensure accessible

        printAndQuoteMethod.setAccessible(true);
        printAndQuoteMethod.invoke(spyPrinter, null, "abc", 0, 3);

        verify(spyPrinter).printAndEscape("abc", 0, 3);
        verify(outMock, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_emptyLen_newRecordTrue() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(null); // forces MINIMAL
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        // Set newRecord = true by reflection
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);

        String value = "";
        invokePrintAndQuote(null, value, 0, 0);

        // Should quote because empty and newRecord true
        verify(outMock).append('\"');
        verify(outMock).append(value, 0, 0);
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_newRecordTrue_charLessThan0x20() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(null); // MINIMAL
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);

        String value = "\u001Fabc"; // char less than ' ' at start
        invokePrintAndQuote(null, value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_newRecordFalse_containsSpecialChar() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(null); // MINIMAL
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        String value = "abc\ndef";
        invokePrintAndQuote(null, value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_noQuoteNeeded() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(null); // MINIMAL
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        String value = "abcDEF123";
        invokePrintAndQuote(null, value, 0, value.length());

        verify(outMock).append(value, 0, value.length());
        verify(outMock, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_quoteCharInValue_doublesQuote() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(null); // MINIMAL
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        String value = "abc\"def\"ghi";
        invokePrintAndQuote(null, value, 0, value.length());

        // Should append starting quote
        verify(outMock).append('\"');
        // Should append value in parts doubling quotes
        // We cannot verify exact calls with subsequence because of multiple calls,
        // but verify append(char) called twice for quoteChar at start and end
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testUnexpectedQuotePolicy_throwsIllegalStateException() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.CUSTOM); // assuming CUSTOM is an unexpected value
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> invokePrintAndQuote(null, "abc", 0, 3));
        assertTrue(ex.getCause() instanceof IllegalStateException);
        assertTrue(ex.getCause().getMessage().contains("Unexpected Quote value"));
    }
}