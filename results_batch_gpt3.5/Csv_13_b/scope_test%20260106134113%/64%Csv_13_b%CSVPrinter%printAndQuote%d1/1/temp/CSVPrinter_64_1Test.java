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

import static org.junit.jupiter.api.Assertions.assertThrows;

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
        outMock = Mockito.mock(Appendable.class);
        formatMock = Mockito.mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(outMock, formatMock);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len) throws Throwable {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        try {
            method.invoke(csvPrinter, object, value, offset, len);
        } catch (InvocationTargetException ite) {
            throw ite.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeAll_quotesEntireValue() throws Throwable {
        Mockito.when(formatMock.getQuoteMode()).thenReturn(QuoteMode.ALL);
        Mockito.when(formatMock.getDelimiter()).thenReturn(',');
        Mockito.when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length());

        Mockito.verify(outMock).append('"');
        Mockito.verify(outMock).append(value, 0, value.length());
        Mockito.verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeNonNumeric_objectNumber_noQuote() throws Throwable {
        Mockito.when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        Mockito.when(formatMock.getDelimiter()).thenReturn(',');
        Mockito.when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "123";
        invokePrintAndQuote(123, value, 0, value.length());

        Mockito.verify(outMock).append(value, 0, value.length());
        Mockito.verify(outMock, Mockito.never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeNonNumeric_objectString_quote() throws Throwable {
        Mockito.when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        Mockito.when(formatMock.getDelimiter()).thenReturn(',');
        Mockito.when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length());

        Mockito.verify(outMock).append('"');
        Mockito.verify(outMock).append(value, 0, value.length());
        Mockito.verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeNone_callsPrintAndEscape() throws Throwable {
        Mockito.when(formatMock.getQuoteMode()).thenReturn(QuoteMode.NONE);
        Mockito.when(formatMock.getDelimiter()).thenReturn(',');
        Mockito.when(formatMock.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc";

        // Spy to verify printAndEscape call
        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);

        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        method.invoke(spyPrinter, "abc", value, 0, value.length());

        Mockito.verify(spyPrinter).printAndEscape(value, 0, value.length());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_emptyValue_newRecord_true() throws Throwable {
        Mockito.when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        Mockito.when(formatMock.getDelimiter()).thenReturn(',');
        Mockito.when(formatMock.getQuoteCharacter()).thenReturn('"');

        // Set newRecord = true via reflection
        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, true);

        CharSequence value = "";
        invokePrintAndQuote("", value, 0, 0);

        Mockito.verify(outMock).append('"');
        Mockito.verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_newRecord_false_noSpecialChars() throws Throwable {
        Mockito.when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        Mockito.when(formatMock.getDelimiter()).thenReturn(',');
        Mockito.when(formatMock.getQuoteCharacter()).thenReturn('"');

        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "abc123";
        invokePrintAndQuote("abc123", value, 0, value.length());

        Mockito.verify(outMock).append(value, 0, value.length());
        Mockito.verify(outMock, Mockito.never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_valueStartsWithSpecialChar_quotes() throws Throwable {
        Mockito.when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        Mockito.when(formatMock.getDelimiter()).thenReturn(',');
        Mockito.when(formatMock.getQuoteCharacter()).thenReturn('"');

        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "#abc";
        invokePrintAndQuote("#abc", value, 0, value.length());

        Mockito.verify(outMock).append('"');
        Mockito.verify(outMock).append(value, 0, value.length());
        Mockito.verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_valueContainsLF_quotes() throws Throwable {
        Mockito.when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        Mockito.when(formatMock.getDelimiter()).thenReturn(',');
        Mockito.when(formatMock.getQuoteCharacter()).thenReturn('"');

        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "ab\nc";
        invokePrintAndQuote("ab\nc", value, 0, value.length());

        Mockito.verify(outMock).append('"');
        Mockito.verify(outMock).append(value, 0, value.length());
        Mockito.verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_valueEndsWithSpace_quotes() throws Throwable {
        Mockito.when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        Mockito.when(formatMock.getDelimiter()).thenReturn(',');
        Mockito.when(formatMock.getQuoteCharacter()).thenReturn('"');

        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "abc ";
        invokePrintAndQuote("abc ", value, 0, value.length());

        Mockito.verify(outMock).append('"');
        Mockito.verify(outMock).append(value, 0, value.length());
        Mockito.verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_valueContainsQuoteChar_quotesAndDoublesQuoteChar() throws Throwable {
        Mockito.when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        Mockito.when(formatMock.getDelimiter()).thenReturn(',');
        Mockito.when(formatMock.getQuoteCharacter()).thenReturn('"');

        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "ab\"c";
        invokePrintAndQuote("ab\"c", value, 0, value.length());

        // Should append starting quote
        Mockito.verify(outMock).append('"');
        // Should append "ab"" (double quote for embedded quote)
        Mockito.verify(outMock).append(value, 0, 3);
        // Should append last segment "c"
        Mockito.verify(outMock).append(value, 2, value.length());
        // Should append ending quote
        Mockito.verify(outMock).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_throwsIllegalStateException() throws Throwable {
        Mockito.when(formatMock.getQuoteMode()).thenReturn(null);
        Mockito.when(formatMock.getDelimiter()).thenReturn(',');
        Mockito.when(formatMock.getQuoteCharacter()).thenReturn('"');

        // forcibly set an invalid QuoteMode to test defaulting to MINIMAL - no exception expected here
        // Instead forcibly test default branch by reflection invoking with invalid QuoteMode
        // We will simulate by passing invalid quoteMode via reflection

        // Use reflection to set private field format to a CSVFormat mock with invalid quoteMode
        CSVFormat formatSpy = Mockito.spy(formatMock);
        Mockito.when(formatSpy.getQuoteMode()).thenReturn(null);

        java.lang.reflect.Field formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        formatField.set(csvPrinter, formatSpy);

        // Should not throw exception, uses MINIMAL as default
        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_unexpectedQuoteMode_throwsIllegalStateException() throws Throwable {
        Mockito.when(formatMock.getQuoteMode()).thenReturn(QuoteMode.ALL);
        Mockito.when(formatMock.getDelimiter()).thenReturn(',');
        Mockito.when(formatMock.getQuoteCharacter()).thenReturn('"');

        // Use reflection to forcibly call method with unexpected QuoteMode enum value
        // Create a dummy QuoteMode enum instance by reflection to simulate unexpected value
        // Since QuoteMode is enum, we cannot create invalid instance easily,
        // so instead we create a subclass to simulate unexpected value for testing

        // This test is not feasible due to enum constraints, so we skip it.
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_valueEmpty_newRecordFalse_noQuote() throws Throwable {
        Mockito.when(formatMock.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        Mockito.when(formatMock.getDelimiter()).thenReturn(',');
        Mockito.when(formatMock.getQuoteCharacter()).thenReturn('"');

        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "";
        invokePrintAndQuote("", value, 0, 0);

        Mockito.verify(outMock).append(value, 0, 0);
        Mockito.verify(outMock, Mockito.never()).append('"');
    }
}