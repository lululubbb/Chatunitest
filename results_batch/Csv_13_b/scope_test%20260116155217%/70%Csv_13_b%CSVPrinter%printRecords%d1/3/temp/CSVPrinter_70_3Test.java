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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_70_3Test {

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
    void testPrintRecords_withSingleObjects() throws IOException {
        Object obj1 = "value1";
        Object obj2 = 123;
        Object obj3 = null;

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(obj1, obj2, obj3);

        verify(spyPrinter).printRecord(obj1);
        verify(spyPrinter).printRecord(obj2);
        verify(spyPrinter).printRecord(obj3);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArray() throws IOException {
        Object[] arr = new Object[] { "a", "b", "c" };

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords((Object) arr);

        verify(spyPrinter).printRecord(arr);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterable() throws IOException {
        List<String> list = Arrays.asList("x", "y", "z");

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords((Object) list);

        verify(spyPrinter).printRecord(list);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMixedTypes() throws IOException {
        Object[] arr = new Object[] { "a", "b" };
        List<String> list = Arrays.asList("x", "y");
        String single = "single";

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(arr, list, single);

        verify(spyPrinter).printRecord(arr);
        verify(spyPrinter).printRecord(list);
        verify(spyPrinter).printRecord(single);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withNoArguments() throws Throwable {
        CSVPrinter spyPrinter = spy(printer);

        // Use doNothing() before calling the method to avoid side effects
        doNothing().when(spyPrinter).printRecord(any(Iterable.class));
        doNothing().when(spyPrinter).printRecord(any(Object[].class));

        spyPrinter.printRecords();

        verify(spyPrinter, never()).printRecord(any(Iterable.class));
        verify(spyPrinter, never()).printRecord(any(Object[].class));
    }

}