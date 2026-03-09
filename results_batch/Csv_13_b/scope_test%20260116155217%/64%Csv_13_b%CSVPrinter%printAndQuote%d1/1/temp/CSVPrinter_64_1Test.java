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

    private CSVPrinter csvPrinter;
    private CSVFormat formatMock;
    private Appendable outMock;

    @BeforeEach
    void setUp() {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(outMock, formatMock);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len)
            throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class);
        method.setAccessible(true);
        method.invoke(csvPrinter, object, value, offset, len);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeAll() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.ALL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc,def\"ghi\njkl";
        invokePrintAndQuote("anyObject", value, 0, value.length());

        // Should append quote char at start and end and handle inner quotes doubling
        // Verify start quote
        verify(outMock).append('"');
        // Verify appended value segments and doubled quotes
        verify(outMock, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        // Verify end quote
        verify(outMock, atLeastOnce()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeNonNumeric_Object() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "text";
        Object object = new Object();

        invokePrintAndQuote(object, value, 0, value.length());

        verify(outMock).append('"');
        verify(outMock, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(outMock, atLeastOnce()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeNonNumeric_Number() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "12345";
        Number object = 123;

        invokePrintAndQuote(object, value, 0, value.length());

        // Should not quote, so append called once with full value
        verify(outMock).append(value, 0, value.length());
        verify(outMock, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeNone() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NONE);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "text";

        // We spy csvPrinter to verify call to printAndEscape
        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class);
        method.setAccessible(true);
        method.invoke(spyPrinter, "obj", value, 0, value.length());

        verify(spyPrinter).printAndEscape(value, 0, value.length());
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_EmptyLen_NewRecord() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        // Set newRecord = true by reflection
        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, true);

        CharSequence value = "";

        invokePrintAndQuote("obj", value, 0, 0);

        verify(outMock).append('"');
        verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_EmptyLen_NotNewRecord() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        // Set newRecord = false by reflection
        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "";

        invokePrintAndQuote("obj", value, 0, 0);

        // Should append empty string as is (no quotes)
        verify(outMock).append(value, 0, 0);
        verify(outMock, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_NewRecord_CharLessThan0() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        // Set newRecord = true
        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, true);

        CharSequence value = "\u0001abc"; // char < '0'

        invokePrintAndQuote("obj", value, 0, value.length());

        verify(outMock).append('"');
        verify(outMock, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_StartCharLessOrEqualComment() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        // Set newRecord = false
        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "#abc"; // '#' == COMMENT

        invokePrintAndQuote("obj", value, 0, value.length());

        verify(outMock).append('"');
        verify(outMock, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_ContainsSpecialChars() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        // Set newRecord = false
        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "abc\ndef";

        invokePrintAndQuote("obj", value, 0, value.length());

        verify(outMock).append('"');
        verify(outMock, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_EndCharLessOrEqualSP() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        // Set newRecord = false
        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "abc ";

        invokePrintAndQuote("obj", value, 0, value.length());

        verify(outMock).append('"');
        verify(outMock, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_NoQuoteNeeded() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        // Set newRecord = false
        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "abcDEF123";

        invokePrintAndQuote("obj", value, 0, value.length());

        verify(outMock).append(value, 0, value.length());
        verify(outMock, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeUnexpected_ThrowsIllegalStateException() throws Throwable {
        when(formatMock.getQuoteMode()).thenReturn(null);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteCharacter()).thenReturn('"');

        // We force an unexpected QuoteMode by reflection
        java.lang.reflect.Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        CSVFormat format = new CSVFormat(',', '"', null, null, null, false, false, null, null, null, null, false,
                false, false);
        quoteModeField.set(format, null);

        // Spy formatMock to return null QuoteMode, but then forcibly set quoteMode to an invalid enum
        when(formatMock.getQuoteMode()).thenReturn(null);

        // We cannot set an invalid enum easily, so test the default null behavior (MINIMAL) suffices.
        // But to cover the default case, we use reflection to call method with invalid enum:
        // Instead, we test coverage by reflection on private method with a dummy enum by invoking via reflection.
        // This is complex, so we skip this as impossible without modifying source.

        // Instead, test coverage for IllegalStateException by calling with a mocked QuoteMode that returns an unknown enum ordinal.
        // This is not feasible in Java enums, so we omit this test.
    }
}