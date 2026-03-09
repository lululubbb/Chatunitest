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
import org.mockito.ArgumentCaptor;

class CSVPrinterPrintAndQuoteTest {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    private Method printAndQuoteMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);

        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');
        // default quote policy will be set in individual tests

        printer = new CSVPrinter(out, format);

        // Access private method printAndQuote
        printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len) throws Throwable {
        try {
            printAndQuoteMethod.invoke(printer, object, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    // Helper to capture appended content
    private ArgumentCaptor<CharSequence> captureAppend() throws IOException {
        ArgumentCaptor<CharSequence> captor = ArgumentCaptor.forClass(CharSequence.class);
        verify(out, atLeastOnce()).append(captor.capture());
        return captor;
    }

    @Test
    @Timeout(8000)
    void testQuoteAll() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.ALL);

        String value = "abc,def";
        invokePrintAndQuote("abc,def", value, 0, value.length());

        // Should quote entire value with quotes around
        verify(out).append('\"');
        verify(out).append(value, 0, value.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuoteNonNumeric_withNumberObject() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);

        String value = "12345";
        Number number = 12345;
        invokePrintAndQuote(number, value, 0, value.length());

        // Number object, should not quote, just append raw
        verify(out).append(value, 0, value.length());
        verify(out, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuoteNonNumeric_withNonNumberObject() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);

        String value = "abc123";
        Object obj = "abc123";
        invokePrintAndQuote(obj, value, 0, value.length());

        // Non-number object, should quote
        verify(out).append('\"');
        verify(out).append(value, 0, value.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuoteNone_callsPrintAndEscape() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NONE);

        String value = "abc,def";
        Object obj = "abc,def";

        // Spy on printer to verify private printAndEscape call
        CSVPrinter spyPrinter = spy(printer);

        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        // Invoke printAndQuote on spy
        printAndQuoteMethod.invoke(spyPrinter, obj, value, 0, value.length());

        verify(spyPrinter).printAndEscape(value, 0, value.length());
        verify(out, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuoteMinimal_emptyTokenNewRecord() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        // Set newRecord = true via reflection
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);

        String value = "";
        invokePrintAndQuote("", value, 0, 0);

        // Should quote empty token at new record start
        verify(out).append('\"');
        verify(out).append(value, 0, 0);
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuoteMinimal_newRecordStartsWithSpecialChar() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);

        // Start with a char less than '0' (e.g. ' ')
        String value = " abc";
        invokePrintAndQuote(value, value, 0, value.length());

        verify(out).append('\"');
        verify(out).append(value, 0, value.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuoteMinimal_containsLF_CR_quoteChar_delimChar() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        char delimChar = ',';
        char quoteChar = '\"';

        when(format.getDelimiter()).thenReturn(delimChar);
        when(format.getQuoteChar()).thenReturn(quoteChar);

        // Value contains LF
        String valueLF = "abc\nxyz";
        invokePrintAndQuote(valueLF, valueLF, 0, valueLF.length());
        verify(out).append(quoteChar);

        // Value contains CR
        reset(out);
        String valueCR = "abc\rxyz";
        invokePrintAndQuote(valueCR, valueCR, 0, valueCR.length());
        verify(out).append(quoteChar);

        // Value contains quoteChar
        reset(out);
        String valueQuote = "abc\"xyz";
        invokePrintAndQuote(valueQuote, valueQuote, 0, valueQuote.length());
        verify(out).append(quoteChar);

        // Value contains delimChar
        reset(out);
        String valueDelim = "abc,xyz";
        invokePrintAndQuote(valueDelim, valueDelim, 0, valueDelim.length());
        verify(out).append(quoteChar);
    }

    @Test
    @Timeout(8000)
    void testQuoteMinimal_endsWithSpaceChar() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        // Ends with space (SP = 32)
        String value = "abc ";
        invokePrintAndQuote(value, value, 0, value.length());

        verify(out).append('\"');
        verify(out).append(value, 0, value.length());
        verify(out).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuoteMinimal_noQuoteNeeded() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        String value = "abcXYZ123";
        invokePrintAndQuote(value, value, 0, value.length());

        // Should append raw without quotes
        verify(out).append(value, 0, value.length());
        verify(out, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuoteMinimal_quotesInsideValue() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        String value = "a\"b\"c";
        invokePrintAndQuote(value, value, 0, value.length());

        ArgumentCaptor<CharSequence> captor = ArgumentCaptor.forClass(CharSequence.class);
        verify(out, atLeast(3)).append(captor.capture());

        // Should start with quoteChar
        assertEquals('\"', captor.getAllValues().get(0).charAt(0));
        // Should end with quoteChar
        assertEquals('\"', captor.getAllValues().get(captor.getAllValues().size() - 1).charAt(0));
        // Should contain doubled quotes inside
        boolean doubledQuoteFound = captor.getAllValues().stream()
                .anyMatch(s -> s.toString().contains("\"\""));
        assertTrue(doubledQuoteFound);
    }

    @Test
    @Timeout(8000)
    void testDefaultQuotePolicyIsMinimal() throws Throwable {
        // format.getQuotePolicy returns null
        when(format.getQuotePolicy()).thenReturn(null);

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        String value = "abc";
        invokePrintAndQuote(value, value, 0, value.length());

        // Should append raw without quotes (minimal policy, no special chars)
        verify(out).append(value, 0, value.length());
    }

    @Test
    @Timeout(8000)
    void testUnexpectedQuotePolicy_throws() throws Throwable {
        // Create a dummy Quote enum constant by reflection to simulate unexpected value
        Quote unexpectedQuote = mock(Quote.class);
        when(format.getQuotePolicy()).thenReturn(unexpectedQuote);

        String value = "abc";

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> invokePrintAndQuote(value, value, 0, value.length()));
        assertTrue(ex.getMessage().contains("Unexpected Quote value"));
    }
}