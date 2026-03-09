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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_67_5Test {

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
    void testPrintRecord_WithNonEmptyIterable() throws IOException {
        Iterable<Object> values = Arrays.asList("value1", 123, null);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        doCallRealMethod().when(spyPrinter).printRecord(any(Iterable.class));
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecord(values);

        verify(spyPrinter, times(3)).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_WithEmptyIterable() throws IOException {
        Iterable<Object> values = Arrays.asList();

        CSVPrinter spyPrinter = Mockito.spy(printer);
        doCallRealMethod().when(spyPrinter).printRecord(any(Iterable.class));
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecord(values);

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_ReflectionInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Iterable<Object> values = Arrays.asList("a", "b");

        Method printRecordMethod = CSVPrinter.class.getDeclaredMethod("printRecord", Iterable.class);
        printRecordMethod.setAccessible(true);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        // Use invokeExact if available, otherwise invoke
        printRecordMethod.invoke(spyPrinter, values);

        verify(spyPrinter, times(2)).print(any());
        verify(spyPrinter).println();
    }
}