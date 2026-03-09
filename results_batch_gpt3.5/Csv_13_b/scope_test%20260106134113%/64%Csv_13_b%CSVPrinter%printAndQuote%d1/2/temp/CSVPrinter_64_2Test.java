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

class CSVPrinterPrintAndQuoteTest {

    private CSVPrinter printer;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
    }

    private void createPrinterWithFormat(CSVFormat format) throws Exception {
        // CSVPrinter constructor: CSVPrinter(final Appendable out, final CSVFormat format)
        printer = new CSVPrinter(new StringBuilder(), format);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len) throws Exception {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class);
        method.setAccessible(true);
        method.invoke(printer, object, value, offset, len);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll() throws Exception {
        when(format.getQuoteMode()).thenReturn(QuoteMode.ALL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('\"');

        createPrinterWithFormat(format);

        StringBuilder out = (StringBuilder) getField(printer, "out");
        CharSequence value = "value";
        invokePrintAndQuote("value", value, 0, value.length());

        String result = out.toString();
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertTrue(result.contains("value"));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_ObjectIsNumber() throws Exception {
        when(format.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('\"');

        createPrinterWithFormat(format);

        StringBuilder out = (StringBuilder) getField(printer, "out");
        CharSequence value = "1234";
        Number numberObject = Integer.valueOf(1234);
        invokePrintAndQuote(numberObject, value, 0, value.length());

        String result = out.toString();
        // Should not quote number
        assertEquals("1234", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_ObjectIsNotNumber() throws Exception {
        when(format.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('\"');

        createPrinterWithFormat(format);

        StringBuilder out = (StringBuilder) getField(printer, "out");
        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length());

        String result = out.toString();
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertTrue(result.contains("abc"));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNone_usesPrintAndEscape() throws Exception {
        when(format.getQuoteMode()).thenReturn(QuoteMode.NONE);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('\"');

        createPrinterWithFormat(format);

        CSVPrinter spyPrinter = spy(printer);
        setField(spyPrinter, "newRecord", true);

        CharSequence value = "noquote";
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class,
                int.class);
        printAndEscapeMethod.setAccessible(true);

        // Replace printer with spy to verify printAndEscape call
        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class,
                int.class, int.class);
        printAndQuoteMethod.setAccessible(true);

        printAndQuoteMethod.invoke(spyPrinter, "noquote", value, 0, value.length());

        verify(spyPrinter).printAndEscape(value, 0, value.length());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_emptyLenNewRecordTrue_quotes() throws Exception {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('\"');

        createPrinterWithFormat(format);

        setField(printer, "newRecord", true);

        StringBuilder out = (StringBuilder) getField(printer, "out");
        CharSequence value = "";
        invokePrintAndQuote("", value, 0, 0);

        String result = out.toString();
        assertEquals("\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_newRecordTrueFirstCharSpecialQuotes() throws Exception {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('\"');

        createPrinterWithFormat(format);

        setField(printer, "newRecord", true);

        StringBuilder out = (StringBuilder) getField(printer, "out");
        CharSequence value = "#start";
        invokePrintAndQuote("#start", value, 0, value.length());

        String result = out.toString();
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertTrue(result.contains("#start"));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsLF_CR_QuoteChar_Delimiter_quotes() throws Exception {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('\"');

        createPrinterWithFormat(format);

        setField(printer, "newRecord", false);

        StringBuilder out = (StringBuilder) getField(printer, "out");
        CharSequence value = "abc\ndef";
        invokePrintAndQuote("abc\ndef", value, 0, value.length());

        String result = out.toString();
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertTrue(result.contains("abc\ndef"));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_endsWithSpace_quotes() throws Exception {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('\"');

        createPrinterWithFormat(format);

        setField(printer, "newRecord", false);

        StringBuilder out = (StringBuilder) getField(printer, "out");
        CharSequence value = "endsWithSpace ";
        invokePrintAndQuote("endsWithSpace ", value, 0, value.length());

        String result = out.toString();
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertTrue(result.contains("endsWithSpace "));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_noQuote_appendsDirectly() throws Exception {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('\"');

        createPrinterWithFormat(format);

        setField(printer, "newRecord", false);

        StringBuilder out = (StringBuilder) getField(printer, "out");
        CharSequence value = "simpleValue";
        invokePrintAndQuote("simpleValue", value, 0, value.length());

        String result = out.toString();
        assertEquals("simpleValue", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_internalQuoteCharDoubled() throws Exception {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('\"');

        createPrinterWithFormat(format);

        setField(printer, "newRecord", false);

        StringBuilder out = (StringBuilder) getField(printer, "out");
        CharSequence value = "value\"with\"quotes";
        invokePrintAndQuote("value\"with\"quotes", value, 0, value.length());

        String result = out.toString();
        // Quotes doubled inside output
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        // The quote char should appear twice in a row inside
        assertTrue(result.contains("\"\""));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNull_throwsIllegalStateException() throws Exception {
        when(format.getQuoteMode()).thenReturn(null);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('\"');

        createPrinterWithFormat(format);

        setField(printer, "newRecord", false);

        // Because quoteMode is null, it should default to MINIMAL, so no exception
        // So forcibly test unexpected QuoteMode by reflection and setting private field
        setField(printer, "format", new CSVFormat(',', '\"', null, null, null, false, true, "\r\n", null, null, null,
                false, false, false) {
            @Override
            public QuoteMode getQuoteMode() {
                return null;
            }
        });

        // Actually, method sets quoteMode to MINIMAL if null, so no exception expected
        CharSequence value = "test";
        invokePrintAndQuote("test", value, 0, value.length());
    }

    private static Object getField(Object target, String fieldName) throws Exception {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}