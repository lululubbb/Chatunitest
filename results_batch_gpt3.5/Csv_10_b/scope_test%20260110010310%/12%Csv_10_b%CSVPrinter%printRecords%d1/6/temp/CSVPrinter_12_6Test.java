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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_12_6Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    private void verifyPrintRecordCalledWith(CSVPrinter spyPrinter, Object arg, int times) throws Exception {
        if (arg instanceof Iterable) {
            verify(spyPrinter, times(times)).printRecord((Iterable<?>) arg);
        } else if (arg != null && arg.getClass().isArray()) {
            verify(spyPrinter, times(times)).printRecord((Object[]) arg);
        } else {
            verify(spyPrinter, times(times)).printRecord(arg);
        }
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjects() throws IOException, Exception {
        Iterable<Object> values = Arrays.asList("one", "two", "three");

        CSVPrinter spyPrinter = spy(printer);
        spyPrinter.printRecords(values);

        // Use reflection to get printRecord(Object...) method
        Method printRecordVarargs = CSVPrinter.class.getMethod("printRecord", Object[].class);

        // Verify printRecord called 3 times with any Object[] argument
        verify(spyPrinter, times(3)).printRecord((Object[]) any());

        // Verify individual calls using reflection to avoid ambiguity
        // Note: verify() returns the mock, so we can use it as the target for invoke
        printRecordVarargs.invoke(verify(spyPrinter, times(1)), new Object[]{new Object[]{"one"}});
        printRecordVarargs.invoke(verify(spyPrinter, times(1)), new Object[]{new Object[]{"two"}});
        printRecordVarargs.invoke(verify(spyPrinter, times(1)), new Object[]{new Object[]{"three"}});
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfIterable() throws IOException {
        Iterable<Object> inner1 = Arrays.asList("a", "b");
        Iterable<Object> inner2 = Collections.singletonList("c");
        Iterable<Iterable<?>> values = Arrays.asList(inner1, inner2);

        CSVPrinter spyPrinter = spy(printer);
        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(inner1);
        verify(spyPrinter).printRecord(inner2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjectArrays() throws IOException {
        Object[] arr1 = new Object[]{"x", "y"};
        Object[] arr2 = new Object[]{"z"};
        Iterable<Object> values = Arrays.asList(arr1, arr2);

        CSVPrinter spyPrinter = spy(printer);
        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(arr1);
        verify(spyPrinter).printRecord(arr2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        CSVPrinter spyPrinter = spy(printer);
        spyPrinter.printRecords(values);

        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object[]) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMixedTypes() throws IOException {
        Object[] arr = new Object[]{"arr"};
        Iterable<Object> iterable = Arrays.asList("i1", "i2");
        Object simple = "simple";

        Iterable<Object> values = Arrays.asList(arr, iterable, simple);

        CSVPrinter spyPrinter = spy(printer);
        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(arr);
        verify(spyPrinter).printRecord(iterable);
        verify(spyPrinter).printRecord(simple);
    }
}