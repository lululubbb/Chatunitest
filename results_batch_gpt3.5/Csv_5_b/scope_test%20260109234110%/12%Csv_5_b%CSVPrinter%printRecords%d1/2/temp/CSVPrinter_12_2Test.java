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

class CSVPrinter_12_2Test {

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
    void testPrintRecords_withIterableOfObjectArrays() throws IOException {
        Object[] record1 = new Object[] { "a", "b" };
        Object[] record2 = new Object[] { "c", "d" };
        Iterable<Object[]> values = Arrays.asList(record1, record2);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord(record1);
        inOrder.verify(spyPrinter).printRecord(record2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfIterables() throws IOException {
        Iterable<String> inner1 = Arrays.asList("x", "y");
        Iterable<String> inner2 = Arrays.asList("z");
        Iterable<Iterable<String>> values = Arrays.asList(inner1, inner2);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord(inner1);
        inOrder.verify(spyPrinter).printRecord(inner2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfSingleObjects() throws IOException {
        Iterable<String> values = Arrays.asList("one", "two");

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord("one");
        inOrder.verify(spyPrinter).printRecord("two");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        // Disambiguate the overloaded printRecord method by specifying varargs explicitly
        verify(spyPrinter, never()).printRecord((Object[]) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_reflectionInvocation() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Iterable<String> values = Arrays.asList("alpha", "beta");

        Method method = CSVPrinter.class.getDeclaredMethod("printRecords", Iterable.class);
        method.setAccessible(true);

        CSVPrinter spyPrinter = spy(printer);

        method.invoke(spyPrinter, values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord("alpha");
        inOrder.verify(spyPrinter).printRecord("beta");
        inOrder.verifyNoMoreInteractions();
    }
}