package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintAndQuoteTest {

    private CSVFormat csvFormat;
    private Appendable out;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        out = mock(Appendable.class);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len,
            Appendable out, boolean newRecord) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(csvFormat, object, value, offset, len, out, newRecord);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_AllQuoteModeAll() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL);
        CharSequence value = "abc";
        invokePrintAndQuote(null, value, 0, value.length(), out, false);
        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_AllQuoteModeAllNonNull_ObjectNonNull() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL_NON_NULL);
        CharSequence value = "abc";
        invokePrintAndQuote(new Object(), value, 0, value.length(), out, false);
        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_AllQuoteModeAllNonNull_ObjectNull() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL_NON_NULL);
        CharSequence value = "abc";
        invokePrintAndQuote(null, value, 0, value.length(), out, false);
        verify(out).append(value, 0, value.length());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_NonNumeric_ObjectNumber() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        CharSequence value = "123";
        invokePrintAndQuote(123, value, 0, value.length(), out, false);
        verify(out).append(value, 0, value.length());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_NonNumeric_ObjectNonNumber() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length(), out, false);
        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeNone_CallsPrintAndEscape() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NONE);
        CharSequence value = "abc";
        // Use spy to verify private printAndEscape call indirectly by throwing exception
        CSVFormat spyFormat = spy(csvFormat);
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        method.setAccessible(true);

        // We replace printAndEscape with a doThrow to detect invocation
        doThrow(new IOException("printAndEscape invoked")).when(spyFormat).printAndEscape(any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class));

        try {
            method.invoke(spyFormat, null, value, 0, value.length(), out, false);
            fail("Expected IOException from printAndEscape");
        } catch (Exception e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof IOException);
            assertEquals("printAndEscape invoked", cause.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_Minimal_EmptyLenNewRecordTrue() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        CharSequence value = "";
        invokePrintAndQuote(null, value, 0, 0, out, true);
        verify(out).append('"');
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_Minimal_EmptyLenNewRecordFalse() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        CharSequence value = "";
        invokePrintAndQuote(null, value, 0, 0, out, false);
        verify(out).append(value, 0, 0);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_Minimal_NewRecordCharLessThan0x20() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        CharSequence value = "\u001Fabc"; // char at pos 0 < 0x20
        invokePrintAndQuote(null, value, 0, value.length(), out, true);
        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_Minimal_NewRecordCharBetween0x22And0x2B() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        CharSequence value = "#abc"; // char at pos 0 == COMMENT (35)
        invokePrintAndQuote(null, value, 0, value.length(), out, false);
        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_Minimal_ContainsLF() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        CharSequence value = "ab\ncd";
        invokePrintAndQuote(null, value, 0, value.length(), out, false);
        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_Minimal_EndsWithSP() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        CharSequence value = "abc ";
        invokePrintAndQuote(null, value, 0, value.length(), out, false);
        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_Minimal_NoQuoteNeeded() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        CharSequence value = "abc";
        invokePrintAndQuote(null, value, 0, value.length(), out, false);
        verify(out).append(value, 0, value.length());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_Minimal_QuoteCharInsideValue() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        // Value containing quote char inside to test doubling
        CharSequence value = "ab\"cd";
        invokePrintAndQuote(null, value, 0, value.length(), out, false);
        verify(out).append('"');
        // The append calls with parts containing quote char doubled
        verify(out, atLeastOnce()).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_UnexpectedQuoteMode() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(null);
        // Use spy to override getQuoteMode to return an unexpected value
        CSVFormat spyFormat = spy(csvFormat);
        when(spyFormat.getQuoteMode()).thenReturn(null);

        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        method.setAccessible(true);

        Exception exception = assertThrows(Exception.class, () -> {
            method.invoke(spyFormat, null, "abc", 0, 3, out, false);
        });
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof IllegalStateException);
        assertTrue(cause.getMessage().contains("Unexpected Quote value"));
    }

}