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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_11_6Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        Object[] values = { "abc", 123, null };

        CSVPrinter spyPrinter = spy(printer);

        // do nothing on println to avoid IOException
        doNothing().when(spyPrinter).println();

        // do nothing on print(Object) to avoid side effects
        doNothing().when(spyPrinter).print(any());

        spyPrinter.printRecord(values);

        InOrder inOrder = inOrder(spyPrinter);
        for (Object value : values) {
            inOrder.verify(spyPrinter).print(value);
        }
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyValues() throws IOException {
        Object[] values = {};

        CSVPrinter spyPrinter = spy(printer);

        doNothing().when(spyPrinter).println();

        spyPrinter.printRecord(values);

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintPrivateMethodsViaReflection() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Reflection to invoke private print methods to ensure coverage indirectly

        // private void print(Object object, CharSequence value, int offset, int len)
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // private void printAndEscape(CharSequence value, int offset, int len)
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        // private void printAndQuote(Object object, CharSequence value, int offset, int len)
        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);

        // Prepare parameters
        String sampleValue = "sample";
        Object sampleObject = "obj";

        // Invoke print(Object, CharSequence, int, int)
        printMethod.invoke(printer, sampleObject, sampleValue, 0, sampleValue.length());

        // Invoke printAndEscape(CharSequence, int, int)
        printAndEscapeMethod.invoke(printer, sampleValue, 0, sampleValue.length());

        // Invoke printAndQuote(Object, CharSequence, int, int)
        printAndQuoteMethod.invoke(printer, sampleObject, sampleValue, 0, sampleValue.length());
    }
}