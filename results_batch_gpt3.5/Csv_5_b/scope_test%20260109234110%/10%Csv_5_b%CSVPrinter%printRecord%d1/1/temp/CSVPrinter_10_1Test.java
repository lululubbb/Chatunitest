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
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_10_1Test {

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
    void testPrintRecord_withMultipleValues() throws IOException {
        Iterable<Object> values = Arrays.asList("val1", 2, null);

        // Spy the printer to verify calls to print(Object)
        CSVPrinter spyPrinter = Mockito.spy(printer);

        // Use doCallRealMethod() to ensure printRecord calls real method on spy
        doCallRealMethod().when(spyPrinter).printRecord(any(Iterable.class));
        // Also call real print(Object) method to avoid mocking it fully
        doCallRealMethod().when(spyPrinter).print(any());

        spyPrinter.printRecord(values);

        verify(spyPrinter, times(3)).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyIterable() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        CSVPrinter spyPrinter = Mockito.spy(printer);

        doCallRealMethod().when(spyPrinter).printRecord(any(Iterable.class));
        doCallRealMethod().when(spyPrinter).print(any());

        spyPrinter.printRecord(values);

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withThrowsIOException() throws IOException {
        Iterable<Object> values = Arrays.asList("val1");
        CSVPrinter spyPrinter = Mockito.spy(printer);

        doCallRealMethod().when(spyPrinter).printRecord(any(Iterable.class));
        doThrow(new IOException("test exception")).when(spyPrinter).print("val1");

        IOException thrown = assertThrows(IOException.class, () -> spyPrinter.printRecord(values));
        assertEquals("test exception", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintMethodViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Call with simple parameters
        printMethod.invoke(printer, "object", "value", 0, 5);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscapeMethodViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, "escapeMe", 0, 8);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuoteMethodViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, "obj", "quoteMe", 0, 7);
    }
}