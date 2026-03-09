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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinter_67_2Test {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        Iterable<Object> values = Arrays.asList("value1", 123, null);

        CSVPrinter spyPrinter = spy(printer);
        // stub print(Object) to do nothing to isolate test
        doNothing().when(spyPrinter).print(any());

        spyPrinter.printRecord(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).print("value1");
        inOrder.verify(spyPrinter).print(123);
        inOrder.verify(spyPrinter).print((Object) null);
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyIterable() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).print(any());

        spyPrinter.printRecord(values);

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrivatePrintAndEscape() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Access private method printAndEscape(CharSequence, int, int)
        Method printAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscape.setAccessible(true);

        // Invoke private method with sample input
        printAndEscape.invoke(printer, "escapeTest", 0, "escapeTest".length());

        // No exception means success; no output verification possible since Appendable is mocked
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrivatePrintAndQuote() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Access private method printAndQuote(Object, CharSequence, int, int)
        Method printAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuote.setAccessible(true);

        printAndQuote.invoke(printer, "obj", "quoteTest", 0, "quoteTest".length());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrivatePrint_Object_CharSequence_Int_Int() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        printMethod.invoke(printer, "obj", "printTest", 0, "printTest".length());
    }
}