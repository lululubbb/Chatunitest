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

class CSVPrinterPrintAndQuoteTest {

    private CSVPrinter printer;
    private CSVFormat formatMock;
    private Appendable outMock;

    @BeforeEach
    void setUp() {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        printer = new CSVPrinter(outMock, formatMock);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len)
            throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class);
        method.setAccessible(true);
        method.invoke(printer, object, value, offset, len);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteAll() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.ALL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        CharSequence value = "abc,def";
        invokePrintAndQuote(null, value, 0, value.length());

        // Expected output: quoted with quotes around entire value
        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteNone_callsPrintAndEscape() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.NONE);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        // Spy on printer to verify printAndEscape call
        CSVPrinter spyPrinter = spy(new CSVPrinter(outMock, formatMock));
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class);
        method.setAccessible(true);

        CharSequence value = "noquote";
        method.invoke(spyPrinter, null, value, 0, value.length());

        // Verify printAndEscape called with correct parameters
        verify(spyPrinter).printAndEscape(value, 0, value.length());
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteNonNumeric_objectIsNumber() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        Number number = 123;
        CharSequence value = "123";
        invokePrintAndQuote(number, value, 0, value.length());

        // Number, NON_NUMERIC should not quote, so append original value
        verify(outMock).append(value, 0, value.length());
        verify(outMock, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteNonNumeric_objectIsNotNumber() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        Object obj = new Object();
        CharSequence value = "abc";
        invokePrintAndQuote(obj, value, 0, value.length());

        // Non-number, NON_NUMERIC should quote value
        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteMinimal_emptyValueNewRecord() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        // Set newRecord true via reflection
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);

        CharSequence value = "";
        invokePrintAndQuote(null, value, 0, 0);

        // Empty token at start of record should be quoted
        verify(outMock).append('\"');
        verify(outMock).append(value, 0, 0);
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteMinimal_newRecordWithSpecialStartChar() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);

        // Value starts with character less than '0' and less than COMMENT ('#' = 35)
        CharSequence value = "\u001Fabc"; // Unit Separator, ASCII 31 (< '#')

        invokePrintAndQuote(null, value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteMinimal_containsLF() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        CharSequence value = "abc\ndef";
        invokePrintAndQuote(null, value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteMinimal_containsDelimiter() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(';');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        CharSequence value = "abc;def";
        invokePrintAndQuote(null, value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteMinimal_endsWithSpace() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        CharSequence value = "abc ";
        invokePrintAndQuote(null, value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteMinimal_noQuoteNeeded() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        CharSequence value = "abcDEF123";
        invokePrintAndQuote(null, value, 0, value.length());

        // Should append without quotes
        verify(outMock).append(value, 0, value.length());
        verify(outMock, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteMinimal_doubleQuoteInside() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        CharSequence value = "ab\"cd\"ef";

        invokePrintAndQuote(null, value, 0, value.length());

        // Should quote and double quotes inside
        verify(outMock).append('\"');
        // The out.append(value, start, pos+1) is called twice for each quote char
        // So verify append called at least once with correct segments and finally append quote
        verify(outMock, atLeast(2)).append(value, anyInt(), anyInt());
        verify(outMock).append('\"');
    }
}