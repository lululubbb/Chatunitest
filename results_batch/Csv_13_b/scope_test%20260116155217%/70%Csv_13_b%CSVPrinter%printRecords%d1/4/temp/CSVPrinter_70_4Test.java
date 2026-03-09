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
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_70_4Test {

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
    void testPrintRecords_withSingleObjects() throws IOException {
        CSVPrinter spyPrinter = spy(printer);

        doNothing().when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).printRecord((Iterable<?>) any());
        doNothing().when(spyPrinter).printRecord(any());

        Object v1 = "value1";
        Object v2 = 123;
        spyPrinter.printRecords(v1, v2);

        verify(spyPrinter, times(1)).printRecord(v1);
        verify(spyPrinter, times(1)).printRecord(v2);
        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArray() throws IOException {
        CSVPrinter spyPrinter = spy(printer);

        doNothing().when(spyPrinter).printRecord((Iterable<?>) any());
        doNothing().when(spyPrinter).printRecord(any());

        Object[] array = new Object[] {"a", "b"};
        spyPrinter.printRecords((Object) array);

        verify(spyPrinter, times(1)).printRecord(array);
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterable() throws IOException {
        CSVPrinter spyPrinter = spy(printer);

        doNothing().when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).printRecord(any());

        Iterable<String> iterable = Arrays.asList("x", "y");
        spyPrinter.printRecords((Object) iterable);

        verify(spyPrinter, times(1)).printRecord(iterable);
        verify(spyPrinter, never()).printRecord((Object[]) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMixedValues() throws IOException {
        CSVPrinter spyPrinter = spy(printer);

        doNothing().when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).printRecord((Iterable<?>) any());
        doNothing().when(spyPrinter).printRecord(any());

        Object[] array = new Object[] {"a", "b"};
        Iterable<String> iterable = Arrays.asList("x", "y");
        Object simple = "simple";

        spyPrinter.printRecords(array, iterable, simple);

        verify(spyPrinter, times(1)).printRecord(array);
        verify(spyPrinter, times(1)).printRecord(iterable);
        verify(spyPrinter, times(1)).printRecord(simple);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmpty() throws IOException {
        CSVPrinter spyPrinter = spy(printer);

        doNothing().when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).printRecord((Iterable<?>) any());

        spyPrinter.printRecords();

        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
    }
}