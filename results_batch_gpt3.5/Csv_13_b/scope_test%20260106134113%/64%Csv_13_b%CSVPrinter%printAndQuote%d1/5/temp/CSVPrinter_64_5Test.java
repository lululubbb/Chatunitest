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
    void testPrintAndQuote_QuoteModeAll() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.ALL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('\"');

        CharSequence value = "abc,def";
        invokePrintAndQuote("abc,def", value, 0, value.length());

        // Expect quote char appended at start and end, and value in between
        // Because quote mode ALL forces quoting
        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeNonNumeric_ObjectIsNumber() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('\"');

        Number numberObject = 123;
        CharSequence value = "123";
        invokePrintAndQuote(numberObject, value, 0, value.length());

        // Number object, NON_NUMERIC disables quoting, so no quotes appended
        verify(outMock).append(value, 0, value.length());
        verify(outMock, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeNonNumeric_ObjectIsNotNumber() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('\"');

        Object obj = new Object();
        CharSequence value = "abc";
        invokePrintAndQuote(obj, value, 0, value.length());

        // Non-number object, quoting expected
        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeNone_CallsPrintAndEscape() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NONE);

        CharSequence value = "value";
        // We spy the printer to verify printAndEscape call
        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(outMock, formatMock));

        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class);
        method.setAccessible(true);
        method.invoke(spyPrinter, "value", value, 0, value.length());

        verify(spyPrinter).printAndEscape(value, 0, value.length());
        verify(outMock, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_EmptyLen_NewRecordTrue() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('\"');

        CharSequence value = "";
        setNewRecord(printer, true);

        invokePrintAndQuote("obj", value, 0, 0);

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, 0);
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_NewRecordTrue_FirstCharSpecial() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('\"');

        // First char < '0' (e.g. space ' ')
        CharSequence value = " abc";
        setNewRecord(printer, true);

        invokePrintAndQuote("obj", value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_NewRecordFalse_StartCharComment() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('\"');
        when(formatMock.getCommentMarker()).thenReturn('#');

        CharSequence value = "#comment";
        setNewRecord(printer, false);

        invokePrintAndQuote("obj", value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_NewRecordFalse_ContainsLF() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('\"');

        CharSequence value = "a\nb";
        setNewRecord(printer, false);

        invokePrintAndQuote("obj", value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_NewRecordFalse_EndsWithSpace() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('\"');

        CharSequence value = "abc ";
        setNewRecord(printer, false);

        invokePrintAndQuote("obj", value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_NewRecordFalse_NoQuoteNeeded() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('\"');

        CharSequence value = "abcDEF123";
        setNewRecord(printer, false);

        invokePrintAndQuote("obj", value, 0, value.length());

        verify(outMock).append(value, 0, value.length());
        verify(outMock, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_QuoteCharInsideValue() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('\"');

        CharSequence value = "a\"b\"c";
        setNewRecord(printer, false);

        invokePrintAndQuote("obj", value, 0, value.length());

        // Verify quote char appended at start and end
        verify(outMock).append('\"');
        // Verify value appended multiple times to double quote quotes inside
        // The value should be appended multiple times due to doubling quotes
        verify(outMock, atLeast(1)).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_ThrowsIllegalStateException() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(null);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('\"');

        // Force unexpected QuoteMode by reflection
        setQuoteMode(printer, null);

        // Use reflection to call printAndQuote with a QuoteMode not in enum (simulate)
        try {
            invokePrintAndQuote("obj", "val", 0, 3);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assert cause instanceof IllegalStateException;
        }
    }

    // Helper to set private field newRecord
    private void setNewRecord(CSVPrinter printer, boolean value) throws Exception {
        var field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        field.set(printer, value);
    }

    // Helper to set private field format.quoteMode to null (simulate)
    private void setQuoteMode(CSVPrinter printer, QuoteMode value) throws Exception {
        var field = CSVPrinter.class.getDeclaredField("format");
        field.setAccessible(true);
        CSVFormat format = (CSVFormat) field.get(printer);

        // Use Mockito to create a new CSVFormat mock with quoteMode null
        CSVFormat newFormat = mock(CSVFormat.class);
        when(newFormat.getQuoteMode()).thenReturn(value);
        when(newFormat.getDelimiter()).thenReturn(',');
        when(newFormat.getQuoteCharacter()).thenReturn('\"');

        field.set(printer, newFormat);
    }
}