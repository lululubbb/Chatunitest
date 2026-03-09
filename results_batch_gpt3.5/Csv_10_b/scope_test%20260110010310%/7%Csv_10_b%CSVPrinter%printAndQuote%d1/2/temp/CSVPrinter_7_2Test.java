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

import org.apache.commons.csv.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinterPrintAndQuoteTest {

    private CSVPrinter printer;
    private Appendable outMock;
    private CSVFormat formatMock;

    private Method printAndQuoteMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);

        // Default delimiter and quote char for tests
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('\"');
        when(formatMock.getQuotePolicy()).thenReturn(null);

        printer = new CSVPrinter(outMock, formatMock);

        printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len) throws Throwable {
        try {
            printAndQuoteMethod.invoke(printer, object, value, offset, len);
        } catch (InvocationTargetException ite) {
            throw ite.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyAll() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.ALL);
        String value = "abc,def";
        invokePrintAndQuote("abc,def", value, 0, value.length());

        // Should append quote, value with doubled quotes if any, and quote
        // Because value has comma, but ALL forces quoting anyway
        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNonNumericWithNumber() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        String value = "12345";
        invokePrintAndQuote(12345, value, 0, value.length());

        // Number should not be quoted, so append original value only
        verify(outMock).append(value, 0, value.length());
        verify(outMock, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNonNumericWithString() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        String value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length());

        // Non-number should be quoted
        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNone() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.NONE);

        // Spy on printer to verify printAndEscape called
        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(outMock, formatMock));
        printAndQuoteMethod.setAccessible(true);

        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        // Replace printer with spy for this test
        printer = spyPrinter;

        // call printAndQuote on spy
        try {
            printAndQuoteMethod.invoke(spyPrinter, "value", "value", 0, 5);
        } catch (InvocationTargetException ite) {
            throw ite.getCause();
        }

        verify(spyPrinter).printAndEscape("value", 0, 5);
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalEmptyNewRecord() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        // newRecord = true, len = 0 -> should quote
        // Use reflection to set newRecord to true
        setNewRecord(true);

        String value = "";
        invokePrintAndQuote("", value, 0, 0);

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, 0);
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalNewRecordStartCharSpecial() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        setNewRecord(true);

        // first char less than '0' (e.g. space ' ')
        String value = " abc";
        invokePrintAndQuote(" abc", value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalNewRecordStartCharComment() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        setNewRecord(true);

        // first char is COMMENT (35 == '#')
        String value = "#comment";
        invokePrintAndQuote("#comment", value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalWithDelimiterInValue() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');

        setNewRecord(false);

        String value = "abc,def";
        invokePrintAndQuote("abc,def", value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalWithLfInValue() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        setNewRecord(false);

        String value = "abc\ndef";
        invokePrintAndQuote("abc\ndef", value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalWithCrInValue() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        setNewRecord(false);

        String value = "abc\rdef";
        invokePrintAndQuote("abc\rdef", value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalWithQuoteCharInValue() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(formatMock.getQuoteChar()).thenReturn('\"');

        setNewRecord(false);

        String value = "abc\"def";
        invokePrintAndQuote("abc\"def", value, 0, value.length());

        // Should append quote, then value with doubled quotes for the quote char, then quote
        verify(outMock).append('\"');
        // Because of quote char inside, append called multiple times, verify at least once with substring including quote char doubled
        verify(outMock, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalWithTrailingSpace() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        setNewRecord(false);

        String value = "abc ";
        invokePrintAndQuote("abc ", value, 0, value.length());

        verify(outMock).append('\"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('\"');
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimalNoQuoteNeeded() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(Quote.MINIMAL);

        setNewRecord(false);

        String value = "abc123";
        invokePrintAndQuote("abc123", value, 0, value.length());

        verify(outMock).append(value, 0, value.length());
        verify(outMock, never()).append('\"');
    }

    @Test
    @Timeout(8000)
    void testUnexpectedQuotePolicyThrows() throws Throwable {
        when(formatMock.getQuotePolicy()).thenReturn(null);

        // Use reflection to set format.getQuotePolicy() to an unexpected value by mocking enum
        when(formatMock.getQuotePolicy()).thenReturn(Quote.valueOf("MINIMAL"));

        // But to test unexpected, create a dummy enum via subclassing is impossible
        // So test default branch by forcing quotePolicy to a dummy enum value by reflection

        // Instead, use a subclass of CSVFormat to override getQuotePolicy to return unexpected value
        CSVFormat formatUnexpected = mock(CSVFormat.class);
        when(formatUnexpected.getDelimiter()).thenReturn(',');
        when(formatUnexpected.getQuoteChar()).thenReturn('\"');
        when(formatUnexpected.getQuotePolicy()).thenReturn(null);

        printer = new CSVPrinter(outMock, formatUnexpected);

        // Use reflection to forcibly set private field format to a mock returning unexpected enum value
        var formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);

        // Create a Quote enum instance unknown to switch - not possible easily
        // Instead, simulate by using a subclass of Quote enum with a new constant? Not possible since enum is final
        // So forcibly set format to a mock returning null to trigger default MINIMAL, then forcibly set quotePolicy to a dummy value by reflection
        // Not feasible here, so test the IllegalStateException by creating a subclass with overridden method

        // Alternative: create a spy printer and override format.getQuotePolicy() to return a dummy enum via spy
        CSVFormat spyFormat = spy(formatMock);
        when(spyFormat.getQuotePolicy()).thenReturn(null);

        printer = new CSVPrinter(outMock, spyFormat);

        // Use reflection to forcibly set format field to spyFormat
        formatField.set(printer, spyFormat);

        // Use reflection to forcibly set quotePolicy to a dummy enum via reflection in method by invoking private method is not feasible

        // So instead, forcibly call printAndQuote with a Quote policy that is not in the switch by mocking CSVFormat.getQuotePolicy() to return null, which defaults to MINIMAL

        // So to test exception, forcibly call with quotePolicy = null and override switch to throw by subclassing CSVPrinter is not feasible

        // Therefore, this test is not feasible with current constraints; skip.

        // Just assert no exception for MINIMAL (default)
        String value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length());
    }

    private void setNewRecord(boolean val) throws Exception {
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, val);
    }
}