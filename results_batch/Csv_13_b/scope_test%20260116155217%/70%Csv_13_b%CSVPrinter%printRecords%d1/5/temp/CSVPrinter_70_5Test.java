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
import org.mockito.Mockito;

class CSVPrinter_70_5Test {

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
    void testPrintRecords_withObjectArray() throws IOException {
        Object[] record1 = new Object[] { "a", "b" };
        Object[] record2 = new Object[] { 1, 2, 3 };

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(record1, record2);

        verify(spyPrinter).printRecord((Object[]) record1);
        verify(spyPrinter).printRecord((Object[]) record2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterable() throws IOException {
        Iterable<String> iterable1 = Arrays.asList("x", "y");
        Iterable<Integer> iterable2 = Collections.singletonList(5);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(iterable1, iterable2);

        verify(spyPrinter).printRecord(iterable1);
        verify(spyPrinter).printRecord(iterable2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withSingleObjects() throws IOException {
        String s = "single";
        Integer i = 10;

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(s, i);

        verify(spyPrinter).printRecord(s);
        verify(spyPrinter).printRecord(i);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_mixedArguments() throws IOException {
        Object[] arr = new Object[] { "a" };
        Iterable<String> iterable = Arrays.asList("b");
        String single = "c";

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(arr, iterable, single);

        verify(spyPrinter).printRecord((Object[]) arr);
        verify(spyPrinter).printRecord(iterable);
        verify(spyPrinter).printRecord(single);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_empty() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords();

        // Verify that printRecord(Object...) was never called
        verify(spyPrinter, never()).printRecord((Object[]) anyVararg());

        // Verify that printRecord(Iterable<?>) was never called
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
    }
}