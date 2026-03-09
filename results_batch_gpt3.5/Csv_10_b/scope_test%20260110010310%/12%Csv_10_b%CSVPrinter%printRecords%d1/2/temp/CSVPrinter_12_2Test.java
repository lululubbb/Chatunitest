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

class CSVPrinter_12_2Test {

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
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord((Object) "value1");
        inOrder.verify(spyPrinter).printRecord((Object) "value2");
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjectArrays() throws IOException {
        Object[] arr1 = new Object[] { "a", "b" };
        Object[] arr2 = new Object[] { "c", "d" };
        Iterable<Object> values = Arrays.asList(arr1, arr2);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord(arr1);
        inOrder.verify(spyPrinter).printRecord(arr2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfIterables() throws IOException {
        Iterable<String> inner1 = Arrays.asList("x", "y");
        Iterable<String> inner2 = Collections.singletonList("z");
        Iterable<Object> values = Arrays.asList(inner1, inner2);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord(inner1);
        inOrder.verify(spyPrinter).printRecord(inner2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        Iterable<Object> values = Collections.emptyList();
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Object) any());
    }
}