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
import org.mockito.InOrder;

class CSVPrinter_12_6Test {

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
    void testPrintRecords_withIterableOfObjects() throws IOException {
        Iterable<Object> values = Arrays.asList("value1", "value2", "value3");
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord((Object) "value1");
        inOrder.verify(spyPrinter).printRecord((Object) "value2");
        inOrder.verify(spyPrinter).printRecord((Object) "value3");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfIterable() throws IOException {
        Iterable<Object> inner1 = Arrays.asList("a", "b");
        Iterable<Object> inner2 = Arrays.asList("c", "d");
        Iterable<Iterable<?>> values = Arrays.asList(inner1, inner2);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord((Iterable<?>) inner1);
        inOrder.verify(spyPrinter).printRecord((Iterable<?>) inner2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjectArray() throws IOException {
        Object[] arr1 = new Object[] {1, 2};
        Object[] arr2 = new Object[] {3, 4};
        Iterable<Object> values = Arrays.asList(arr1, arr2);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord(arr1);
        inOrder.verify(spyPrinter).printRecord(arr2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        Iterable<Object> values = Collections.emptyList();
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        // Use explicit cast to resolve ambiguity
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withNullValueInIterable() throws IOException {
        Iterable<Object> values = Arrays.asList((Object) null);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord((Object) isNull());
        inOrder.verifyNoMoreInteractions();
    }
}