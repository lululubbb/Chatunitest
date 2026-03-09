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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        Iterable<Object> values = Arrays.asList("value1", "value2");
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, times(2)).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjectArrays() throws IOException {
        Object[] arr1 = new Object[] {"a", "b"};
        Object[] arr2 = new Object[] {"c", "d"};
        Iterable<Object> values = Arrays.asList(arr1, arr2);
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, times(2)).printRecord((Object[]) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfIterables() throws IOException {
        Iterable<String> inner1 = Arrays.asList("x", "y");
        Iterable<String> inner2 = Arrays.asList("z");
        Iterable<Object> values = Arrays.asList(inner1, inner2);
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, times(2)).printRecord((Iterable<?>) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMixedIterable() throws IOException {
        Object[] array = new Object[] {"arrayVal"};
        Iterable<String> iterable = Arrays.asList("iterVal");
        String single = "singleVal";
        Iterable<Object> values = Arrays.asList(array, iterable, single);
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, times(1)).printRecord(array);
        verify(spyPrinter, times(1)).printRecord(iterable);
        verify(spyPrinter, times(1)).printRecord((Object) single);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_reflectionInvocation() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Iterable<Object> values = Arrays.asList("value1", new Object[] {"arr"}, Arrays.asList("iter"));
        Method method = CSVPrinter.class.getDeclaredMethod("printRecords", Iterable.class);
        method.setAccessible(true);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        method.invoke(spyPrinter, values);

        // Disambiguate overloaded printRecord methods by using reflection for verification

        // Verify printRecord(Object)
        Method printRecordObject = CSVPrinter.class.getDeclaredMethod("printRecord", Object.class);
        printRecordObject.setAccessible(true);
        verify(spyPrinter, times(1)).printRecord((Object) any());

        // Verify printRecord(Object[])
        Method printRecordArray = CSVPrinter.class.getDeclaredMethod("printRecord", Object[].class);
        printRecordArray.setAccessible(true);
        verify(spyPrinter, times(1)).printRecord((Object[]) any());

        // Verify printRecord(Iterable<?>)
        Method printRecordIterable = CSVPrinter.class.getDeclaredMethod("printRecord", Iterable.class);
        printRecordIterable.setAccessible(true);
        verify(spyPrinter, times(1)).printRecord((Iterable<?>) any());
    }
}