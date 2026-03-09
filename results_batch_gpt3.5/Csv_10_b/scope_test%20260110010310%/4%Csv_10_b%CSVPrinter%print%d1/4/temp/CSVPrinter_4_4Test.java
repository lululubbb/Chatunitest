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

class CSVPrinter_4_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        // Mock format.getNullString() to return null by default to avoid NPE in constructor or print
        when(format.getNullString()).thenReturn(null);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull() throws IOException {
        when(format.getNullString()).thenReturn(null);
        printer.print(null);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNotNull() throws IOException {
        when(format.getNullString()).thenReturn("NULL");
        printer.print(null);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws IOException {
        String value = "value";
        printer.print(value);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_invokesPrivatePrint() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        String testValue = "test";
        privatePrint.invoke(printer, testValue, testValue, 0, testValue.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrintAndEscapeAndQuoteCoverage() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method printAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscape.setAccessible(true);

        Method printAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuote.setAccessible(true);

        String escapeValue = "escape\nvalue\r\n";
        printAndEscape.invoke(printer, escapeValue, 0, escapeValue.length());
        verify(out, atLeastOnce()).append(any(CharSequence.class));

        String quoteValue = "quote,value";
        printAndQuote.invoke(printer, quoteValue, quoteValue, 0, quoteValue.length());
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }
}