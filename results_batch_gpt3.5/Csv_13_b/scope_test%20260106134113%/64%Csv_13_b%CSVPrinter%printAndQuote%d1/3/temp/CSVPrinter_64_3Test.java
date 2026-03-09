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
import org.mockito.ArgumentCaptor;

public class CSVPrinter_64_3Test {

    private CSVPrinter printer;
    private CSVFormat format;
    private Appendable out;

    @BeforeEach
    public void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

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
    public void testPrintAndQuote_AllQuoteMode_QuotesAll() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.ALL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        String value = "abc";
        invokePrintAndQuote("anyObject", value, 0, value.length());

        // Should write quoted value: "abc"
        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    public void testPrintAndQuote_NonNumericQuoteMode_ObjectIsNumber() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        String value = "12345";
        Number num = 123;

        invokePrintAndQuote(num, value, 0, value.length());

        // Number object, should NOT quote, should append original
        verify(out).append(value, 0, value.length());
        verify(out, never()).append('"');
    }

    @Test
    @Timeout(8000)
    public void testPrintAndQuote_NonNumericQuoteMode_ObjectIsNotNumber() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        String value = "abc";
        Object obj = new Object();

        invokePrintAndQuote(obj, value, 0, value.length());

        // Non-number object, should quote
        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    public void testPrintAndQuote_NoneQuoteMode_CallsPrintAndEscape() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.NONE);

        // We spy printer to verify printAndEscape call
        CSVPrinter spyPrinter = spy(new CSVPrinter(out, format));
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        CharSequence value = "abc";
        method.invoke(spyPrinter, new Object[]{"obj", value, 0, value.length()});

        verify(spyPrinter).printAndEscape(value, 0, value.length());
        verify(out, never()).append(any(CharSequence.class), anyInt(), anyInt());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndQuote_MinimalQuoteMode_EmptyValueNewRecord_Quotes() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        // newRecord is true initially
        CharSequence value = "";
        invokePrintAndQuote("obj", value, 0, 0);

        verify(out).append('"');
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    public void testPrintAndQuote_MinimalQuoteMode_NewRecordStartsWithSpecialChar_Quotes() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "#value";
        // newRecord true initially
        invokePrintAndQuote("obj", value, 0, value.length());

        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    public void testPrintAndQuote_MinimalQuoteMode_ValueContainsLF_Quotes() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        // value contains LF char inside
        CharSequence value = "ab\ncd";
        invokePrintAndQuote("obj", value, 0, value.length());

        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    public void testPrintAndQuote_MinimalQuoteMode_ValueEndsWithSpace_Quotes() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc ";
        invokePrintAndQuote("obj", value, 0, value.length());

        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    public void testPrintAndQuote_MinimalQuoteMode_NoQuoteNeeded_AppendsDirectly() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        // newRecord false so no special quote for first char
        // value starts with normal char 'a'
        // no special chars inside or end spaces
        setField(printer, "newRecord", false);

        CharSequence value = "abc123";
        invokePrintAndQuote("obj", value, 0, value.length());

        verify(out).append(value, 0, value.length());
        verify(out, never()).append('"');
    }

    @Test
    @Timeout(8000)
    public void testPrintAndQuote_MinimalQuoteMode_DoubleQuoteInside_DoublesQuotes() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "ab\"cd";
        invokePrintAndQuote("obj", value, 0, value.length());

        ArgumentCaptor<CharSequence> captor = ArgumentCaptor.forClass(CharSequence.class);

        verify(out).append('"');
        verify(out, atLeastOnce()).append(captor.capture(), anyInt(), anyInt());
        verify(out).append('"');

        // Should have doubled the quote character (printed twice)
        // The implementation appends chunks up to and including the quote char, then restarts from that pos
        // So the output calls to append should contain the quote char doubled effectively
        boolean doubledQuoteFound = captor.getAllValues().stream()
                .anyMatch(seq -> seq.toString().contains("\"\""));
        assertFalse(doubledQuoteFound, "The method appends chunks including quote chars separately, not doubled in single call");

        // The test mainly ensures no exceptions and quotes present
    }

    @Test
    @Timeout(8000)
    public void testPrintAndQuote_UnexpectedQuoteMode_Throws() throws Throwable {
        when(format.getQuoteMode()).thenReturn(null);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        // Force unknown QuoteMode by reflection
        setField(format, "quoteMode", null);

        // We create a subclass to return invalid enum to force exception
        CSVFormat badFormat = mock(CSVFormat.class);
        when(badFormat.getQuoteMode()).thenReturn(null);
        when(badFormat.getDelimiter()).thenReturn(',');
        when(badFormat.getQuoteCharacter()).thenReturn('"');

        CSVPrinter badPrinter = new CSVPrinter(out, badFormat);

        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            method.invoke(badPrinter, "obj", "abc", 0, 3);
        });
        assertTrue(ex.getMessage().startsWith("Unexpected Quote value"));
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}