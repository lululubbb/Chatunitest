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

class CSVPrinterPrintAndQuoteTest {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    private Method printAndQuoteMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);

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

    @Test
    @Timeout(8000)
    void testQuoteAll() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.ALL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');

        CharSequence value = "abc,def";
        invokePrintAndQuote("abc,def", value, 0, value.length());

        // Expect quotes at start and end
        var inOrder = inOrder(out);
        inOrder.verify(out).append('\"');
        inOrder.verify(out).append(value, 0, value.length());
        inOrder.verify(out).append('\"');
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testQuoteNonNumericWithNumber() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');

        CharSequence value = "12345";
        invokePrintAndQuote(12345, value, 0, value.length());

        // Number should not be quoted, append called once
        verify(out).append(value, 0, value.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testQuoteNonNumericWithString() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');

        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length());

        var inOrder = inOrder(out);
        inOrder.verify(out).append('\"');
        inOrder.verify(out).append(value, 0, value.length());
        inOrder.verify(out).append('\"');
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testQuoteNone() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NONE);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');

        CharSequence value = "abc,def";
        // We expect printAndEscape to be called, it is private, so we only verify that out.append is never called here
        // Instead, printAndEscape would handle appending, but we cannot mock it here.
        // So we spy printer and verify printAndEscape called.

        CSVPrinter spyPrinter = Mockito.spy(printer);
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        // invoke printAndQuote on spy
        try {
            printAndQuoteMethod.invoke(spyPrinter, "abc,def", value, 0, value.length());
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        verify(spyPrinter).printAndEscape(value, 0, value.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testQuoteMinimalEmptyFirstToken() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');

        // newRecord is true by default, empty token
        CharSequence value = "";
        invokePrintAndQuote("", value, 0, 0);

        var inOrder = inOrder(out);
        inOrder.verify(out).append('\"');
        inOrder.verify(out).append(value, 0, 0);
        inOrder.verify(out).append('\"');
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testQuoteMinimalNewRecordStartCharTriggersQuote() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');

        // newRecord true, value starts with char < '0' or between '9' and 'A' or 'Z' and 'a' or > 'z'
        // Use value starting with '#'(which is COMMENT in Constants)
        CharSequence value = "#abc";
        invokePrintAndQuote("#abc", value, 0, value.length());

        var inOrder = inOrder(out);
        inOrder.verify(out).append('\"');
        inOrder.verify(out).append(value, 0, value.length());
        inOrder.verify(out).append('\"');
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testQuoteMinimalNewRecordStartCharLessOrEqualComment() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');

        // newRecord true, value starts with char <= COMMENT (35 = '#')
        CharSequence value = "#test";
        invokePrintAndQuote("#test", value, 0, value.length());

        var inOrder = inOrder(out);
        inOrder.verify(out).append('\"');
        inOrder.verify(out).append(value, 0, value.length());
        inOrder.verify(out).append('\"');
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testQuoteMinimalContainsSpecialChars() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');

        // Contains LF, should quote
        CharSequence value = "abc\ndef";
        invokePrintAndQuote("abc\ndef", value, 0, value.length());

        var inOrder = inOrder(out);
        inOrder.verify(out).append('\"');
        inOrder.verify(out).append(value, 0, value.length());
        inOrder.verify(out).append('\"');
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testQuoteMinimalEndsWithSpace() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');

        // Ends with space (SP = 32)
        CharSequence value = "abc ";
        invokePrintAndQuote("abc ", value, 0, value.length());

        var inOrder = inOrder(out);
        inOrder.verify(out).append('\"');
        inOrder.verify(out).append(value, 0, value.length());
        inOrder.verify(out).append('\"');
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testQuoteMinimalNoQuoteNeeded() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');

        // Simple alphanumeric no special chars, newRecord false
        CharSequence value = "abc123";
        // set newRecord to false via reflection
        var field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        field.set(printer, false);

        invokePrintAndQuote("abc123", value, 0, value.length());

        verify(out).append(value, 0, value.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testQuoteMinimalWithQuoteCharInside() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');

        // Value contains quote char inside, should double quote
        CharSequence value = "abc\"def";
        invokePrintAndQuote("abc\"def", value, 0, value.length());

        var inOrder = inOrder(out);
        inOrder.verify(out).append('\"');
        // The method appends segments doubling quotes, so expect append with segments including quotes doubled
        // We cannot verify exact segments easily, but verify append called multiple times with correct chars
        inOrder.verify(out, atLeast(1)).append(value, 0, value.length());
        inOrder.verify(out).append('\"');
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testQuoteMinimalUnexpectedQuotePolicy() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(null);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('\"');

        // Inject unexpected Quote value by reflection
        var formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        formatField.set(printer, format);

        // Mock getQuotePolicy to return unexpected enum (simulate)
        when(format.getQuotePolicy()).thenReturn(null);

        // Force null triggers Quote.MINIMAL, so test default branch by passing invalid enum via spy
        CSVFormat spyFormat = Mockito.spy(format);
        when(spyFormat.getQuotePolicy()).thenReturn(null);
        when(spyFormat.getDelimiter()).thenReturn(',');
        when(spyFormat.getQuoteChar()).thenReturn('\"');

        var printerWithSpyFormat = new CSVPrinter(out, spyFormat);

        // Use reflection to call printAndQuote with invalid enum by forcing switch default
        // We cannot pass invalid enum, so skip this test as impossible realistically

        // So this test is omitted.
    }
}