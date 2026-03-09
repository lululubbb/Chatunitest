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

class CSVPrinter_69_6Test {

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
    void testPrintRecords_withIterableOfObjects() throws IOException {
        Iterable<Object> values = Arrays.asList("a", "b", "c");

        // Spy printer to verify printRecord(Object) call
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, times(3)).printRecord((Object) any());
        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfIterable() throws IOException {
        Iterable<Object> innerIterable1 = Arrays.asList("x", "y");
        Iterable<Object> innerIterable2 = Collections.singletonList("z");
        Iterable<Object> values = Arrays.asList(innerIterable1, innerIterable2);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, times(2)).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjectArrays() throws IOException {
        Object[] array1 = new Object[] {1, 2};
        Object[] array2 = new Object[] {3, 4};
        Iterable<Object> values = Arrays.asList(array1, array2);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, times(2)).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_mixedTypes() throws IOException {
        Object[] array = new Object[] {"array"};
        Iterable<Object> iterable = Arrays.asList("iter1", "iter2");
        Object single = "single";

        Iterable<Object> values = Arrays.asList(array, iterable, single);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, times(1)).printRecord((Object[]) eq(array));
        verify(spyPrinter, times(1)).printRecord((Iterable<?>) eq(iterable));
        verify(spyPrinter, times(1)).printRecord((Object) eq(single));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_emptyIterable() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, never()).printRecord((Object) any());
        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_privateMethodInvocation() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Use reflection to invoke private printRecords(Object...) method with mixed inputs
        Method privatePrintRecords = CSVPrinter.class.getDeclaredMethod("printRecords", Object[].class);
        privatePrintRecords.setAccessible(true);

        Object[] params = new Object[] { "one", new Object[] { "two", "three" }, Arrays.asList("four", "five") };

        // Spy to verify calls to printRecord methods
        CSVPrinter spyPrinter = spy(printer);

        // Invoke private method - cast to Object to avoid varargs ambiguity
        privatePrintRecords.invoke(spyPrinter, (Object) params);

        verify(spyPrinter, times(1)).printRecord((Object) eq("one"));
        verify(spyPrinter, times(1)).printRecord((Object[]) aryEq(new Object[] { "two", "three" }));
        verify(spyPrinter, times(1)).printRecord((Iterable<?>) eq(Arrays.asList("four", "five")));
    }
}