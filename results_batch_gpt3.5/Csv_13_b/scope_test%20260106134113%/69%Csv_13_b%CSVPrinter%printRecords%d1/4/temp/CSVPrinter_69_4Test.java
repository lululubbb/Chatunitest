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

class CSVPrinter_69_4Test {

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
        Iterable<Object> iterable = Arrays.asList("one", "two", "three");

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(iterable);

        for (Object val : iterable) {
            verify(spyPrinter).printRecord(val);
        }
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjectArrays() throws IOException {
        Object[] arr1 = new Object[] {"a", "b"};
        Object[] arr2 = new Object[] {"c", "d"};
        Iterable<Object> iterable = Arrays.asList(arr1, arr2);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(iterable);

        verify(spyPrinter).printRecord(arr1);
        verify(spyPrinter).printRecord(arr2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfIterables() throws IOException {
        Iterable<String> inner1 = Arrays.asList("x", "y");
        Iterable<String> inner2 = Arrays.asList("z");
        Iterable<Object> iterable = Arrays.asList(inner1, inner2);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(iterable);

        verify(spyPrinter).printRecord(inner1);
        verify(spyPrinter).printRecord(inner2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMixedValues() throws IOException {
        Object[] arr = new Object[] {"arr1"};
        Iterable<String> iterable = Collections.singletonList("iter1");
        String single = "single";

        Iterable<Object> values = Arrays.asList(arr, iterable, single);
        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(arr);
        verify(spyPrinter).printRecord(iterable);
        verify(spyPrinter).printRecord(single);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_reflectionInvoke() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Iterable<Object> iterable = Arrays.asList("one", "two");
        Method method = CSVPrinter.class.getDeclaredMethod("printRecords", Iterable.class);
        method.setAccessible(true);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        method.invoke(spyPrinter, iterable);

        for (Object val : iterable) {
            verify(spyPrinter).printRecord(val);
        }
    }
}