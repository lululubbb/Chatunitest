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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinter_13_4Test {

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
    void testPrintRecords_withArrayContainingObjectArrayIterableAndSingleObject() throws IOException {
        Object[] innerArray = new Object[] { "inner1", "inner2" };
        Iterable<String> iterable = Arrays.asList("iter1", "iter2");
        Object singleObject = "single";

        Object[] values = new Object[] { innerArray, iterable, singleObject };

        CSVPrinter spyPrinter = spy(printer);

        doNothing().when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).printRecord((Iterable<?>) any());
        doNothing().when(spyPrinter).printRecord((Object) any());

        spyPrinter.printRecords(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord((Object[]) innerArray);
        inOrder.verify(spyPrinter).printRecord((Iterable<?>) iterable);
        inOrder.verify(spyPrinter).printRecord(singleObject);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_emptyArray() throws IOException {
        Object[] values = new Object[0];
        CSVPrinter spyPrinter = spy(printer);

        doNothing().when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).printRecord((Iterable<?>) any());
        doNothing().when(spyPrinter).printRecord((Object) any());

        spyPrinter.printRecords(values);

        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_nullValueInArray() throws IOException {
        Object[] values = new Object[] { null };
        CSVPrinter spyPrinter = spy(printer);

        doNothing().when(spyPrinter).printRecord((Object) any());

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord((Object) null);
    }
}