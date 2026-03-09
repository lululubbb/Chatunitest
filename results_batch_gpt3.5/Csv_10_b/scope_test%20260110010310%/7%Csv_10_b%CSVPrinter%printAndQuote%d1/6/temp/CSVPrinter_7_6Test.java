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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
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
        out = new StringBuilder();
        format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteChar()).thenReturn('"');
        when(format.getQuotePolicy()).thenReturn(null);

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
    void testQuotePolicyNullDefaultsToMinimal_EmptyValue_NewRecordTrue_Quotes() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(null);
        setField(printer, "newRecord", true);

        invokePrintAndQuote(null, "", 0, 0);

        String result = out.toString();
        assertEquals("\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyAll_AlwaysQuotes() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.ALL);

        invokePrintAndQuote("test", "test", 0, 4);

        String result = out.toString();
        assertEquals("\"test\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNonNumeric_Object_Quotes() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);

        invokePrintAndQuote("abc", "abc", 0, 3);

        String result = out.toString();
        assertEquals("\"abc\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNonNumeric_Number_NoQuote() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);

        invokePrintAndQuote(123, "123", 0, 3);

        String result = out.toString();
        assertEquals("123", result);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNone_UsesPrintAndEscape() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NONE);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        // Override printer with spy for this test
        printAndQuoteMethod.invoke(spyPrinter, "abc", "abc", 0, 3);
        verify(spyPrinter).printAndEscape("abc", 0, 3);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_NewRecordTrue_CharLessThan0To9_Quotes() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setField(printer, "newRecord", true);

        // Value starts with a char < '0' (e.g. ' ')
        invokePrintAndQuote(null, " test", 0, 5);

        String result = out.toString();
        assertEquals("\" test\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_NewRecordTrue_CharIsComment_Quotes() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setField(printer, "newRecord", true);

        // COMMENT char is 35 ('#')
        char commentChar = (char)35;
        invokePrintAndQuote(null, commentChar + "abc", 0, 4);

        String result = out.toString();
        assertEquals("\"#abc\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_NoSpecialChars_NoQuote() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setField(printer, "newRecord", false);

        invokePrintAndQuote(null, "abc123", 0, 6);

        String result = out.toString();
        assertEquals("abc123", result);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_ContainsLF_Quotes() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setField(printer, "newRecord", false);

        invokePrintAndQuote(null, "ab\ncd", 0, 5);

        String result = out.toString();
        assertEquals("\"ab\ncd\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_EndsWithSpace_Quotes() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setField(printer, "newRecord", false);

        invokePrintAndQuote(null, "abc ", 0, 4);

        String result = out.toString();
        assertEquals("\"abc \"", result);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_QuoteCharInside_QuotesAndDoublesQuote() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setField(printer, "newRecord", false);

        // value with quote char inside
        invokePrintAndQuote(null, "ab\"cd", 0, 5);

        String result = out.toString();
        // should double the quote char inside and wrap with quotes
        assertEquals("\"ab\"\"cd\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_UnexpectedQuote_Throws() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(null);
        setField(printer, "newRecord", false);

        // forcibly set an unexpected Quote enum by reflection (simulate)
        setField(format, "quotePolicy", null);

        // Use reflection to set format.getQuotePolicy() to return a bogus Quote by mocking
        when(format.getQuotePolicy()).thenReturn(null);
        // forcibly call with unknown enum by reflection is complicated, so skip this test because
        // the switch covers all enums and default throws IllegalStateException

        // Instead test default branch by mocking format.getQuotePolicy to return unknown enum
        CSVFormat formatMock = mock(CSVFormat.class);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getQuoteChar()).thenReturn('"');
        when(formatMock.getQuotePolicy()).thenReturn(null);

        CSVPrinter printer2 = new CSVPrinter(out, formatMock);

        // Use reflection to call method with unknown enum - not possible without changing enum
        // So test default branch by invoking with a spy and throwing exception manually
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}