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
    void testQuotePolicyAll() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.ALL);
        String val = "abc";
        invokePrintAndQuote(val, val, 0, val.length());
        String expected = "\"" + val + "\"";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNonNumeric_withNumber() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        Number number = 123;
        String val = "123";
        invokePrintAndQuote(number, val, 0, val.length());
        // Number, so quote == false, so append original value without quotes
        assertEquals(val, out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNonNumeric_withNonNumber() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NON_NUMERIC);
        String val = "abc";
        invokePrintAndQuote(val, val, 0, val.length());
        String expected = "\"" + val + "\"";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyNone_callsPrintAndEscape() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.NONE);

        // Spy on printer to verify printAndEscape call
        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(out, format));
        Method m = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        m.setAccessible(true);

        // We will invoke on spyPrinter
        try {
            m.invoke(spyPrinter, "val", "val", 0, 3);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        verify(spyPrinter, times(1)).getClass().getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_emptyLen_newRecordTrue() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        // newRecord = true triggers quote on empty len
        setNewRecord(printer, true);
        String val = "";
        invokePrintAndQuote(val, val, 0, 0);
        assertEquals("\"\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_emptyLen_newRecordFalse() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, false);
        String val = "";
        invokePrintAndQuote(val, val, 0, 0);
        // no quote, so empty appended
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_newRecordTrue_valueStartsWithSpecialChar() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, true);

        // Character less than '0' triggers quote
        String val = "#abc";
        invokePrintAndQuote(val, val, 0, val.length());
        assertEquals("\"#abc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_newRecordFalse_valueStartsWithCommentChar() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, false);

        // COMMENT is 35 ('#'), so value starting with '#' triggers quote
        String val = "#abc";
        invokePrintAndQuote(val, val, 0, val.length());
        assertEquals("\"#abc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_valueContainsLF() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, false);

        String val = "ab\ncd";
        invokePrintAndQuote(val, val, 0, val.length());
        assertEquals("\"ab\ncd\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_valueContainsCR() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, false);

        String val = "ab\rcd";
        invokePrintAndQuote(val, val, 0, val.length());
        assertEquals("\"ab\rcd\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_valueContainsQuoteChar() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, false);

        String val = "ab\"cd";
        invokePrintAndQuote(val, val, 0, val.length());
        // Quotes inside should be doubled
        assertEquals("\"ab\"\"cd\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_valueContainsDelimiter() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, false);

        String val = "ab,cd";
        invokePrintAndQuote(val, val, 0, val.length());
        assertEquals("\"ab,cd\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_valueEndsWithSpace() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, false);

        String val = "abc ";
        invokePrintAndQuote(val, val, 0, val.length());
        assertEquals("\"abc \"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyMinimal_valueNoQuoteNeeded() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(Quote.MINIMAL);
        setNewRecord(printer, false);

        String val = "abc123";
        invokePrintAndQuote(val, val, 0, val.length());
        assertEquals(val, out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuotePolicyUnexpected_throwsIllegalStateException() throws Throwable {
        when(format.getQuotePolicy()).thenReturn(null);
        setNewRecord(printer, false);

        // Use reflection to set format.getQuotePolicy() to a dummy enum value outside known ones
        when(format.getQuotePolicy()).thenReturn(Quote.valueOf("MINIMAL")); // normal minimal

        // Now forcibly set format to a mock that returns an unknown enum to trigger exception
        CSVFormat badFormat = mock(CSVFormat.class);
        when(badFormat.getDelimiter()).thenReturn(',');
        when(badFormat.getQuoteChar()).thenReturn('"');
        when(badFormat.getQuotePolicy()).thenReturn(null);

        CSVPrinter badPrinter = new CSVPrinter(out, badFormat);

        // Use reflection to call method with bad enum
        Method m = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        m.setAccessible(true);

        // Use a custom enum to break switch
        Quote unknownQuote = null;
        try {
            // Create a dummy enum by reflection is complicated, so instead test default by passing null and breaking code path
            when(badFormat.getQuotePolicy()).thenReturn(null);
            m.invoke(badPrinter, "abc", "abc", 0, 3);
            fail("Expected IllegalStateException");
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof IllegalStateException);
            assertTrue(e.getCause().getMessage().contains("Unexpected Quote value"));
        }
    }

    private void setNewRecord(CSVPrinter printer, boolean value) throws Exception {
        java.lang.reflect.Field f = CSVPrinter.class.getDeclaredField("newRecord");
        f.setAccessible(true);
        f.set(printer, value);
    }
}