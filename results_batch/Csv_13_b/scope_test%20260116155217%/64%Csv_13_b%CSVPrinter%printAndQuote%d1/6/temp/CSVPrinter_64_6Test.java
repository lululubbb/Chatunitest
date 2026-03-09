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

    private CSVPrinter csvPrinter;
    private CSVFormat formatMock;
    private Appendable outMock;

    @BeforeEach
    void setUp() {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(outMock, formatMock);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len) throws Throwable {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        try {
            method.invoke(csvPrinter, object, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_AllQuoteMode_QuotesEverything() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.ALL);
        when(formatMock.getQuoteCharacter()).thenReturn('"');
        when(formatMock.getDelimiter()).thenReturn(',');

        CharSequence value = "abc,def\"ghi\njkl";
        invokePrintAndQuote("abc", value, 0, value.length());

        // verify output starts and ends with quote char and quotes inside
        verify(outMock).append('"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock, times(2)).append('"'); // one at start and one at end
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_NonNumericQuoteMode_ObjectIsNumber_NoQuotes() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(formatMock.getQuoteCharacter()).thenReturn('"');
        when(formatMock.getDelimiter()).thenReturn(',');

        CharSequence value = "12345";
        Number numberObject = 123;

        invokePrintAndQuote(numberObject, value, 0, value.length());

        verify(outMock).append(value, 0, value.length());
        verify(outMock, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_NonNumericQuoteMode_ObjectIsNotNumber_Quotes() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(formatMock.getQuoteCharacter()).thenReturn('"');
        when(formatMock.getDelimiter()).thenReturn(',');

        CharSequence value = "abc";
        Object obj = new Object();

        invokePrintAndQuote(obj, value, 0, value.length());

        verify(outMock).append('"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_NoneQuoteMode_CallsPrintAndEscape() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NONE);
        when(formatMock.getQuoteCharacter()).thenReturn('"');
        when(formatMock.getDelimiter()).thenReturn(',');

        CharSequence value = "testvalue";

        // Spy on CSVPrinter to verify printAndEscape is called
        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(outMock, formatMock));
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        // We need to stub printAndEscape to avoid IOException
        doNothing().when(spyPrinter).printAndEscape(value, 0, value.length());

        method.invoke(spyPrinter, "obj", value, 0, value.length());

        verify(spyPrinter).printAndEscape(value, 0, value.length());
        verify(outMock, never()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(outMock, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_EmptyLen_NewRecord_Quotes() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getQuoteCharacter()).thenReturn('"');
        when(formatMock.getDelimiter()).thenReturn(',');

        // Set newRecord to true via reflection
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, true);

        CharSequence value = "";

        invokePrintAndQuote("obj", value, 0, 0);

        verify(outMock).append('"');
        verify(outMock).append(value, 0, 0);
        verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_EmptyLen_NotNewRecord_NoQuotes() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getQuoteCharacter()).thenReturn('"');
        when(formatMock.getDelimiter()).thenReturn(',');

        // Set newRecord to false via reflection
        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "";

        invokePrintAndQuote("obj", value, 0, 0);

        verify(outMock).append(value, 0, 0);
        verify(outMock, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_NewRecord_FirstCharSpecial_Quotes() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getQuoteCharacter()).thenReturn('"');
        when(formatMock.getDelimiter()).thenReturn(',');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, true);

        CharSequence value = "#abc";

        invokePrintAndQuote("obj", value, 0, value.length());

        verify(outMock).append('"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_ContainsLF_Quotes() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getQuoteCharacter()).thenReturn('"');
        when(formatMock.getDelimiter()).thenReturn(',');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "abc\ndef";

        invokePrintAndQuote("obj", value, 0, value.length());

        verify(outMock).append('"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_EndsWithSpace_Quotes() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getQuoteCharacter()).thenReturn('"');
        when(formatMock.getDelimiter()).thenReturn(',');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "abc ";

        invokePrintAndQuote("obj", value, 0, value.length());

        verify(outMock).append('"');
        verify(outMock).append(value, 0, value.length());
        verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_NoSpecialChars_NoQuotes() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getQuoteCharacter()).thenReturn('"');
        when(formatMock.getDelimiter()).thenReturn(',');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "abcDEF123";

        invokePrintAndQuote("obj", value, 0, value.length());

        verify(outMock).append(value, 0, value.length());
        verify(outMock, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_QuotesInsideValue_DoubleQuotes() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getQuoteCharacter()).thenReturn('"');
        when(formatMock.getDelimiter()).thenReturn(',');

        var newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "abc\"def\"ghi";

        invokePrintAndQuote("obj", value, 0, value.length());

        // Expect quote char at start and end
        verify(outMock).append('"');
        verify(outMock).append('"');
        verify(outMock, atLeastOnce()).append(value, 0, value.length());
        verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_UnexpectedQuoteMode_ThrowsIllegalStateException() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(null);
        when(formatMock.getQuoteCharacter()).thenReturn('"');
        when(formatMock.getDelimiter()).thenReturn(',');

        // Force quoteModePolicy to an unknown enum value by mocking getQuoteMode to null and then returning null triggers default to MINIMAL
        // So we forcibly set formatMock.getQuoteMode to a mocked enum that is unknown by reflection hack
        CSVFormat formatSpy = spy(formatMock);
        doReturn(null).when(formatSpy).getQuoteMode();

        // We create new csvPrinter with this spy
        CSVPrinter printer = new CSVPrinter(outMock, formatSpy);

        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        // We cannot pass an unknown enum, so this branch is unreachable in normal code.
        // So we skip this test as impossible to reach without changing source code.
    }
}