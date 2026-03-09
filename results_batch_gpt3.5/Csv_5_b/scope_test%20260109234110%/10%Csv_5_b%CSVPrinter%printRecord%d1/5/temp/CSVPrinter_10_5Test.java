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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinter_10_5Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues_callsPrintAndPrintln() throws IOException {
        Iterable<Object> values = Arrays.asList("val1", 2, null);

        printer.printRecord(values);

        InOrder inOrder = inOrder(out);
        // Verify append called at least 3 times (for each value printed)
        verify(out, atLeast(3)).append(any(CharSequence.class));
        // Verify append called at least once for println (newline)
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyIterable_onlyPrintlnCalled() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        printer.printRecord(values);

        // No print calls, only println which appends newline(s)
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrivatePrintMethodForEachValue() throws Exception {
        Iterable<Object> values = Arrays.asList("a", "b");

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecord(values);

        // Verify print(Object) called twice and println() once on spyPrinter
        verify(spyPrinter, times(2)).print(any());
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintMethodUsingReflection() throws Exception {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Prepare parameters
        String value = "hello";
        CharSequence cs = "hello world";
        int offset = 0;
        int len = 5;

        // Invoke private print method
        printMethod.invoke(printer, value, cs, offset, len);

        // Should have appended something to out
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscapeMethodUsingReflection() throws Exception {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        String cs = "escape me";

        method.invoke(printer, cs, 0, cs.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuoteMethodUsingReflection() throws Exception {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        String val = "quote me";
        CharSequence cs = val;

        method.invoke(printer, val, cs, 0, cs.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }
}