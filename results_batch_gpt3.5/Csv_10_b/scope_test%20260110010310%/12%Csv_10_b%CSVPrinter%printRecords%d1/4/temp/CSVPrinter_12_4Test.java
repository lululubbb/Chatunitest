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
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinter_12_4Test {

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
    void testPrintRecords_withIterableOfObjects_callsPrintRecordWithObject() throws IOException {
        Iterable<Object> input = Arrays.asList("one", "two", "three");
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(input);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord("one");
        inOrder.verify(spyPrinter).printRecord("two");
        inOrder.verify(spyPrinter).printRecord("three");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjectArrays_callsPrintRecordWithObjectArray() throws IOException {
        Object[] array1 = new Object[] { "a", "b" };
        Object[] array2 = new Object[] { "c", "d" };
        Iterable<Object> input = Arrays.asList(array1, array2);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(input);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord(array1);
        inOrder.verify(spyPrinter).printRecord(array2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfIterables_callsPrintRecordWithIterable() throws IOException {
        Iterable<String> inner1 = Arrays.asList("x", "y");
        Iterable<String> inner2 = Collections.singletonList("z");
        Iterable<Iterable<String>> input = Arrays.asList(inner1, inner2);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(input);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord(inner1);
        inOrder.verify(spyPrinter).printRecord(inner2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMixedIterable_callsAppropriatePrintRecord() throws IOException {
        Object[] objArray = new Object[] { "1", "2" };
        Iterable<String> iterable = Arrays.asList("a", "b");
        String singleObject = "single";

        Iterable<Object> input = Arrays.asList(objArray, iterable, singleObject);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(input);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord(objArray);
        inOrder.verify(spyPrinter).printRecord(iterable);
        inOrder.verify(spyPrinter).printRecord(singleObject);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable_doesNotCallPrintRecord() throws IOException {
        Iterable<Object> input = Collections.emptyList();
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(input);

        try {
            Method printRecordIterable = CSVPrinter.class.getMethod("printRecord", Iterable.class);
            verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        } catch (NoSuchMethodException e) {
            fail("Method printRecord(Iterable<?>) not found");
        }

        try {
            Method printRecordVarargs = CSVPrinter.class.getMethod("printRecord", Object[].class);
            verify(spyPrinter, never()).printRecord((Object[]) any());
        } catch (NoSuchMethodException e) {
            fail("Method printRecord(Object...) not found");
        }

        try {
            Method printRecordObject = CSVPrinter.class.getMethod("printRecord", Object.class);
            verify(spyPrinter, never()).printRecord((Object) any());
        } catch (NoSuchMethodException e) {
            fail("Method printRecord(Object) not found");
        }
    }

}