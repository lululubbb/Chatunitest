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
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_69_2Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = Mockito.mock(Appendable.class);
        format = Mockito.mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjectArrays() throws IOException {
        Object[] record1 = new Object[] { "a", "b" };
        Object[] record2 = new Object[] { "c", "d" };
        Iterable<Object[]> iterable = Arrays.asList(record1, record2);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(iterable);

        verify(spyPrinter, times(1)).printRecord((Object[]) record1);
        verify(spyPrinter, times(1)).printRecord((Object[]) record2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfIterables() throws IOException {
        Iterable<String> inner1 = Arrays.asList("a", "b");
        Iterable<String> inner2 = Arrays.asList("c", "d");
        Iterable<Iterable<String>> iterable = Arrays.asList(inner1, inner2);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(iterable);

        verify(spyPrinter, times(1)).printRecord(inner1);
        verify(spyPrinter, times(1)).printRecord(inner2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjects() throws IOException {
        Iterable<String> iterable = Arrays.asList("a", "b", "c");

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(iterable);

        verify(spyPrinter, times(3)).printRecord(any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        Iterable<Object> emptyIterable = Collections.emptyList();

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(emptyIterable);

        verify(spyPrinter, never()).printRecord(any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withNullValueInIterable() throws IOException {
        Iterable<Object> iterable = Arrays.asList((Object) null);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(iterable);

        verify(spyPrinter, times(1)).printRecord((Object) isNull());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscape_methodInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, "abc", 0, 3);

        verify(out, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuote_methodInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, "obj", "quoted", 0, 6);

        verify(out, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrint_methodInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, "obj", "value", 0, 5);

        verify(out, atLeastOnce()).append(anyChar());
    }
}