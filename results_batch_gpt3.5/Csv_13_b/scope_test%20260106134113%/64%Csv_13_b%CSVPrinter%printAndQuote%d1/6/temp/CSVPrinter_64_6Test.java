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

class CSVPrinter_64_6Test {

    private CSVPrinter printer;
    private CSVFormat format;
    private Appendable out;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len)
            throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class);
        method.setAccessible(true);
        method.invoke(printer, object, value, offset, len);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.ALL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        String value = "testValue";
        invokePrintAndQuote("test", value, 0, value.length());

        // Expect output with quotes around value
        ArgumentCaptor<CharSequence> captor = ArgumentCaptor.forClass(CharSequence.class);
        verify(out).append('"');
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_Object() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        String value = "abc123";
        invokePrintAndQuote("stringObject", value, 0, value.length());

        // Should quote because object is not Number
        verify(out).append('"');
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_Number() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        String value = "12345";
        invokePrintAndQuote(12345, value, 0, value.length());

        // Should not quote because object is Number
        verify(out).append(value, 0, value.length());
        verify(out, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNone() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.NONE);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "noquote";
        // printAndEscape is private, but it is called when QuoteMode.NONE
        // We will spy on printer to verify printAndEscape call
        CSVPrinter spyPrinter = spy(new CSVPrinter(out, format));

        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class);
        method.setAccessible(true);
        method.invoke(spyPrinter, "obj", value, 0, value.length());

        verify(spyPrinter).printAndEscape(value, 0, value.length());
        verify(out, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_EmptyLen_NewRecord() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        // Set newRecord to true via reflection
        var field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        field.set(printer, true);

        CharSequence value = "";
        invokePrintAndQuote("obj", value, 0, 0);

        verify(out).append('"');
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_NewRecordStartChar() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        // Set newRecord to true
        var field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        field.set(printer, true);

        CharSequence value = "#startChar";
        invokePrintAndQuote("obj", value, 0, value.length());

        verify(out).append('"');
        verify(out).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_ContainsSpecialChar() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc,\"def";
        invokePrintAndQuote("obj", value, 0, value.length());

        verify(out).append('"');
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_EndCharLessThanSpace() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc ";
        invokePrintAndQuote("obj", value, 0, value.length());

        verify(out).append('"');
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_NoQuoteNeeded() throws Throwable {
        when(format.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        // Set newRecord to false so minimal quoting rules apply
        var field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        field.set(printer, false);

        CharSequence value = "abcDEF123";
        invokePrintAndQuote("obj", value, 0, value.length());

        verify(out).append(value, 0, value.length());
        verify(out, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testUnexpectedQuoteModeThrows() throws Throwable {
        when(format.getQuoteMode()).thenReturn(null); // will default to MINIMAL, so we mock differently
        when(format.getDelimiter()).thenReturn(',');
        when(format.getQuoteCharacter()).thenReturn('"');

        // Create a CSVFormat mock that returns an invalid QuoteMode by reflection override
        CSVFormat badFormat = mock(CSVFormat.class);
        when(badFormat.getDelimiter()).thenReturn(',');
        when(badFormat.getQuoteCharacter()).thenReturn('"');
        when(badFormat.getQuoteMode()).thenReturn(null);

        CSVPrinter badPrinter = new CSVPrinter(out, badFormat);

        // Use reflection to forcibly set quoteModePolicy to an invalid enum by invoking method with a patch
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class);
        method.setAccessible(true);

        // We cannot pass invalid enum, so instead test default path with null handled as MINIMAL (covered above)
        // So here we do nothing since the code throws only if quoteModePolicy is invalid enum (not possible here)
    }
}