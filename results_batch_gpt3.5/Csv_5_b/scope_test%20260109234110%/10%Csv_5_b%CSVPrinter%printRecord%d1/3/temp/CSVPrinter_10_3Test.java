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

class CSVPrinter_10_3Test {

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
    void testPrintRecord_withEmptyIterable() throws IOException {
        printer.printRecord(Collections.emptyList());
        // Should call println() after no print calls
        verify(out, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withSingleValue() throws IOException {
        // Spy the printer to verify print(Object) calls
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecord(Collections.singletonList("value1"));

        InOrder inOrder = inOrder(spyPrinter, out);
        inOrder.verify(spyPrinter).print("value1");
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecord(Arrays.asList("val1", "val2", "val3"));

        InOrder inOrder = inOrder(spyPrinter, out);
        inOrder.verify(spyPrinter).print("val1");
        inOrder.verify(spyPrinter).print("val2");
        inOrder.verify(spyPrinter).print("val3");
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrivatePrintObjectCharSequenceIntInt() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Use reflection to invoke private print(Object, CharSequence, int, int)
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Prepare parameters
        String object = "testObject";
        CharSequence value = "testValue";
        int offset = 0;
        int len = value.length();

        // Invoke private method
        printMethod.invoke(printer, object, value, offset, len);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrivatePrintAndEscape() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        CharSequence value = "escapeTest";
        int offset = 0;
        int len = value.length();

        printAndEscapeMethod.invoke(printer, value, offset, len);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrivatePrintAndQuote() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);

        Object obj = "quoteObject";
        CharSequence value = "quoteValue";
        int offset = 0;
        int len = value.length();

        printAndQuoteMethod.invoke(printer, obj, value, offset, len);
    }
}