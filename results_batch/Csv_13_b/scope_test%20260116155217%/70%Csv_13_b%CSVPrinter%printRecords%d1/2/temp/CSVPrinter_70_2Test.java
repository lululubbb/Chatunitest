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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_70_2Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withSingleValues() throws IOException {
        // single values: all non-iterable and non-array
        printer = spy(printer);
        Object v1 = "value1";
        Object v2 = 123;
        Object v3 = 45.6;

        printer.printRecords(v1, v2, v3);

        // verify printRecord called for each value individually
        verify(printer).printRecord(v1);
        verify(printer).printRecord(v2);
        verify(printer).printRecord(v3);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArray() throws IOException {
        printer = spy(printer);
        Object[] array = new Object[] {"a", "b", "c"};

        printer.printRecords((Object) array);

        verify(printer).printRecord(array);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterable() throws IOException {
        printer = spy(printer);
        Iterable<String> iterable = Arrays.asList("x", "y", "z");

        printer.printRecords((Object) iterable);

        verify(printer).printRecord(iterable);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMixedValues() throws IOException {
        printer = spy(printer);
        Object[] array = new Object[] {"array1", "array2"};
        Iterable<String> iterable = Arrays.asList("iter1", "iter2");
        String single = "single";

        printer.printRecords(array, iterable, single);

        verify(printer).printRecord(array);
        verify(printer).printRecord(iterable);
        verify(printer).printRecord(single);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmpty() throws IOException {
        printer = spy(printer);
        // no values
        printer.printRecords();

        // verify printRecord(Object...) was never called
        try {
            Method printRecordObjectArray = CSVPrinter.class.getMethod("printRecord", Object[].class);
            verify(printer, never()).printRecord((Object[]) any());
        } catch (NoSuchMethodException e) {
            fail("Method printRecord(Object...) not found");
        }
        // verify printRecord(Iterable<?>) was never called
        try {
            Method printRecordIterable = CSVPrinter.class.getMethod("printRecord", Iterable.class);
            verify(printer, never()).printRecord((Iterable<?>) any());
        } catch (NoSuchMethodException e) {
            fail("Method printRecord(Iterable<?>) not found");
        }
    }
}