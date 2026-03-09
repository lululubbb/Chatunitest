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
import org.mockito.Mockito;

class CSVPrinter_10_5Test {

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
    void testPrintRecord_withIterable_callsPrintForEachAndPrintln() throws IOException {
        Iterable<Object> values = Arrays.asList("value1", 123, null);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecord(values);

        for (Object value : values) {
            verify(spyPrinter).print(value);
        }
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyIterable_callsPrintlnOnly() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecord(values);

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintObjectValue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        String value = "testValue";
        printMethod.invoke(printer, value, value, 0, value.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscape() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        String value = "escape,test";
        printAndEscapeMethod.invoke(printer, value, 0, value.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuote() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);

        String value = "quote,test";
        printAndQuoteMethod.invoke(printer, value, value, 0, value.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

}