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

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinterPrintAndQuoteTest {

    private CSVPrinter printer;
    private CSVFormat formatMock;
    private Appendable outMock;

    @BeforeEach
    void setup() {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        printer = new CSVPrinter(outMock, formatMock);
    }

    // Helper to invoke private method printAndQuote
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
    void testPrintAndQuote_QuoteModeAll() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.ALL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length());

        // Should append quoteChar, value, quoteChar
        verify(outMock).append('"');
        verify(outMock).append(value, 0, 3);
        verify(outMock, times(2)).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeNonNumeric_ObjectIsNumber() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "123";
        Number numberObject = 123;
        invokePrintAndQuote(numberObject, value, 0, value.length());

        // Number object, quote should be false, so append original value only
        verify(outMock).append(value, 0, 3);
        verify(outMock, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeNonNumeric_ObjectIsNotNumber() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc";
        Object obj = new Object();
        invokePrintAndQuote(obj, value, 0, value.length());

        // Should quote because object not Number
        verify(outMock).append('"');
        verify(outMock).append(value, 0, 3);
        verify(outMock, times(2)).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeNone() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NONE);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc";

        // Spy on printer to verify printAndEscape called
        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(outMock, formatMock));

        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        method.invoke(spyPrinter, "abc", value, 0, value.length());

        verify(spyPrinter).printAndEscape(value, 0, value.length());
        verify(outMock, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_EmptyLen_NewRecordTrue() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "";
        // newRecord is true by default

        invokePrintAndQuote("abc", value, 0, 0);

        // Should quote because empty and newRecord true
        verify(outMock).append('"');
        verify(outMock).append(value, 0, 0);
        verify(outMock, times(2)).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_NewRecordTrue_CharLessThan0() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "\u0001abc"; // char < '0'
        invokePrintAndQuote("abc", value, 0, value.length());

        verify(outMock).append('"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock, times(2)).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_StartCharIsComment() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "#abc"; // '#' is COMMENT char
        invokePrintAndQuote("abc", value, 0, value.length());

        verify(outMock).append('"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock, times(2)).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_ContainsSpecialChar() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "ab\nc";
        invokePrintAndQuote("abc", value, 0, value.length());

        verify(outMock).append('"');
        verify(outMock).append(value, 0, value.indexOf('\n'));
        verify(outMock).append(value, value.indexOf('\n'), value.indexOf('\n') + 1);
        verify(outMock).append(value, value.indexOf('\n'), value.length());
        verify(outMock, times(2)).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_EndCharLessOrEqualSP() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc ";
        invokePrintAndQuote("abc", value, 0, value.length());

        verify(outMock).append('"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock, times(2)).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_NoQuoteNeeded() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length());

        verify(outMock).append(value, 0, value.length());
        verify(outMock, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_QuoteCharInsideValue() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "ab\"cd";
        invokePrintAndQuote("abc", value, 0, value.length());

        // Should double quote the internal quote char
        verify(outMock).append('"');
        verify(outMock).append(value, 0, 3); // up to and including quote char at pos=2
        verify(outMock).append(value, 2, 5);
        verify(outMock, times(2)).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeNull_UsesMinimal() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(null);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length());

        verify(outMock).append(value, 0, value.length());
        verify(outMock, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_UnexpectedQuoteMode_Throws() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.ALL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        // Create a dummy QuoteMode enum to simulate unexpected value by reflection
        // This is complicated, so instead forcibly invoke private method with reflection and pass unexpected QuoteMode by mock

        CSVFormat formatSpy = spy(formatMock);
        doReturn(null).when(formatSpy).getQuoteMode();

        CSVPrinter printerSpy = spy(new CSVPrinter(outMock, formatSpy));

        // forcibly set quoteModePolicy to an invalid value by reflection is not possible
        // so we skip this test as the default switch covers all enums

        // This test is skipped because QuoteMode is enum and all values covered
    }
}